package mod.moineau.contentpacks.client.mixin;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.ContentPacksClient;
import net.minecraft.client.GameLoadCookie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.main.GameConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.CommonColors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Final
    public Gui gui;

    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Shadow
    @Final
    private BlockColors blockColors;

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;blockColors:Lnet/minecraft/client/color/block/BlockColors;", shift = At.Shift.AFTER))
    private void inject$init_loaders(GameConfig gameConfig, CallbackInfo ci) {
        ContentPacksClient.getInstance().registerReloadListeners(this.resourceManager, this.blockColors);
    }

    @Inject(method = "onGameLoadFinished", at = @At(value = "TAIL"))
    private void inject$onGameLoadFinished_popup(GameLoadCookie cookie, CallbackInfo ci) {
        int count = ContentPacks.getInstance().getErrors().size();
        if (count > 0) {
            SystemToast.add(
                    gui.toastManager(),
                    ContentPacksClient.TOAST_LOAD_FAILURE,
                    Component.translatable("options.contentpacks.toast.errors.title", count).withColor(CommonColors.SOFT_RED),
                    Component.translatable("options.contentpacks.toast.errors.description").withColor(CommonColors.SOFT_RED)
            );
        }
    }
}