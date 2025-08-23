package mod.moineau.contentpacks.resource;

import net.minecraft.resource.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public final class ClientContentManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Client");
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
