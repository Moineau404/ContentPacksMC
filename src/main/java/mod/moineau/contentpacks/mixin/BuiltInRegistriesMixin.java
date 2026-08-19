package mod.moineau.contentpacks.mixin;

import mod.moineau.contentpacks.event.ContentPacksEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "bootStrap()V", at = @At(value = "TAIL"))
    private static void inject$bootStrap(CallbackInfo ci) {
        ContentPacksEvents.REGISTRIES_LOADED.invoker().onRegistriesLoaded();
    }
}
