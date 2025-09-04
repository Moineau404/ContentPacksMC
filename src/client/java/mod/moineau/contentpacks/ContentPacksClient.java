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
import net.minecraft.client.toast.SystemToast;
import net.minecraft.resource.ResourcePackManager;

public class ContentPacksClient implements ClientModInitializer {
	public static final ResourcePackManager PACK_MANAGER = new ResourcePackManager(ContentPacks.PACK_PROVIDER);
	public static final ContentPacksOptions OPTIONS = ContentPacksOptions.read();
	public static final SystemToast.Type CONTENT_LOAD_FAILURE = new SystemToast.Type(10000L);
	public static final SystemToast.Type CONTENT_CHANGED = new SystemToast.Type();

	@Override
	public void onInitializeClient() {
		ColorResolvers.bootstrap();
		BlockTintSourceTypes.bootstrap();
		BoatTypeRendering.bootstrap();
		SignRendering.bootstrap();

		PACK_MANAGER.scanPacks();
		OPTIONS.updatePackManager(PACK_MANAGER);
		ContentManager.registerLoader(ClientContentManager::load);
		ContentPacks.load(PACK_MANAGER.createResourcePacks());

		if (OPTIONS.isDebugEnabled()) {
			ErrorTracker.write();
		}
		if (OPTIONS.isOutputEnabled()) {
			ContentOutput.output();
		}
	}
}