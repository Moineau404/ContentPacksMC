package mod.moineau.contentpacks.api.client.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mod.moineau.contentpacks.api.client.render.CustomTextureRegistry;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderChestBlock.class)
public class EnderChestBlockMixin {
    @ModifyReturnValue(method = "newBlockEntity", at = @At(value = "RETURN"))
    BlockEntity inject$newBlockEntity_customTexture(BlockEntity blockEntity) {
        CustomTextureRegistry.pass(this, blockEntity);
        return blockEntity;
    }
}
