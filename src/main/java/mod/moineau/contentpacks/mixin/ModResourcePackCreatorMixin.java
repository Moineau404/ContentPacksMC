package mod.moineau.contentpacks.mixin;

import mod.moineau.contentpacks.ContentPacks;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.resource.*;
import net.minecraft.util.path.SymlinkFinder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ModResourcePackCreator.class)
public class ModResourcePackCreatorMixin {
    @Shadow
    @Final
    private ResourceType type;

    @Unique
    private static final ResourcePackProvider CONTENT_CLIENT_RESOURCES_PACK_PROVIDER = new FileResourcePackProvider(
            ContentPacks.PATH, ResourceType.CLIENT_RESOURCES, ContentPacks.PACK_SOURCE, new SymlinkFinder(path -> true));

    @Unique
    private static final ResourcePackProvider CONTENT_SERVER_DATA_PACK_PROVIDER = new FileResourcePackProvider(
            ContentPacks.PATH, ResourceType.SERVER_DATA, ContentPacks.PACK_SOURCE, new SymlinkFinder(path -> true));

    @Inject(method = "register", at = @At(value = "RETURN"))
    private void inject$init(Consumer<ResourcePackProfile> consumer, CallbackInfo ci) {
        if (this.type == ResourceType.CLIENT_RESOURCES) {
            CONTENT_CLIENT_RESOURCES_PACK_PROVIDER.register(consumer);
        }
        if (this.type == ResourceType.SERVER_DATA) {
            CONTENT_SERVER_DATA_PACK_PROVIDER.register(consumer);
        }
    }
}