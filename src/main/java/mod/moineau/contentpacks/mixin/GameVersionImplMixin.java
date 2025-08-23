package mod.moineau.contentpacks.mixin;

import mod.moineau.contentpacks.ContentPacks;
import net.minecraft.GameVersion;
import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Support content resource type in addition to client resources (assets) and server data
 */
@Mixin(GameVersion.Impl.class)
public class GameVersionImplMixin {
    @Inject(method = "packVersion", at = @At(value = "HEAD"), cancellable = true)
    public void packVersion(ResourceType type, CallbackInfoReturnable<Integer> cir) {
        if (type == ContentPacks.RESOURCE_TYPE) {
            cir.setReturnValue(ContentPacks.PACK_VERSION);
        }
    }
}
