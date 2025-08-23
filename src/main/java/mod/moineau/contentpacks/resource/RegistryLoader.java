package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.api.util.CodecUtil;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.StrictJsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.*;

public class RegistryLoader<T> extends ContentLoader<RegistryEntry<T>> {
    private final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/RegistryLoader");
    private final ResourceFinder finder;
    private final Registry<T> registry;
    private final Codec<T> codec;
    private final boolean injectId;

    protected RegistryLoader(Registry<T> registry, Codec<T> codec, boolean injectId) {
        this.finder = ResourceFinder.json(registry.getKey());
        this.registry = registry;
        this.codec = codec;
        this.injectId = injectId;
    }

    protected RegistryLoader(Registry<T> registry, Codec<T> codec) {
        this(registry, codec, false);
    }

    @Deprecated
    protected RegistryLoader(Registry<T> registry, Codec<T> codec, boolean injectId, String directoryName) {
        this.finder = ResourceFinder.json(directoryName);
        this.registry = registry;
        this.codec = codec;
        this.injectId = injectId;
    }

    @Deprecated
    protected RegistryLoader(Registry<T> registry, Codec<T> codec, String directoryName) {
        this(registry, codec, false, directoryName);
    }

    public final LazyRegistryEntryReference<T> get(RegistryKey<T> registryKey) {
        return new LazyRegistryEntryReference<>(registryKey);
    }

    public final LazyRegistryEntryReference<T> get(Identifier id) {
        return get(RegistryKey.of(this.registry.getKey(), id));
    }

    @Override
    protected List<RegistryEntry<T>> contents(ResourceManager resourceManager) {
        Map<Identifier, Resource> resourceMap = finder.findResources(resourceManager);
        List<RegistryEntry<T>> entries = new LinkedList<>();

        for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
            Identifier id = finder.toResourceId(entry.getKey());
            if (registry.containsId(id)) {
                continue;
            }

            Resource resource = entry.getValue();
            try {
                Reader reader = resource.getReader();

                JsonElement jsonElement = StrictJsonParser.parse(reader);
                if (this.injectId) {
                    jsonElement = CodecUtil.jsonInjectId(jsonElement, id);
                }

                DataResult<T> result = this.codec.parse(JsonOps.INSTANCE, jsonElement);
                T value = result.getPartialOrThrow(JsonParseException::new);

                entries.add(Registry.registerReference(registry, id, value));
                result.ifError(error -> LOGGER.error("Partially loaded entry {} for registry {} from pack {}: {}",
                        id, this.registry.getKey().getValue().toString(), resource.getPackId(), error.message()));

                try {
                    reader.close();
                } catch (Throwable ignored) {}
            } catch (Exception e) {
                LOGGER.error("Failed to load entry {} for registry {} from pack {}", id, this.registry.getKey().getValue().toString(), resource.getPackId(), e);
            }
        }

        return entries;
    }

    public Map<Identifier, JsonElement> serialize() {
        Map<Identifier, JsonElement> jsonElements = new HashMap<>();

        for (RegistryEntry<T> entry : getEntries()) {
            Identifier id = entry.getKey().orElseThrow().getValue();
            try {
                DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, entry.value());
                JsonElement jsonElement = result.getPartialOrThrow();
                jsonElements.put(finder.toResourcePath(id), jsonElement);
                result.ifError(error -> LOGGER.warn("Partially encoded {} from registry {}: {}", id, this.registry.getKey().getValue().toString(), error.message()));
            } catch (JsonParseException | NoSuchElementException e) {
                LOGGER.warn("Failed to encode {} from registry {}", id, this.registry.getKey().getValue().toString());
            }
        }

        return jsonElements;
    }
}
