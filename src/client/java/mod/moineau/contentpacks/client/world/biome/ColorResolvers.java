package mod.moineau.contentpacks.client.world.biome;

import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ColorResolver;

public final class ColorResolvers {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, ColorResolver> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<ColorResolver> CODEC = ID_MAPPER.codec(Identifier.CODEC);

    public static void bootStrap() {
        ID_MAPPER.put(Identifier.withDefaultNamespace("grass"), BiomeColors.GRASS_COLOR_RESOLVER);
        ID_MAPPER.put(Identifier.withDefaultNamespace("foliage"), BiomeColors.FOLIAGE_COLOR_RESOLVER);
        ID_MAPPER.put(Identifier.withDefaultNamespace("dry_foliage"), BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER);
        ID_MAPPER.put(Identifier.withDefaultNamespace("water"), BiomeColors.WATER_COLOR_RESOLVER);
    }
}
