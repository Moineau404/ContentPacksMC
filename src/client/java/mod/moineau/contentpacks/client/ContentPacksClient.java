package mod.moineau.contentpacks.client;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.options.ContentPacksOptions;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSourceTypes;
import mod.moineau.contentpacks.client.render.entity.EntityModelTypes;
import mod.moineau.contentpacks.client.render.entity.EntityRendererTypes;
import mod.moineau.contentpacks.client.resource.*;
import mod.moineau.contentpacks.client.world.biome.ColorResolvers;
import mod.moineau.contentpacks.resource.ResourceLoader;
import mod.moineau.packrepos.PackRepos;
import mod.moineau.packrepos.packs.RequiredFolderRepositorySource;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Consumer;

// TODO : Make this class an instance instead of every fields being static
/**
 * Main client class of Content Packs mod.
 */
public final class ContentPacksClient implements ClientModInitializer, ResourceLoader {
	private static ContentPacksClient INSTANCE;
	public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Client");
	public static final SystemToast.SystemToastId TOAST_LOAD_FAILURE = new SystemToast.SystemToastId(10000L);
	public static final SystemToast.SystemToastId TOAST_CHANGED = new SystemToast.SystemToastId();
	public static final SystemToast.SystemToastId TOAST_OUTPUT = new SystemToast.SystemToastId();
	public static final Path REQUIRED_CONTENT_PACK_DIRECTORY = FabricLoader.getInstance().getConfigDir().resolve(PackRepos.MOD_ID).resolve("required_contentpacks");
	public static final Path BUNDLED_CONTENT_PACK_DIRECTORY = FabricLoader.getInstance().getConfigDir().resolve(PackRepos.MOD_ID).resolve("bundled_contentpacks");
	public static final FolderRepositorySource REQUIRED_CONTENT_PACK_REPOSITORY_SOURCE = new RequiredFolderRepositorySource(REQUIRED_CONTENT_PACK_DIRECTORY, ContentPacks.PACK_TYPE, PackRepos.REQUIRED_PACK_SOURCE, PackRepos.DIRECTORY_VALIDATOR);
	public static final FolderRepositorySource BUNDLED_CONTENT_PACK_REPOSITORY_SOURCE = new RequiredFolderRepositorySource(BUNDLED_CONTENT_PACK_DIRECTORY, ContentPacks.PACK_TYPE, PackRepos.BUNDLED_PACK_SOURCE, PackRepos.DIRECTORY_VALIDATOR);
	private PackRepository packRepository;
	private ContentPacksOptions options;
	private ColorResolverManager colorResolverManager;
	private ColorMapReloadListener colorMapReloadListener;
	private BlockColorReloadListener blockColorReloadListener;
	private FluidModelReloadListener fluidModelReloadListener;
	private EntityModelReloadListener entityModelReloadListener;
	private EntityAssetReloadListener entityAssetReloadListener;

	public ContentPacksClient() {
		if (INSTANCE != null) throw new IllegalStateException("ContentPacksClient already initialized!");
		INSTANCE = this;
	}

	@Override
	public void onInitializeClient() {
		ColorResolvers.bootStrap();
		BlockTintSourceTypes.bootStrap();
		EntityModelTypes.bootStrap();
		EntityRendererTypes.bootStrap();
		this.colorResolverManager = new ColorResolverManager();
		REQUIRED_CONTENT_PACK_DIRECTORY.toFile().mkdirs();
		BUNDLED_CONTENT_PACK_DIRECTORY.toFile().mkdirs();
		this.packRepository = new PackRepository(ContentPacks.getInstance().getRepositorySource(), REQUIRED_CONTENT_PACK_REPOSITORY_SOURCE, BUNDLED_CONTENT_PACK_REPOSITORY_SOURCE);
		this.packRepository.reload();
		this.options = ContentPacksOptions.read();
		this.options.updateRepository(packRepository);
		ContentPacks.getInstance().registerLoader(this);
		ContentPacks.getInstance().loadRepository(packRepository);
	}

	@Override
	public void load(ResourceManager resourceManager, Consumer<String> errorHandler) {
		this.colorResolverManager.load(resourceManager, errorHandler);
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