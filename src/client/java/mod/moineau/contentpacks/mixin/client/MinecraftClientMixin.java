package mod.moineau.contentpacks.mixin.client;

import mod.moineau.contentpacks.resource.BlockColorLoader;
import mod.moineau.contentpacks.resource.BlockRenderLayerLoader;
import mod.moineau.contentpacks.resource.ColorMapLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Unique
    private ColorMapLoader contentpacks$colorMapLoader;

    @Unique
    private BlockColorLoader contentpacks$blockColorLoader;

    @Unique
    private BlockRenderLayerLoader contentpacks$blockRenderLayerLoader;

    @Shadow
    @Final
    private ReloadableResourceManagerImpl resourceManager;

    @Shadow
    @Final
    private BlockColors blockColors;

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;blockColors:Lnet/minecraft/client/color/block/BlockColors;", shift = At.Shift.AFTER))
    private void inject$init(RunArgs args, CallbackInfo info) {
        this.contentpacks$colorMapLoader = new ColorMapLoader();
        this.resourceManager.registerReloader(this.contentpacks$colorMapLoader);
        this.contentpacks$blockColorLoader = new BlockColorLoader(this.blockColors);
        this.resourceManager.registerReloader(this.contentpacks$blockColorLoader);
        this.contentpacks$blockRenderLayerLoader = new BlockRenderLayerLoader();
        this.resourceManager.registerReloader(this.contentpacks$blockRenderLayerLoader);
    }
}