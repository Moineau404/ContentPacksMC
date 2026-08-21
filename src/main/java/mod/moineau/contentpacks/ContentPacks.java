package mod.moineau.contentpacks;

import mod.moineau.contentpacks.block.MapColors;
import mod.moineau.contentpacks.integration.ContentPacksExtension;
import mod.moineau.contentpacks.interaction.InteractionType;
import mod.moineau.contentpacks.registry.ContentRegistries;
import mod.moineau.contentpacks.resource.ContentManager;
import mod.moineau.contentpacks.resource.InteractionManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.OverlayMetadataSection;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.*;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Main class of Content Packs mod.
 */
public final class ContentPacks implements ModInitializer {
    private static ContentPacks INSTANCE;
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks");
    public static final String MOD_ID = "contentpacks";
    public static final Path PATH = FabricLoader.getInstance().getGameDir().resolve("contentpacks");
    public static final PackFormat PACK_VERSION = PackFormat.of(9);
    public static final int PACK_LAST_PRE_MINOR_VERSION = 0;
    public static final PackType PACK_TYPE = PackType.valueOf("CONTENTPACKS_CONTENT");
    public static final MetadataSectionType<PackMetadataSection> PACK_METADATA_SECTION_TYPE = new MetadataSectionType<>("pack", PackMetadataSection.codecForPackType(PACK_TYPE));
    public static final MetadataSectionType<OverlayMetadataSection> PACK_OVERLAY_METADATA_SECTION_TYPE = new MetadataSectionType<>("overlays", OverlayMetadataSection.codecForPackType(PACK_TYPE));
    public static final PackSource PACK_SOURCE = PackSource.create(packDescription -> Component.translatable("pack.nameAndSource", packDescription, Component.translatable("pack.source.content")).withStyle(ChatFormatting.GRAY), false);
    private RepositorySource repositorySource;
    private Collection<Pack> activePacks;
    private boolean hasLoaded;

    public ContentPacks() {
        INSTANCE = this;
    }

    @Override
    public void onInitialize() {
        ContentRegistries.bootStrap();
        MapColors.bootStrap();
        InteractionType.bootStrap();

        repositorySource = new FolderRepositorySource(PATH, PACK_TYPE, PACK_SOURCE, new DirectoryValidator(_ -> true));
    }

    public void loadRepository(PackRepository packRepository) {
        activePacks = packRepository.getSelectedPacks();
        load(packRepository.openAllSelected());
    }

    public void loadPacks(List<Pack> packs) {
        activePacks = packs;
        List<PackResources> packResources = new ArrayList<>();
        packs.forEach(pack -> packResources.add(pack.open()));
        load(packResources);
    }

    private void load(List<PackResources> packResources) {
        if (hasLoaded) {
            throw new IllegalStateException("Cannot load content twice!");
        }

        List<ContentPacksExtension> extensions = FabricLoader.getInstance().getEntrypoints("contentpacks", ContentPacksExtension.class);

        extensions.forEach(ContentPacksExtension::beforeContentLoaded);
        LOGGER.info("Loading content...");
        MultiPackResourceManager packManager = new MultiPackResourceManager(PACK_TYPE, packResources);
        ContentManager.load(packManager);
        InteractionManager.load(packManager);
        LOGGER.info("Content loaded!");
        extensions.forEach(ContentPacksExtension::afterContentLoaded);

        hasLoaded = true;
    }

    public static ContentPacks getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Content Packs has not been initialized!");
        }
        return INSTANCE;
    }

    public static RepositorySource getRepositorySource() {
        return getInstance().repositorySource;
    }

    public static Collection<Pack> getActivePacks() {
        return getInstance().activePacks;
    }

    public static boolean hasLoaded() {
        return INSTANCE != null && INSTANCE.hasLoaded;
    }
}