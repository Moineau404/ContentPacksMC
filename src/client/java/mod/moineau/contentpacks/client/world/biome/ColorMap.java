package mod.moineau.contentpacks.client.world.biome;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ColorMapColorUtil;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public final class ColorMap implements ColorResolver {
    public static final Map<Identifier, ColorMap> MAP = new HashMap<>();
    public static final Codec<ColorMap> CODEC = Codec.INT.fieldOf("fallback").codec().xmap(ColorMap::new, colorMap -> colorMap.fallback);

    private int[] pixels = new int[65536];
    private final int fallback;

    public ColorMap(int fallback) {
        this.fallback = fallback;
    }

    public void update(int[] pixels) {
        this.pixels = pixels;
    }

    public int getColor(double temperature, double downfall) {
        return ColorMapColorUtil.get(temperature, downfall, pixels, fallback);
    }

    @Override
    public int getColor(Biome biome, double x, double z) {
        double temperature = Mth.clamp(biome.climateSettings.temperature, 0.0F, 1.0F);
        double downfall = Mth.clamp(biome.climateSettings.downfall, 0.0F, 1.0F);
        return this.getColor(temperature, downfall);
    }
}
