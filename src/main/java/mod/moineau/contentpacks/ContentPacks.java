package mod.moineau.contentpacks;

import mod.moineau.api.util.FileUtil;
import mod.moineau.contentpacks.api.fluid.ContentFluid;
import mod.moineau.contentpacks.block.BlockWithEntityTypes;
import mod.moineau.contentpacks.block.MapColors;
import mod.moineau.contentpacks.codec.*;
import mod.moineau.contentpacks.integration.ContentPacksExtension;
import mod.moineau.contentpacks.item.ItemTypes;
import mod.moineau.contentpacks.registry.ContentRegistries;
import mod.moineau.contentpacks.registry.ContentRegistryKeys;
import mod.moineau.contentpacks.resource.RegistryLoader;
import mod.moineau.contentpacks.resource.RegistryManager;
import mod.moineau.contentpacks.resource.ResourceLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.OverlayMetadataSection;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.*;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.world.level.block.BlockTypes;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * Main class of Content Packs mod.
 */
public final class ContentPacks implements ModInitializer {
    private static ContentPacks INSTANCE;
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks");
    public static final String MOD_ID = "contentpacks";
    public static final Path PATH = FabricLoader.getInstance().getGameDir().resolve("contentpacks");
    public static final PackFormat PACK_VERSION = PackFormat.of(10);
    public static final int PACK_LAST_PRE_MINOR_VERSION = 0;
    public static final PackType PACK_TYPE = PackType.valueOf("CONTENTPACKS_CONTENT");
    public static final MetadataSectionType<PackMetadataSection> PACK_METADATA_SECTION_TYPE = new MetadataSectionType<>("pack", PackMetadataSection.codecForPackType(PACK_TYPE));
    public static final MetadataSectionType<OverlayMetadataSection> PACK_OVERLAY_METADATA_SECTION_TYPE = new MetadataSectionType<>("overlays", OverlayMetadataSection.codecForPackType(PACK_TYPE));
    public static final PackSource PACK_SOURCE = PackSource.create(packDescription -> Component.translatable("pack.nameAndSource", packDescription, Component.translatable("pack.source.content")).withStyle(ChatFormatting.GRAY), false);
    private static final Path ERRORS_PATH = FabricLoader.getInstance().getGameDir().resolve("logs").resolve(ContentPacks.MOD_ID).resolve("errors.log");
    private RepositorySource repositorySource;
    private RegistryManager registryManager;
    private final TreeMap<Integer, List<ResourceLoader>> loaders = new TreeMap<>();
    private List<ContentPacksExtension> extensions;
    private Collection<Pack> activePacks;
    private List<String> errors;
    private boolean hasLoaded;

    public ContentPacks() {
        if (INSTANCE != null) {
            throw new IllegalStateException("ContentPacks already initialized!");
        }
        INSTANCE = this;
    }

