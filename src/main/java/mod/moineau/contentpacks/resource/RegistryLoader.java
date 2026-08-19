package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.util.ErrorLogger;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <T>
 */
public class RegistryLoader<T> extends ContentLoader<Holder<T>> {
    private static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/RegistryLoader");
    private final FileToIdConverter finder;
    private final Registry<T> registry;
    private final Codec<T> codec;
    private final boolean injectId;

    protected RegistryLoader(Registry<T> registry, Codec<T> codec, boolean injectId) {
        this.finder = FileToIdConverter.registry(registry.key());
        this.registry = registry;
        this.codec = codec;
        this.injectId = injectId;
    }

    protected RegistryLoader(Registry<T> registry, Codec<T> codec) {
        this(registry, codec, false);
    }

    @Deprecated
    protected RegistryLoader(Registry<T> registry, Codec<T> codec, boolean injectId, String directoryName) {
        this.finder = FileToIdConverter.json(directoryName);
        this.registry = registry;
        this.codec = codec;
        this.injectId = injectId;
    }

    @Deprecated
    protected RegistryLoader(Registry<T> registry, Codec<T> codec, String directoryName) {
        this(registry, codec, false, directoryName);
    }

    public final Holder<T> get(Identifier id) {
        return Holder.Reference.createStandAlone(this.registry, ResourceKey.create(this.registry.key(), id));
    }

    @Override
    protected List<Holder<T>> contents(ResourceManager resourceManager) {
        Map<Identifier, Resource> resourceMap = finder.listMatchingResources(resourceManager);
        List<Holder<T>> entries = new LinkedList<>();

        for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
            Identifier id = finder.fileToId(entry.getKey());
            if (registry.containsKey(id)) {
                ErrorLogger.LOAD.write(id, entry.getValue(), "Cannot override existing entry");
                continue;
            }

            Resource resource = entry.getValue();
            try {
                Reader reader = resource.openAsReader();

                JsonElement jsonElement = StrictJsonParser.parse(reader);
                if (this.injectId) {
                    jsonElement = CodecUtil.jsonInjectId(jsonElement, id);
                }

                DataResult<T> result = this.codec.parse(JsonOps.INSTANCE, jsonElement);
                T value = result.getPartialOrThrow(JsonParseException::new);

                entries.add(Registry.registerForHolder(registry, id, value));
                result.ifError(error -> ErrorLogger.LOAD.write(entry.getKey(), resource, error.message()));

                try {
                    reader.close();
                } catch (Throwable ignored) {}
            } catch (Exception e) {
                ErrorLogger.LOAD.write(entry.getKey(), resource, e.getMessage());
            }
        }

        return entries;
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @ApiStatus.Internal
    private Map<Identifier, JsonElement> serialize(Iterable<Holder<T>> entries) {
        Map<Identifier, JsonElement> jsonElements = new HashMap<>();

        for (Holder<T> entry : entries) {
            Identifier id = entry.unwrapKey().orElseThrow().identifier();
            try {
                DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, entry.value());
                JsonElement jsonElement = result.getPartialOrThrow();
                jsonElements.put(finder.idToFile(id), jsonElement);
                result.ifError(error -> ErrorLogger.OUTPUT.write(id, this.registry.key().identifier(), String.format("Partially encoded: %s", error.message())));
            } catch (Exception e) {
                ErrorLogger.OUTPUT.write(id, this.registry.key().identifier(), String.format("Failed to encode: %s", e.getMessage()));
            }
        }

        return jsonElements;
    }

    @ApiStatus.Internal
    @Override
    public Map<Identifier, JsonElement> serialize() {
        return serialize(this.entries);
    }

    @ApiStatus.Internal
    public Map<Identifier, JsonElement> serializeAll() {
        return serialize(this.registry.asHolderIdMap());
    }
}
