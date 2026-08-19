package mod.moineau.contentpacks.client.mixin;

import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.resource.BlockColorLoader;
import mod.moineau.contentpacks.client.resource.ColorMapReloadListener;
import mod.moineau.contentpacks.client.resource.FluidModelLoader;
import mod.moineau.contentpacks.util.ErrorLogger;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Unique
    private ColorMapReloadListener contentpacks$colorMapReloadListener;

    @Unique
    private BlockColorLoader contentpacks$blockColorLoader;

    @Unique
    private FluidModelLoader contentpacks$fluidModelLoader;

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
        this.contentpacks$colorMapReloadListener = new ColorMapReloadListener();
        this.resourceManager.registerReloadListener(this.contentpacks$colorMapReloadListener);
        this.contentpacks$blockColorLoader = new BlockColorLoader(this.blockColors);
        this.resourceManager.registerReloadListener(this.contentpacks$blockColorLoader);
        this.contentpacks$fluidModelLoader = new FluidModelLoader();
        this.resourceManager.registerReloadListener(this.contentpacks$fluidModelLoader);
    }

    @Inject(method = "onGameLoadFinished", at = @At(value = "TAIL"))
    private void inject$onGameLoadFinished_popup(GameLoadCookie cookie, CallbackInfo ci) {
        if (ErrorLogger.LOAD.count() > 0) {
            SystemToast.add(gui.toastManager(), ContentPacksClient.CONTENT_LOAD_FAILURE,
                    Component.translatable("options.contentpacks.errors.title", ErrorLogger.LOAD.count()).withColor(CommonColors.SOFT_RED), Component.translatable("options.contentpacks.errors.description").withColor(CommonColors.SOFT_RED));
        }
    }
}