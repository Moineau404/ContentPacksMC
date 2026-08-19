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
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks");
    public static final String MOD_ID = "contentpacks";
    public static final Path PATH = FabricLoader.getInstance().getGameDir().resolve("contentpacks");
    public static final PackFormat PACK_VERSION = PackFormat.of(9);
    public static final int PACK_LAST_PRE_MINOR_VERSION = 0;
    public static final PackType PACK_TYPE = PackType.valueOf("CONTENTPACKS_CONTENT");
    public static final MetadataSectionType<PackMetadataSection> PACK_METADATA_SECTION_TYPE = new MetadataSectionType<>("pack", PackMetadataSection.codecForPackType(PACK_TYPE));
    public static final MetadataSectionType<OverlayMetadataSection> PACK_OVERLAY_METADATA_SECTION_TYPE = new MetadataSectionType<>("overlays", OverlayMetadataSection.codecForPackType(PACK_TYPE));
    public static final PackSource PACK_SOURCE = PackSource.create(
            packDescription -> Component.translatable("pack.nameAndSource", packDescription, Component.translatable("pack.source.content")).withStyle(ChatFormatting.GRAY),
            false
    );
    public static final RepositorySource REPOSITORY_SOURCE = new FolderRepositorySource(
            PATH, PACK_TYPE, PACK_SOURCE, new DirectoryValidator(_ -> true));
    private static boolean hasLoaded;
    public static Collection<Pack> ACTIVE_PACKS;

    @Override
    public void onInitialize() {
        ContentRegistries.bootStrap();
        MapColors.bootStrap();
        InteractionType.bootStrap();
    }

    public static void loadRepository(PackRepository packRepository) {
        ACTIVE_PACKS = packRepository.getSelectedPacks();
        load(packRepository.openAllSelected());
    }

    public static void loadPacks(List<Pack> packs) {
        ACTIVE_PACKS = packs;
        List<PackResources> packResources = new ArrayList<>();
        packs.forEach(pack -> packResources.add(pack.open()));
        load(packResources);
    }

    // TODO Make loading of content packs to occure after all mods are initialized
    private static void load(List<PackResources> packResources) {
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

    public static Collection<Pack> getActivePacks() {
        return ACTIVE_PACKS;
    }
}