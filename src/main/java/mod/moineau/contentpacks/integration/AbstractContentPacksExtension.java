package mod.moineau.contentpacks.integration;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.resource.RegistryManager;
import mod.moineau.contentpacks.resource.ResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractContentPacksExtension {
    public abstract void onInitialize();

    public abstract void onContentLoaded();

    protected static <T> void subscribeRegistry(ResourceKey<? extends Registry<T>> registry, RegistryManager.Callback<T> callback) {
        ContentPacks.getInstance().getRegistryManager().subscribe(registry, callback);
    }

    protected static <T> void subscribeRegistry(ResourceKey<? extends Registry<T>> registry, Consumer<T> consumer) {
        ContentPacks.getInstance().getRegistryManager().subscribe(registry, consumer);
    }

    protected static <T> void subscribeRegistry(ResourceKey<? extends Registry<T>> registry, List<RegistryManager.Callback<T>> callbacks) {
        ContentPacks.getInstance().getRegistryManager().subscribe(registry, callbacks);
    }

    protected static void registerLoader(ResourceLoader loader) {
        ContentPacks.getInstance().registerLoader(loader);
    }

    protected static void registerLoader(ResourceLoader loader, int priority) {
        ContentPacks.getInstance().registerLoader(loader, priority);
    }
}