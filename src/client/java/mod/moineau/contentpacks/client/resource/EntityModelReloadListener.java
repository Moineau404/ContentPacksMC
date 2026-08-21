package mod.moineau.contentpacks.client.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.render.entity.EntityModelTypes;
import net.fabricmc.fabric.impl.client.rendering.ModelLayerImpl;
import net.fabricmc.fabric.mixin.client.rendering.ModelLayersAccessor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class EntityModelReloadListener implements PreparableReloadListener {
    public static final String DIRECTORY = "models/entity";
    public static final FileToIdConverter FINDER = FileToIdConverter.json(DIRECTORY);
    public static final Codec<Either<LayerDefinition, Map<String, LayerDefinition>>> CODEC = Codec.either(
            EntityModelTypes.CODEC,
            Codec.unboundedMap(Codec.STRING, EntityModelTypes.CODEC)
    );

    @Override
    public CompletableFuture<Void> reload(SharedState currentReload, Executor taskExecutor, PreparationBarrier preparationBarrier, Executor reloadExecutor) {
        ResourceManager resourceManager = currentReload.resourceManager();

        return CompletableFuture.supplyAsync(
                () -> FINDER.listMatchingResources(resourceManager),
                taskExecutor
        ).thenCompose(resourceMap -> {
            List<CompletableFuture<List<Entry>>> list = new ArrayList<>(resourceMap.size());
            for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Identifier id = FINDER.fileToId(entry.getKey());
                    Resource resource = entry.getValue();

                    try (Reader reader = resource.openAsReader()) {
                        JsonElement jsonElement = StrictJsonParser.parse(reader);

                        DataResult<Either<LayerDefinition, Map<String, LayerDefinition>>> result = CODEC.parse(JsonOps.INSTANCE, jsonElement);
                        Either<LayerDefinition, Map<String, LayerDefinition>> value = result.getPartialOrThrow(JsonParseException::new);

                        result.ifError(error -> ContentPacksClient.LOGGER.error("Partially loaded entity model {} from pack {}: {}", id, resource.sourcePackId(), error.message()));
                        return value.map(
                                model -> List.of(new Entry(new ModelLayerLocation(id, "main"), model)),
                                models -> models.entrySet().stream().map(kv -> new Entry(new ModelLayerLocation(id, kv.getKey()), kv.getValue())).toList()
                        );
                    } catch (Exception e) {
                        ContentPacksClient.LOGGER.error("Failed to load entity model {} from pack {}: {}", id, resource.sourcePackId(), e);
                        return null;
                    }
                }, taskExecutor));
            }

            return Util.sequence(list).thenAcceptAsync(lists -> lists.forEach(entries -> {
                if (entries != null) {
                    entries.forEach(EntityModelReloadListener::register);
                }
            }));
        }).thenCompose(preparationBarrier::wait);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void register(Entry entry) {
        ModelLayerLocation modelId = entry.modelId();
        LayerDefinition model = entry.model();
        ModelLayerImpl.PROVIDERS.put(modelId, () -> model);
        ModelLayersAccessor.getLayers().add(modelId);
    }

    private record Entry(ModelLayerLocation modelId, LayerDefinition model) {}
}
