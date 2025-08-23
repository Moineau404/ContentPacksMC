package mod.moineau.contentpacks.world.biome;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.biome.ColorResolver;

import java.util.HashMap;
import java.util.Map;

public final class ColorMap implements ColorResolver {
    public static final Map<Identifier, ColorMap> MAP = new HashMap<>();
    public static final Codec<ColorMap> CODEC = Codec.INT.fieldOf("fallback").codec().xmap(ColorMap::new, colorMap -> colorMap.fallback);

    private int[] colorMap = new int[65536];
    private final int fallback;

    public ColorMap(int fallback) {
        this.fallback = fallback;
    }

    public void setColorMap(int[] pixels) {
        this.colorMap = pixels;
    }

    public int getColor(double temperature, double downfall) {
        return BiomeColors.getColor(temperature, downfall, colorMap, fallback);
    }

    @Override
    public int getColor(Biome biome, double x, double z) {
        double d = MathHelper.clamp(biome.weather.temperature, 0.0F, 1.0F);
        double e = MathHelper.clamp(biome.weather.downfall, 0.0F, 1.0F);
        return this.getColor(d, e);
    }
}
