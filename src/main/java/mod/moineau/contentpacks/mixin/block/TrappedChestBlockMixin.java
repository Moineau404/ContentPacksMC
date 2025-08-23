package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.CustomTextureProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.TrappedChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(TrappedChestBlock.class)
public class TrappedChestBlockMixin {
    @Shadow
    public static final MapCodec<TrappedChestBlock> CODEC = CustomTextureProvider.createCodec(AbstractBlock.createCodec(TrappedChestBlock::new));
}
