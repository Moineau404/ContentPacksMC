package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.api.util.CodecUtil;
import mod.moineau.api.util.JsonUtil;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.metadata.MetaProperties;
import mod.moineau.contentpacks.metadata.MetaProperty;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static mod.moineau.contentpacks.resource.RegistryManager.Callback;

public class RegistryLoader<T> {
    private static final Logger LOGGER = ContentPacks.LOGGER;
    private final FileToIdConverter finder;
    private final Registry<T> registry;
    private final Codec<T> codec;
    private final Set<Flag> flags;

    @Deprecated
    public RegistryLoader(Registry<T> registry, Codec<T> codec, Flag... flags) {
        this.finder = FileToIdConverter.registry(registry.key());
        this.registry = registry;
        this.codec = codec;
        this.flags = Set.of(flags);
    }

    public RegistryLoader(Registry<T> registry, Codec<T> codec) {
        this(registry, codec, Flag.values());
    }

    public List<Finalizer> load(ResourceManager resourceManager, Consumer<String> errorHandler) {
        Map<Identifier, Resource> resourceMap = finder.listMatchingResources(resourceManager);

        List<Result> results = new LinkedList<>();
        resourceMap.forEach((location, resource) -> {
            Identifier id = this.finder.fileToId(location);
            ResourceKey<T> resourceKey = ResourceKey.create(this.registry.key(), id);
            String pack = resource.sourcePackId();
            if (!registry.containsKey(id)) {
                try {
                    DataResult<JsonElement> jsonResult = JsonUtil.readResult(resource.openAsReader());
                    jsonResult.ifSuccess(jsonElement -> {
                        if (this.flags.contains(Flag.INJECT_ID)) {
                            CodecUtil.jsonInjectId(jsonElement, id);
                        }
                        if (this.flags.contains(Flag.INJECT_LOCATION)) {
                            CodecUtil.jsonInjectLocation(jsonElement, location);
                        }
                    });

                    DataResult<T> result = jsonResult.flatMap(jsonElement -> this.codec.parse(JsonOps.INSTANCE, jsonElement));

                    DataResult<ResourceMetadata> metadataResult;
                    try {
                        metadataResult = DataResult.success(resource.metadata());
                    } catch (Exception e) {
                        metadataResult = DataResult.error(e::getMessage);
                    }

                    results.add(new Result(resourceKey, location, pack, result, metadataResult));
                } catch (IOException e) {
                    results.add(new Result(resourceKey, location, pack, DataResult.error(e::getMessage), DataResult.success(ResourceMetadata.EMPTY)));
                }
            } else {
                results.add(new Result(resourceKey, location, pack, DataResult.error(() -> String.format("Cannot override existing entry %s in registry %s", id, registry.key().identifier())), DataResult.success(ResourceMetadata.EMPTY)));
            }
        });

        List<Finalizer> finalizers = new LinkedList<>();
        results.forEach(result -> {
            result.ifSuccess((value, metadata) -> finalizers.add(new Finalizer(result.resourceKey, () -> {
                Registry.register(this.registry, result.resourceKey.identifier(), value);
                return value;
            }, metadata)));
            result.ifError(errorHandler);
        });

        return finalizers;
    }

    public ResourceKey<? extends Registry<T>> getRegistryName() {
        return registry.key();
    }

    public RegistryOutput<T> getOutput() {
        return new RegistryOutput<>(this.registry, this.codec, this.finder::idToFile);
    }

    private final class Result {
        private final ResourceKey<T> resourceKey;
        private final Identifier location;
        private final String pack;
        private final DataResult<T> result;
        private final DataResult<ResourceMetadata> metadataResult;

        private Result(ResourceKey<T> resourceKey, Identifier location, String pack, DataResult<T> result, DataResult<ResourceMetadata> metadataResult) {
            this.resourceKey = resourceKey;
            this.location = location;
            this.pack = pack;
            this.result = result;
            this.metadataResult = metadataResult;
        }

        private void ifSuccess(BiConsumer<T, ResourceMetadata> ifSuccess) {
            this.result.ifSuccess(value -> {
                ifSuccess.accept(value, this.metadataResult.result().orElse(ResourceMetadata.EMPTY));
            });
        }

        private void ifError(Consumer<String> ifError) {
            this.result.ifError(e -> ifError.accept(String.format("(%s) [%s] %s", this.pack, this.location, e.message())));
            this.metadataResult.ifError(e -> ifError.accept(String.format("(%s) [%s (metadata)] %s", this.pack, this.location, e.message())));
        }
    }

    public final class Finalizer {
        private final ResourceKey<T> resourceKey;
        private final Supplier<T> applier;
        private final ResourceMetadata metadata;

        public Finalizer(ResourceKey<T> resourceKey, Supplier<T> applier, ResourceMetadata metadata) {
            this.resourceKey = resourceKey;
            this.applier = applier;
            this.metadata = metadata;
        }

        public Runnable apply(Callback<T> callback) {
            T value = applier.get();
            return () -> {
                callback.apply(this.resourceKey, value);
                List<MetaProperty.Value<T, ?>> metaProperties = MetaProperties.get(RegistryLoader.this.registry.key(), metadata);
                metaProperties.forEach(metaPropertyValue -> metaPropertyValue.apply(value));
            };
        }
    }

    @Deprecated
    public enum Flag {
        INJECT_ID,
        INJECT_LOCATION;
    }
}