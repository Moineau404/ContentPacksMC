package mod.moineau.contentpacks.server;

import mod.moineau.contentpacks.ContentPacks;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.resource.ResourcePack;

import java.util.ArrayList;
import java.util.List;

/**
 * On server, all content packs present in the directory are loaded, there is no selection.
 */
public class ContentPacksServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        List<ResourcePack> packs = new ArrayList<>();
        ContentPacks.PACK_PROVIDER.register(profile -> packs.add(profile.createResourcePack()));
        ContentPacks.load(packs);
    }
}