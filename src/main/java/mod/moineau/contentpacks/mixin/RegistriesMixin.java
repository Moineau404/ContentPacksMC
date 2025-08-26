package mod.moineau.contentpacks.mixin;

import mod.moineau.contentpacks.event.ContentPacksEvents;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registries.class)
public class RegistriesMixin {
    @Inject(method = "bootstrap()V", at = @At(value = "TAIL"))
    private static void inject$bootstrap(CallbackInfo ci) {
        ContentPacksEvents.REGISTRIES_LOADED.invoker().onRegistriesLoaded();
    }
}
