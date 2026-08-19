package mod.moineau.contentpacks.resource;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;

/**
 *
 * @param <T>
 */
public abstract class ContentLoader<T> implements ResourceLoader {
    protected ImmutableList<T> entries;
    private final Event<ContentLoaded<T>> event = EventFactory.createArrayBacked(ContentLoaded.class, callbacks -> (entries) -> {
        for (ContentLoaded<T> callback : callbacks) {
            callback.onContentLoaded(entries);
        }
    });

    @Override
    public final void load(ResourceManager resourceManager) {
        this.entries = ImmutableList.copyOf(this.contents(resourceManager));
        this.event.invoker().onContentLoaded(this.entries);
    }

    protected abstract List<T> contents(ResourceManager resourceManager);

    public final ImmutableList<T> getEntries() {
        return this.entries;
    }

    public final void registerListener(ContentLoaded<T> listener) {
        this.event.register(listener);
    }

    @ApiStatus.Internal
    public Map<Identifier, JsonElement> serialize() {
        return Map.of();
    }

    @FunctionalInterface
    public interface ContentLoaded<T> {
        void onContentLoaded(ImmutableList<T> entries);
    }
}
