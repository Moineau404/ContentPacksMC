package mod.moineau.contentpacks.client.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.render.block.ContentBlockColors;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSource;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSourceTypes;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Block;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

// TODO Clean/update
public class BlockColorLoader implements PreparableReloadListener {
    private static final FileToIdConverter FINDER = FileToIdConverter.json("blockstates");
    private static final Codec<Optional<List<BlockTintSource>>> CODEC = BlockTintSourceTypes.CODEC.listOf().optionalFieldOf("tints").codec();

    private final ContentBlockColors blockColors;

    public BlockColorLoader(BlockColors blockColors) {
        this.blockColors = (ContentBlockColors) blockColors;
    }

    @Override
    public CompletableFuture<Void> reload(
            SharedState currentReload,
            Executor taskExecutor,
            PreparationBarrier preparationBarrier,
            Executor reloadExecutor
    ) {
        ResourceManager manager = currentReload.resourceManager();
        return CompletableFuture.supplyAsync(
                () -> FINDER.listMatchingResources(manager),
                taskExecutor
        ).thenCompose(resourceMap -> {
            List<CompletableFuture<Pair<Block, List<BlockTintSource>>>> list = new ArrayList<>(resourceMap.size());

            for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Identifier id = FINDER.fileToId(entry.getKey());
                    Block block = BuiltInRegistries.BLOCK.getValue(id);

                    if (block == null) {
                        ContentPacksClient.LOGGER.debug("Discovered unknown block color {}, ignoring", id);
                        return null;
                    } else {
                        Resource resource = entry.getValue();

                        try (Reader reader = resource.openAsReader()) {
                            JsonElement jsonElement = StrictJsonParser.parse(reader);
                            Optional<List<BlockTintSource>> tintsResult = CODEC.parse(JsonOps.INSTANCE, jsonElement).getPartialOrThrow(JsonParseException::new);

                            return tintsResult.map(tints -> new Pair<>(block, tints)).orElse(null);
                        } catch (Exception e) {
                            ContentPacksClient.LOGGER.error("Failed to load block tints for block {} from pack {}", id, resource.sourcePackId(), e);
                            return null;
                        }
                    }
                }, taskExecutor));
            }

            return Util.sequence(list).thenAcceptAsync(entries -> {
                blockColors.contentpacks$clearSourceOverrides();

                for (Pair<Block, List<BlockTintSource>> entry : entries) {
                    if (entry != null) {
                        blockColors.contentpacks$addSourceOverrides(entry.getFirst(), new ArrayList<>(entry.getSecond()));
                    }
                }
            });
        }).thenCompose(preparationBarrier::wait);
    }
}