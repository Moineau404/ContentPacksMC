package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionSet;
import mod.moineau.contentpacks.interaction.InteractionType;
import mod.moineau.contentpacks.util.ErrorLogger;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StrictJsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class InteractionLoader<T> implements ResourceLoader {
    private final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/InteractionLoader");
    private final String DIR = "interaction/";
    private final FileToIdConverter finder;
    private final Codec<Map<InteractionType<T, ?>, Interaction<T>>> codec;
    private final Function<Identifier, T> resolver;

    private InteractionLoader(String directoryName, Codec<InteractionType<T, ?>> codec, Function<Identifier, T> resolver) {
        this.finder = FileToIdConverter.json(DIR + directoryName);
        this.codec = Codec.dispatchedMap(codec, type -> type.codec().codec());
        this.resolver = resolver;
    }

    public static <T> InteractionLoader<T> create(String directoryName, Codec<InteractionType<T, ?>> codec, Function<Identifier, T> resolver) {
        return new InteractionLoader<>(directoryName, codec, resolver);
    }

    public static <T> InteractionLoader<T> create(String directoryName, ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<T, ?>> idMapper, Function<Identifier, T> resolver) {
        return create(directoryName, idMapper.codec(Identifier.CODEC), resolver);
    }

    public static <T> InteractionLoader<T> create(Registry<T> registry, Codec<InteractionType<T, ?>> codec) {
        return create(registry.key().identifier().getPath(), codec, registry::getValue);
    }

    public static <T> InteractionLoader<T> create(Registry<T> registry, ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<T, ?>> idMapper) {
        return create(registry, idMapper.codec(Identifier.CODEC));
    }

    public static <T> InteractionLoader<TagKey<T>> create(ResourceKey<? extends Registry<T>> registryRef, Codec<InteractionType<TagKey<T>, ?>> codec) {
        return create("tag/" + registryRef.identifier().getPath(), codec, id -> TagKey.create(registryRef, id));
    }

    public static <T> InteractionLoader<TagKey<T>> create(ResourceKey<? extends Registry<T>> registryRef, ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<TagKey<T>, ?>> idMapper) {
        return create(registryRef, idMapper.codec(Identifier.CODEC));
    }

    @Override
    public void load(ResourceManager resourceManager) {
        Map<Identifier, List<Resource>> resourceMap = finder.listMatchingResourceStacks(resourceManager);

        for (Map.Entry<Identifier, List<Resource>> entry : resourceMap.entrySet()) {
            Identifier id = finder.fileToId(entry.getKey());

            try {
                T target = this.resolver.apply(id);
                if (target == null) {
                    continue;
                }

                InteractionSet<T> entries = new InteractionSet<>();
                for (Resource resource : entry.getValue()) {
                    try {
                        Reader reader = resource.openAsReader();

                        JsonElement jsonElement = StrictJsonParser.parse(reader);

                        DataResult<Map<InteractionType<T, ?>, Interaction<T>>> result = this.codec.parse(JsonOps.INSTANCE, jsonElement);
                        Map<InteractionType<T, ?>, Interaction<T>> value = result.getPartialOrThrow(JsonParseException::new);

                        entries.addAll(value.values());
                        result.ifError(error -> ErrorLogger.LOAD.write(entry.getKey(), resource, error.message()));

                        try {
                            reader.close();
                        } catch (Throwable ignored) {}
                    } catch (Exception e) {
                        ErrorLogger.LOAD.write(entry.getKey(), resource, e.getMessage());
                    }
                }

                entries.forEach(interaction -> interaction.register(target));
            } catch (Exception e) {
                ErrorLogger.LOAD.write(entry.getKey(), entry.getValue(), e.getMessage());
            }
        }
    }
}
