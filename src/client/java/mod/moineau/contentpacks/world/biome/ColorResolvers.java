package mod.moineau.contentpacks.world.biome;

import com.mojang.serialization.Codec;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.biome.ColorResolver;

public final class ColorResolvers {
    public static final Codecs.IdMapper<Identifier, ColorResolver> ID_MAPPER = new Codecs.IdMapper<>();
    public static final Codec<ColorResolver> CODEC = ID_MAPPER.getCodec(Identifier.CODEC);

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.ofVanilla("grass"), BiomeColors.GRASS_COLOR);
        ID_MAPPER.put(Identifier.ofVanilla("foliage"), BiomeColors.FOLIAGE_COLOR);
        ID_MAPPER.put(Identifier.ofVanilla("dry_foliage"), BiomeColors.DRY_FOLIAGE_COLOR);
        ID_MAPPER.put(Identifier.ofVanilla("water"), BiomeColors.WATER_COLOR);
    }
}
