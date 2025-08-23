package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import net.minecraft.client.render.BlockRenderLayer;

public final class VanillaClientCodecs {
    public static final Codec<BlockRenderLayer> BLOCK_RENDER_LAYER = CodecUtil.enumByName(BlockRenderLayer.class);
}
