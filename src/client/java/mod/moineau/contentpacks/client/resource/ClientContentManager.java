package mod.moineau.contentpacks.client.resource;

import mod.moineau.contentpacks.resource.ResourceLoader;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.LinkedList;
import java.util.List;

public final class ClientContentManager {
    private static final List<ResourceLoader> LOADERS = new LinkedList<>();
    public static final ColorResolverManager COLOR_RESOLVERS = new ColorResolverManager();

    public static void registerLoader(ResourceLoader loader) {
        LOADERS.add(loader);
    }

    public static void load(ResourceManager resourceManager) {
        LOADERS.forEach(loader -> loader.load(resourceManager));
    }

    static {
        registerLoader(COLOR_RESOLVERS);
    }
}
