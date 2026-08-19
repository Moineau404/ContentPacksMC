package mod.moineau.contentpacks.client.mixin;

import net.minecraft.client.renderer.Sheets;
import org.spongepowered.asm.mixin.Mixin;

// TODO Enchanting Table, Lectern, Banner ?
@Mixin(Sheets.class)
public class SheetsMixin {
//    @Inject(method = "chooseSprite(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/level/block/state/properties/ChestType;Z)Lnet/minecraft/client/resources/model/sprite/SpriteId;", at = @At("HEAD"), cancellable = true)
//    private static void inject$chooseSprite(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<ChestRenderState.ChestMaterialType> callbackInfo) {
//        ChestRendering.getChestTexture(type, blockEntity.getBlockState().getBlock()).ifPresent(callbackInfo::setReturnValue);
//    }
}