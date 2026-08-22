package mod.moineau.contentpacks.client.resource;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.JsonUtil;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class DependentReloadListener<D, T> implements PreparableReloadListener {
    protected final FileToIdConverter finder;
    protected final Codec<T> codec;

    public DependentReloadListener(String directory, Codec<T> codec) {
        this.finder = FileToIdConverter.json(directory);
        this.codec = codec;
    }

    public DependentReloadListener(String directory, String extension, Codec<T> codec) {
        this.finder = new FileToIdConverter(directory, extension);
        this.codec = codec;
    }

    @Override
    public final CompletableFuture<Void> reload(SharedState currentReload, Executor taskExecutor, PreparationBarrier preparationBarrier, Executor reloadExecutor) {
        ResourceManager manager = currentReload.resourceManager();
        return CompletableFuture.supplyAsync(() -> this.finder.listMatchingResources(manager), taskExecutor).thenCompose(resourceMap -> {
            List<CompletableFuture<Entry<D, T>>> list = new ArrayList<>(resourceMap.size());

            resourceMap.forEach((location, resource) -> list.add(CompletableFuture.supplyAsync(() -> {
                Identifier id = finder.fileToId(location);

                D dependence = this.getDependence(id);
                if (dependence != null) {
                    try (Reader reader = resource.openAsReader()) {
//                        T object = JsonUtil.parseOrPartial(reader, this.codec, e -> this.handlePartialError(id, resource.sourcePackId(), e));
                        T object = JsonUtil.parse(reader, this.codec);
                        return new Entry<>(dependence, object, id);
                    } catch (Exception e) {
                        this.handleReadingError(id, resource.sourcePackId(), e.getMessage());
                    }
                } else {
                    this.handleNullError(id);
                }

                return null;
            }, taskExecutor)));

            return Util.sequence(list).thenAcceptAsync(entries -> {
                for (Entry<D, T> entry : entries) {
                    if (entry != null) {
                        this.loadEntry(entry.dependence(), entry.entry(), entry.id());
                    }
                }
            });
        }).thenCompose(preparationBarrier::wait);
    }

    protected abstract @Nullable D getDependence(Identifier id);

    protected abstract void loadEntry(D dependence, T object, Identifier id);

    protected void handleNullError(Identifier id) {}

    protected void handleReadingError(Identifier id, String pack, String message) {}

    protected void handlePartialError(Identifier id, String pack, String message) {}

    private record Entry<D, T>(D dependence, T entry, Identifier id) {}
}
