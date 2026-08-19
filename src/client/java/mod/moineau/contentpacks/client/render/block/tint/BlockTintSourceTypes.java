package mod.moineau.contentpacks.client.render.block.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;

public final class BlockTintSourceTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends BlockTintSource>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<BlockTintSource> CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(BlockTintSource::getCodec, Function.identity());

    public static void bootStrap() {
        ID_MAPPER.put(Identifier.withDefaultNamespace("constant"), ConstantBlockTintSource.CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("resolver"), ResolverBlockTintSource.CODEC);
    }
}
