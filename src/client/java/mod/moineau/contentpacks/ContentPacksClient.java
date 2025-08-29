package mod.moineau.contentpacks;

import mod.moineau.contentpacks.options.ContentPacksOptions;
import mod.moineau.contentpacks.render.block.tint.BlockTintSourceTypes;
import mod.moineau.contentpacks.render.entity.BoatTypeRendering;
import mod.moineau.contentpacks.render.entity.SignRendering;
import mod.moineau.contentpacks.resource.ClientContentManager;
import mod.moineau.contentpacks.resource.ContentManager;
import mod.moineau.contentpacks.resource.ContentOutput;
import mod.moineau.contentpacks.resource.ErrorTracker;
import mod.moineau.contentpacks.world.biome.ColorResolvers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resource.ResourcePackManager;

public class ContentPacksClient implements ClientModInitializer {
	public static final ResourcePackManager PACK_MANAGER = new ResourcePackManager(ContentPacks.PACK_PROVIDER);
	public static final ContentPacksOptions OPTIONS = ContentPacksOptions.read();

	@Override
	public void onInitializeClient() {
		bootstrap();
		PACK_MANAGER.scanPacks();
		OPTIONS.updatePackManager(PACK_MANAGER);
		ContentManager.registerLoader(ClientContentManager::load);
		ContentPacks.load(PACK_MANAGER.createResourcePacks());
		if (OPTIONS.isOutputEnabled()) {
			ContentOutput.output();
		}
		ErrorTracker.write();
	}

	public static void bootstrap() {
		ColorResolvers.bootstrap();
		BlockTintSourceTypes.bootstrap();
		BoatTypeRendering.bootstrap();
		SignRendering.bootstrap();
	}
}