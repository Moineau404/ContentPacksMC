package mod.moineau.contentpacks.integration;

import mod.moineau.contentpacks.resource.ContentManager;
import mod.moineau.contentpacks.resource.ResourceLoader;

public abstract class ContentPacksAddon {
    public abstract void beforeContentLoaded();

    protected static void registerLoader(ResourceLoader loader) {
        ContentManager.registerLoader(loader);
    }
}