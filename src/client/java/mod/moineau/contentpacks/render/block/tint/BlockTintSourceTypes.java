package mod.moineau.contentpacks.render.block.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class BlockTintSourceTypes {
    public static final Codecs.IdMapper<Identifier, MapCodec<? extends BlockTintSource>> ID_MAPPER = new Codecs.IdMapper<>();
    public static final Codec<BlockTintSource> CODEC = ID_MAPPER.getCodec(Identifier.CODEC).dispatch(BlockTintSource::getCodec, Function.identity());

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.ofVanilla("constant"), ConstantBlockTintSource.CODEC);
        ID_MAPPER.put(Identifier.ofVanilla("biome"), BiomeBlockTintSource.CODEC);
        ID_MAPPER.put(Identifier.ofVanilla("grass"), GrassBlockTintSource.CODEC);
    }
}