    @Override
    public void onInitialize() {
        ContentRegistries.bootStrap();
        MapColors.bootStrap();
        this.repositorySource = new FolderRepositorySource(PATH, PACK_TYPE, PACK_SOURCE, new DirectoryValidator(_ -> true));
        //
        this.registryManager = new RegistryManager();
        this.registryManager.register(new RegistryLoader<>(ContentRegistries.SOUND_TYPE, SoundTypeCodecs.CODEC));
        this.registryManager.register(new RegistryLoader<>(ContentRegistries.BLOCK_SET_TYPE, BlockSetTypeCodecs.CODEC, RegistryLoader.Flag.INJECT_ID));
        this.registryManager.register(new RegistryLoader<>(ContentRegistries.WOOD_TYPE, WoodTypeCodecs.CODEC, RegistryLoader.Flag.INJECT_ID));
        this.registryManager.register(new RegistryLoader<>(ContentRegistries.TREE_GROWER, TreeGrowerCodecs.CODEC, RegistryLoader.Flag.INJECT_ID));
        this.registryManager.register(new RegistryLoader<>(BuiltInRegistries.FLUID, ContentFluid.DOWNGRADED_CODEC));
        this.registryManager.register(new RegistryLoader<>(BuiltInRegistries.BLOCK, BlockTypes.CODEC.codec(), RegistryLoader.Flag.INJECT_ID));
        this.registryManager.register(new RegistryLoader<>(BuiltInRegistries.ENTITY_TYPE, EntityTypeCodecs.CODEC, RegistryLoader.Flag.INJECT_ID));
        this.registryManager.register(new RegistryLoader<>(ContentRegistries.TOOL_MATERIAL, ToolMaterialCodecs.CODEC));
        this.registryManager.register(new RegistryLoader<>(ContentRegistries.ARMOR_MATERIAL, ArmorMaterialCodecs.CODEC));
        this.registryManager.register(new RegistryLoader<>(BuiltInRegistries.ITEM, ItemTypes.CODEC.codec(), RegistryLoader.Flag.INJECT_ID));
        this.registryManager.register(new RegistryLoader<>(BuiltInRegistries.CREATIVE_MODE_TAB, CreativeModTabCodecs.CODEC));
        this.registryManager.subscribe(ContentRegistryKeys.BLOCK_SET_TYPE, BlockSetType::register);
        this.registryManager.subscribe(ContentRegistryKeys.WOOD_TYPE, WoodType::register);
        this.registryManager.subscribe(Registries.FLUID, fluid -> {
            for (FluidState fluidState : fluid.getStateDefinition().getPossibleStates()) {
                Fluid.FLUID_STATE_REGISTRY.add(fluidState);
            }
        });
        this.registryManager.subscribe(Registries.BLOCK, block -> {
            if (block instanceof EntityBlock) {
                BlockWithEntityTypes.register(block);
            }
        });
        //
        LOGGER.debug("Initializing extensions...");
        this.extensions = FabricLoader.getInstance().getEntrypoints("contentpacks.main", ContentPacksExtension.class);
        this.extensions.forEach(ContentPacksExtension::onInitialize);
    }

    public void loadRepository(PackRepository packRepository) {
        this.activePacks = packRepository.getSelectedPacks();
        load(packRepository.openAllSelected());
    }

    public void loadPacks(Collection<Pack> packs) {
        this.activePacks = packs;
        List<PackResources> packResources = new LinkedList<>();
        packs.forEach(pack -> packResources.add(pack.open()));
        load(packResources);
    }

    private void load(List<PackResources> packResources) {
        if (this.hasLoaded) {
            throw new IllegalStateException("Cannot load content twice!");
        }

        LOGGER.info("Loading content... ({} packs)", packResources.size());
        MultiPackResourceManager resourceManager = new MultiPackResourceManager(PACK_TYPE, packResources);
        this.errors = new LinkedList<>();
        this.registryManager.load(resourceManager, this.errors::add);
        this.loaders.values().forEach(list -> list.forEach(loader -> loader.load(resourceManager, this.errors::add)));
        if (this.errors.isEmpty()) {
            LOGGER.info("Content loaded successfully!");
        } else {
            this.errors.forEach(LOGGER::debug);
            LOGGER.error("Content loaded with {} errors!", errors.size());
        }
        FileUtil.writeLinesSafe(ERRORS_PATH.toFile(), errors, e -> LOGGER.error("Failed to write errors file:", e));

        this.hasLoaded = true;
        this.extensions.forEach(ContentPacksExtension::onContentLoaded);
    }

    public void registerLoader(ResourceLoader loader, int priority) {
        this.loaders.computeIfAbsent(priority, _ -> new LinkedList<>()).add(loader);
    }

    public void registerLoader(ResourceLoader loader) {
        this.registerLoader(loader, 0);
    }

    public static ContentPacks getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("ContentPacks has not been initialized!");
        }
        return INSTANCE;
    }

    public RepositorySource getRepositorySource() {
        return this.repositorySource;
    }

    public RegistryManager getRegistryManager() {
        return this.registryManager;
    }

    public Collection<Pack> getActivePacks() {
        return this.activePacks;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public static boolean hasLoaded() {
        return INSTANCE != null && INSTANCE.hasLoaded;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}