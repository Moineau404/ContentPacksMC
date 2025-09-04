package mod.moineau.contentpacks;

import mod.moineau.contentpacks.block.MapColors;
import mod.moineau.contentpacks.integration.ContentPacksExtension;
import mod.moineau.contentpacks.interaction.InteractionType;
import mod.moineau.contentpacks.registry.ContentRegistries;
import mod.moineau.contentpacks.resource.ContentManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.path.SymlinkFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

public final class ContentPacks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks");
    public static final String MOD_ID = "contentpacks";
    public static final int PACK_VERSION = 7;
    public static final Path PATH = FabricLoader.getInstance().getGameDir().resolve("contentpacks");
    public static final ResourceType RESOURCE_TYPE = ResourceType.valueOf("CONTENT");
    public static final ResourcePackSource PACK_SOURCE = new ResourcePackSource() {
        @Override
        public Text decorate(Text packDisplayName) {
            return Text.translatable("pack.nameAndSource", packDisplayName, Text.translatable("pack.source.content")).formatted(Formatting.GRAY);
        }

        @Override
        public boolean canBeEnabledLater() {
            return false;
        }
    };
    public static final ResourcePackProvider PACK_PROVIDER = new FileResourcePackProvider(
            PATH, RESOURCE_TYPE, PACK_SOURCE, new SymlinkFinder(path -> true));
    private static boolean hasLoaded;

    @Override
    public void onInitialize() {
        ContentRegistries.bootstrap();
        MapColors.bootstrap();
        InteractionType.bootstrap();
    }

    // TODO Implementation of an error monitoring system with debug file where all errors are written by pack
    // TODO Make loading of content packs to occure after all mods are initialized
    public static void load(List<ResourcePack> packs) {
        if (hasLoaded) {
            throw new IllegalStateException("Cannot load content twice!");
        }
        List<ContentPacksExtension> extensions = FabricLoader.getInstance().getEntrypoints("contentpacks", ContentPacksExtension.class);
        extensions.forEach(ContentPacksExtension::beforeContentLoaded);
        LOGGER.info("Loading content...");
        ContentManager.load(new LifecycledResourceManagerImpl(RESOURCE_TYPE, packs));
        LOGGER.info("Content loaded!");
        hasLoaded = true;
        extensions.forEach(ContentPacksExtension::afterContentLoaded);
    }
}