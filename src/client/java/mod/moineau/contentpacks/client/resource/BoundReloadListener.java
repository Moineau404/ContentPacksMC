package mod.moineau.contentpacks.client.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class BoundReloadListener<B, T> implements PreparableReloadListener {
    protected final FileToIdConverter finder;
    protected final Codec<T> codec;

    public BoundReloadListener(String directory, Codec<T> codec) {
        this.finder = FileToIdConverter.json(directory);
        this.codec = codec;
    }

    @Override
    public final CompletableFuture<Void> reload(SharedState currentReload, Executor taskExecutor, PreparationBarrier preparationBarrier, Executor reloadExecutor) {
        ResourceManager manager = currentReload.resourceManager();
        return CompletableFuture.supplyAsync(
                () -> this.finder.listMatchingResources(manager),
                taskExecutor
        ).thenCompose(resourceMap -> {
            List<CompletableFuture<Entry<B, T>>> list = new ArrayList<>(resourceMap.size());
            for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Identifier id = finder.fileToId(entry.getKey());
                    B bound = this.getBound(id);

                    if (bound != null) {
                        Resource resource = entry.getValue();

                        try (Reader reader = resource.openAsReader()) {
                            JsonElement jsonElement = StrictJsonParser.parse(reader);
                            T object = this.codec.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(JsonParseException::new);

                            return new Entry<>(bound, object, id);
                        } catch (Exception e) {
                            readingErrorProvider(id, resource.sourcePackId(), e.getMessage());
                            return null;
                        }
                    } else {
                        nullErrorProvider(id);
                        return null;
                    }
                }, taskExecutor));
            }

            return Util.sequence(list).thenAcceptAsync(entries -> {
                for (Entry<B, T> entry : entries) {
                    if (entry != null) {
                        this.loadEntry(entry.bound(), entry.entry(), entry.id());
                    }
                }
            });
        }).thenCompose(preparationBarrier::wait);
    }

    protected abstract @Nullable B getBound(Identifier id);

    protected abstract void loadEntry(B bound, T object, Identifier id);

    protected void nullErrorProvider(Identifier id) {}

    protected void readingErrorProvider(Identifier id, String pack, String message) {}

    private record Entry<B, T>(B bound, T entry, Identifier id) {}
}
