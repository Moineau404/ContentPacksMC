package mod.moineau.contentpacks.mixin;

import mod.moineau.contentpacks.ContentPacks;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Support content resource type in addition to client resources (assets) and server data
 */
@Mixin(PackFormat.class)
public class PackFormatMixin {
    @Inject(method = "lastPreMinorVersion", at = @At(value = "HEAD"), cancellable = true)
    private static void lastPreMinorVersion(PackType type, CallbackInfoReturnable<Integer> cir) {
        if (type == ContentPacks.PACK_TYPE) {
            cir.setReturnValue(ContentPacks.PACK_LAST_PRE_MINOR_VERSION);
        }
    }
}
