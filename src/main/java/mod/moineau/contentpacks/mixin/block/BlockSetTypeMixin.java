package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(BlockSetType.class)
public class BlockSetTypeMixin {
    @Shadow
    public static final Codec<BlockSetType> CODEC = ContentRegistries.BLOCK_SET_TYPE.getCodec();
}
