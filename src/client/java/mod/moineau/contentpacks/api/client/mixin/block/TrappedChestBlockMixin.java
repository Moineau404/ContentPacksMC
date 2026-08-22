package mod.moineau.contentpacks.api.client.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TrappedChestBlock.class)
public class TrappedChestBlockMixin {
    @ModifyReturnValue(method = "newBlockEntity", at = @At(value = "RETURN"))
    BlockEntity inject$newBlockEntity_customTexture(BlockEntity blockEntity) {
        CustomTextureProvider.passTexture(this, blockEntity);
        return blockEntity;
    }
}
