package mod.moineau.contentpacks.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ContentPacksEvents {
    public static final Event<ContentLoaded> CONTENT_LOADED = EventFactory.createArrayBacked(ContentLoaded.class, callbacks -> () -> {
        for (ContentLoaded callback : callbacks) {
            callback.onContentLoaded();
        }
    });
    public static final Event<RegistriesLoaded> REGISTRIES_LOADED = EventFactory.createArrayBacked(RegistriesLoaded.class, callbacks -> () -> {
        for (RegistriesLoaded callback : callbacks) {
            callback.onRegistriesLoaded();
        }
    });

    public interface ContentLoaded {
        void onContentLoaded();
    }

    public interface RegistriesLoaded {
        void onRegistriesLoaded();
    }
}
