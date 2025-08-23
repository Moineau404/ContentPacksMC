package mod.moineau.contentpacks.resource;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public abstract class ContentLoader<T> implements ResourceLoader {
    private ImmutableList<T> entries;
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

    @ApiStatus.OverrideOnly
    protected abstract List<T> contents(ResourceManager resourceManager);

    @Unmodifiable
    public final List<T> getEntries() {
        return this.entries;
    }

    public final void registerListener(ContentLoaded<T> listener) {
        this.event.register(listener);
    }

    @FunctionalInterface
    public interface ContentLoaded<T> {
        void onContentLoaded(ImmutableList<T> entries);
    }
}
