package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.util.ErrorLogger;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.*;

/**
 *
 * @param <T>
 */
public class RegistryLoader<T> extends ContentLoader<Holder<T>> {
    private static final Logger LOGGER = ContentPacks.LOGGER;
    private static final Set<RegistryLoader<?>> LOADERS = new HashSet<>();
    private final FileToIdConverter finder;
    private final Registry<T> registry;
    private final Codec<T> codec;
    private final boolean injectId;

    protected RegistryLoader(Registry<T> registry, Codec<T> codec, boolean injectId) {
        LOADERS.add(this);
        this.finder = FileToIdConverter.registry(registry.key());
        this.registry = registry;
        this.codec = codec;
        this.injectId = injectId;
    }

    protected RegistryLoader(Registry<T> registry, Codec<T> codec) {
        this(registry, codec, false);
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

    public RegistryOutput<T> getOutput() {
        return new RegistryOutput<>(this.registry, this.codec, this.finder::idToFile);
    }

    public static List<RegistryLoader<?>> getLoaders() {
        return List.copyOf(LOADERS);
    }
}
