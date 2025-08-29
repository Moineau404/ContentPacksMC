package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionSet;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.dynamic.Codecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class InteractionLoader<T> implements ResourceLoader {
    private final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/InteractionLoader");
    private final String DIR = "interactions/";
    private final ResourceFinder finder;
    private final Codec<Map<InteractionType<T, ?>, Interaction<T>>> codec;
    private final Function<Identifier, T> resolver;

    private InteractionLoader(String directoryName, Codec<InteractionType<T, ?>> codec, Function<Identifier, T> resolver) {
        this.finder = ResourceFinder.json(DIR + directoryName);
        this.codec = Codec.dispatchedMap(codec, type -> type.codec().codec());
        this.resolver = resolver;
    }

    public static <T> InteractionLoader<T> create(String directoryName, Codec<InteractionType<T, ?>> codec, Function<Identifier, T> resolver) {
        return new InteractionLoader<>(directoryName, codec, resolver);
    }

    public static <T> InteractionLoader<T> create(String directoryName, Codecs.IdMapper<Identifier, InteractionType<T, ?>> idMapper, Function<Identifier, T> resolver) {
        return create(directoryName, idMapper.getCodec(Identifier.CODEC), resolver);
    }

    public static <T> InteractionLoader<T> create(Registry<T> registry, Codec<InteractionType<T, ?>> codec) {
        return create(registry.getKey().getValue().getPath(), codec, registry::get);
    }

    public static <T> InteractionLoader<T> create(Registry<T> registry, Codecs.IdMapper<Identifier, InteractionType<T, ?>> idMapper) {
        return create(registry, idMapper.getCodec(Identifier.CODEC));
    }

    public static <T> InteractionLoader<TagKey<T>> create(RegistryKey<? extends Registry<T>> registryRef, Codec<InteractionType<TagKey<T>, ?>> codec) {
        return create("tag/" + registryRef.getValue().getPath(), codec, id -> TagKey.of(registryRef, id));
    }

    public static <T> InteractionLoader<TagKey<T>> create(RegistryKey<? extends Registry<T>> registryRef, Codecs.IdMapper<Identifier, InteractionType<TagKey<T>, ?>> idMapper) {
        return create(registryRef, idMapper);
    }

    @Override
    public void load(ResourceManager resourceManager) {
        Map<Identifier, List<Resource>> resourceMap = finder.findAllResources(resourceManager);

        for (Map.Entry<Identifier, List<Resource>> entry : resourceMap.entrySet()) {
            Identifier id = finder.toResourceId(entry.getKey());

            try {
                T target = this.resolver.apply(id);
                if (target == null) {
                    continue;
                }

                InteractionSet<T> entries = new InteractionSet<>();
                for (Resource resource : entry.getValue()) {
                    try {
                        Reader reader = resource.getReader();

                        JsonElement jsonElement = StrictJsonParser.parse(reader);

                        DataResult<Map<InteractionType<T, ?>, Interaction<T>>> result = this.codec.parse(JsonOps.INSTANCE, jsonElement);
                        Map<InteractionType<T, ?>, Interaction<T>> value = result.getPartialOrThrow(JsonParseException::new);

                        entries.addAll(value.values());
                        result.ifError(error -> ErrorTracker.print(entry.getKey(), resource, error.message()));

                        try {
                            reader.close();
                        } catch (Throwable ignored) {}
                    } catch (Exception e) {
                        ErrorTracker.print(entry.getKey(), resource, e.getMessage());
                    }
                }

                entries.forEach(interaction -> interaction.register(target));
            } catch (Exception e) {
                ErrorTracker.print(entry.getKey(), entry.getValue(), e.getMessage());
            }
        }
    }
}
