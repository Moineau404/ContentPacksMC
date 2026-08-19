package mod.moineau.contentpacks.client;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.options.ContentPacksOptions;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSourceTypes;
import mod.moineau.contentpacks.client.render.entity.BoatTypeRendering;
import mod.moineau.contentpacks.client.render.entity.ChestRendering;
import mod.moineau.contentpacks.client.resource.ClientContentManager;
import mod.moineau.contentpacks.client.world.biome.ColorResolvers;
import mod.moineau.contentpacks.resource.ContentManager;
import mod.moineau.contentpacks.resource.ContentOutput;
import mod.moineau.contentpacks.util.ErrorLogger;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.server.packs.repository.PackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main client class of Content Packs mod.
 */
public class ContentPacksClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Client");
	public static final PackRepository PACK_REPOSITORY = new PackRepository(ContentPacks.REPOSITORY_SOURCE);
	public static final ContentPacksOptions OPTIONS = ContentPacksOptions.read();
	public static final SystemToast.SystemToastId CONTENT_LOAD_FAILURE = new SystemToast.SystemToastId(10000L);
	public static final SystemToast.SystemToastId CONTENT_CHANGED = new SystemToast.SystemToastId();

	@Override
	public void onInitializeClient() {
		PACK_REPOSITORY.reload();
		OPTIONS.updatePackManager(PACK_REPOSITORY);
		ContentManager.registerLoader(ClientContentManager::load);
		ContentPacks.loadRepository(PACK_REPOSITORY);

		ErrorLogger.LOAD.flush();
		if (OPTIONS.isOutputEnabled()) {
			ContentOutput.output();
			ErrorLogger.OUTPUT.flush();
		}

		ColorResolvers.bootStrap();
		BlockTintSourceTypes.bootStrap();
		BoatTypeRendering.bootStrap();
		ChestRendering.bootStrap();
	}
}