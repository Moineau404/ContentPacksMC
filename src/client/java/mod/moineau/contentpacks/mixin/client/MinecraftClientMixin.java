package mod.moineau.contentpacks.mixin.client;

import mod.moineau.contentpacks.ContentPacksClient;
import mod.moineau.contentpacks.options.ContentPacksOptions;
import mod.moineau.contentpacks.resource.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Unique
    private ColorMapLoader contentpacks$colorMapLoader;

    @Unique
    private BlockColorLoader contentpacks$blockColorLoader;

    @Unique
    private BlockRenderLayerLoader contentpacks$blockRenderLayerLoader;

    @Unique
    private FluidAssetLoader contentpacks$fluidAssetLoader;

    @Shadow
    @Final
    private ReloadableResourceManagerImpl resourceManager;

    @Shadow
    @Final
    private BlockColors blockColors;

    @Shadow public abstract ToastManager getToastManager();

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;blockColors:Lnet/minecraft/client/color/block/BlockColors;", shift = At.Shift.AFTER))
    private void inject$init_loaders(RunArgs args, CallbackInfo info) {
        this.contentpacks$colorMapLoader = new ColorMapLoader();
        this.resourceManager.registerReloader(this.contentpacks$colorMapLoader);
        this.contentpacks$blockColorLoader = new BlockColorLoader(this.blockColors);
        this.resourceManager.registerReloader(this.contentpacks$blockColorLoader);
        this.contentpacks$blockRenderLayerLoader = new BlockRenderLayerLoader();
        this.resourceManager.registerReloader(this.contentpacks$blockRenderLayerLoader);
        this.contentpacks$fluidAssetLoader = new FluidAssetLoader();
        this.resourceManager.registerReloader(this.contentpacks$fluidAssetLoader);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void inject$init_popup(RunArgs args, CallbackInfo info) {
        if (ContentPacksOptions.read().isDebugEnabled() && ErrorTracker.hasErrors()) {
            // TODO Find why translatable "options.contentpacks.errors.description" does not work
            this.getToastManager().add(SystemToast.create(((MinecraftClient) (Object) this), ContentPacksClient.CONTENT_LOAD_FAILURE,
                    Text.translatable("options.contentpacks.errors.title", ErrorTracker.errorCount()).withColor(Colors.LIGHT_RED), Text.translatableWithFallback("options.contentpacks.errors.description", "Check errors output file.").withColor(Colors.LIGHT_RED)));
        }
    }
}