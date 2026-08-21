package mod.moineau.contentpacks.server;

import mod.moineau.contentpacks.ContentPacks;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.packs.repository.Pack;

import java.util.ArrayList;
import java.util.List;

/**
 * On server, all content packs present in the directory are loaded, there is no selection.
 */
public class ContentPacksServer implements DedicatedServerModInitializer {
    private static ContentPacksServer INSTANCE;

    public ContentPacksServer() {
        INSTANCE = this;
    }

    @Override
    public void onInitializeServer() {
        List<Pack> packs = new ArrayList<>();
        ContentPacks.getRepositorySource().loadPacks(packs::add);
        ContentPacks.getInstance().loadPacks(packs);
    }

    public static ContentPacksServer getInstance() {
        return INSTANCE;
    }
}