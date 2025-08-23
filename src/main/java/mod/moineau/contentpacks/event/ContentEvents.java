package mod.moineau.contentpacks.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ContentEvents {
    public static final Event<ContentLoaded> CONTENT_LOADED = EventFactory.createArrayBacked(ContentEvents.ContentLoaded.class, callbacks -> () -> {
        for (ContentEvents.ContentLoaded callback : callbacks) {
            callback.onContentLoaded();
        }
    });

    public interface ContentLoaded {
        void onContentLoaded();
    }
}
