package mod.moineau.contentpacks.client;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.options.ContentPacksOptions;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSourceTypes;
import mod.moineau.contentpacks.api.client.render.ChestRendering;
import mod.moineau.contentpacks.client.render.entity.EntityModelTypes;
import mod.moineau.contentpacks.client.render.entity.EntityRendererTypes;
import mod.moineau.contentpacks.client.resource.*;
import mod.moineau.contentpacks.client.world.biome.ColorResolvers;
import mod.moineau.contentpacks.resource.ContentManager;
import mod.moineau.contentpacks.util.ErrorLogger;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO : Make this class an instance instead of every fields being static
/**
 * Main client class of Content Packs mod.
 */
public final class ContentPacksClient implements ClientModInitializer {
	private static ContentPacksClient INSTANCE;
	public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Client");
	public static final SystemToast.SystemToastId TOAST_LOAD_FAILURE = new SystemToast.SystemToastId(10000L);
	public static final SystemToast.SystemToastId TOAST_CHANGED = new SystemToast.SystemToastId();
	public static final SystemToast.SystemToastId TOAST_OUTPUT = new SystemToast.SystemToastId();
	private PackRepository packRepository;
	private ContentPacksOptions options;
	private ColorMapReloadListener colorMapReloadListener;
	private BlockColorReloadListener blockColorReloadListener;
	private FluidModelReloadListener fluidModelReloadListener;
	private EntityModelReloadListener entityModelReloadListener;
	private EntityAssetReloadListener entityAssetReloadListener;

	public ContentPacksClient() {
		INSTANCE = this;
	}

	@Override
	public void onInitializeClient() {
		ColorResolvers.bootStrap();
		BlockTintSourceTypes.bootStrap();
		EntityModelTypes.bootStrap();
		EntityRendererTypes.bootStrap();

		packRepository = new PackRepository(ContentPacks.getRepositorySource());
		packRepository.reload();
		options = ContentPacksOptions.read();
		options.updateRepository(packRepository);

		ContentManager.registerLoader(ClientContentManager::load);
		ContentPacks.getInstance().loadRepository(packRepository);
		ErrorLogger.LOAD.flush();

		ChestRendering.bootStrap();
	}

	public void registerReloadListeners(ReloadableResourceManager resourceManager, BlockColors blockColors) {
		colorMapReloadListener = new ColorMapReloadListener();
		blockColorReloadListener = new BlockColorReloadListener(blockColors);
		fluidModelReloadListener = new FluidModelReloadListener();
		entityModelReloadListener = new EntityModelReloadListener();
		entityAssetReloadListener = new EntityAssetReloadListener();
		resourceManager.registerReloadListener(colorMapReloadListener);
		resourceManager.registerReloadListener(blockColorReloadListener);
		resourceManager.registerReloadListener(fluidModelReloadListener);
		resourceManager.registerReloadListener(entityModelReloadListener);
		resourceManager.registerReloadListener(entityAssetReloadListener);
	}

	public static ContentPacksClient getInstance() {
		return INSTANCE;
	}

	public static PackRepository getPackRepository() {
		return getInstance().packRepository;
	}

	public static ContentPacksOptions getOptions() {
		return getInstance().options;
	}
}