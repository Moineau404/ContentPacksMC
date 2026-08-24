package mod.moineau.contentpacks.server;

import mod.moineau.contentpacks.ContentPacks;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.packs.repository.Pack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * On server, all content packs present in the directory are loaded, there is no selection.
 */
public class ContentPacksServer implements DedicatedServerModInitializer {
    private static ContentPacksServer INSTANCE;
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Server");

    public ContentPacksServer() {
        if (INSTANCE != null) throw new IllegalStateException("ContentPacksServer already initialized!");
        INSTANCE = this;
    }

    @Override
    public void onInitializeServer() {
        List<Pack> packs = new ArrayList<>();
        ContentPacks.getInstance().getRepositorySource().loadPacks(packs::add);
        ContentPacks.getInstance().loadPacks(packs);
    }

    public static ContentPacksServer getInstance() {
        return INSTANCE;
    }
}