package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(TrappedChestBlock.class)
public class TrappedChestBlockMixin {
    @Shadow
    public static final MapCodec<TrappedChestBlock> CODEC = CustomTextureProvider.createCodec(BlockBehaviour.simpleCodec(TrappedChestBlock::new), CustomTextureProvider.MULTI_CHEST_BLOCKS::add);
}
