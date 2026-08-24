package mod.moineau.contentpacks.api.client;

import mod.moineau.contentpacks.api.client.render.CustomTextureRegistry;
import mod.moineau.contentpacks.integration.ContentPacksExtension;
import net.minecraft.core.registries.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentPacksAPIClient extends ContentPacksExtension {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacksAPI/Client");

    @Override
    public void onInitialize() {
        subscribeRegistry(Registries.BLOCK, (id, value, metadata) -> {
            metadata.getSection(CustomTextureRegistry.METADATA_SECTION_TYPE).ifPresent(texture -> CustomTextureRegistry.register(value, texture));
        });
    }

    @Override
    public void onContentLoaded() {
        //
    }
}
