package mod.moineau.contentpacks.resource;

import mod.moineau.contentpacks.ContentPacks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Consumer;

public final class RegistryManager implements ResourceLoader {
    private static final Logger LOGGER = ContentPacks.LOGGER;
    private final TreeMap<Integer, List<RegistryLoader<?>>> loaders = new TreeMap<>(Comparator.reverseOrder());
    private final Map<ResourceKey<? extends Registry<?>>, List<Callback<?>>> callbacks = new HashMap<>();

    public RegistryManager() {}

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void load(ResourceManager resourceManager, Consumer<String> errorHandler) {
        List<Runnable> callbackRuns = new LinkedList<>();
        this.loaders.values().forEach(list -> list.forEach(loader -> {
            Callback callback = Callback.of((List) this.callbacks.getOrDefault(loader.getRegistryName(), List.of()));
            loader.load(resourceManager, errorHandler).forEach(finalizer -> callbackRuns.add(finalizer.apply(callback)));
        }));
        callbackRuns.forEach(Runnable::run);
    }

    public RegistryManager register(RegistryLoader<?> loader, int priority) {
        this.loaders.computeIfAbsent(priority, _ -> new LinkedList<>()).add(loader);
        return this;
    }

    public RegistryManager register(RegistryLoader<?> loader) {
        return this.register(loader, 0);
    }

    public <T> void subscribe(ResourceKey<? extends Registry<T>> registryName, Callback<T> callback) {
        this.callbacks.computeIfAbsent(registryName, _ -> new LinkedList<>()).add(callback);
    }

    public <T> void subscribe(ResourceKey<? extends Registry<T>> registryName, Consumer<T> consumer) {
        this.callbacks.computeIfAbsent(registryName, _ -> new LinkedList<>()).add(Callback.of(consumer));
    }

    public <T> void subscribe(ResourceKey<? extends Registry<T>> registryName, List<Callback<T>> callbacks) {
        this.callbacks.computeIfAbsent(registryName, _ -> new LinkedList<>()).addAll(callbacks);
    }

    public List<RegistryOutput<?>> getOutputs() {
        return this.loaders.values().stream().flatMap(List::stream).<RegistryOutput<?>>map(RegistryLoader::getOutput).toList();
    }

    public interface Callback<T> {
        void apply(ResourceKey<T> resourceKey, T value);

        static <T> Callback<T> of(Consumer<T> consumer) {
            return (_, value) -> consumer.accept(value);
        }

        static <T> Callback<T> of(List<Callback<T>> callbacks) {
            return (id, value) -> callbacks.forEach(callback -> callback.apply(id, value));
        }
    }
}