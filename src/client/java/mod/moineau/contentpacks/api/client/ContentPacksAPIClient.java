package mod.moineau.contentpacks.api.client;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.client.modifier.CustomTextureModifier;
import mod.moineau.contentpacks.api.modifier.ModifierType;
import mod.moineau.contentpacks.client.integration.ContentPacksClientExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentPacksAPIClient extends ContentPacksClientExtension {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacksAPI/Client");

    @Override
    public void onInitialize() {
        ModifierType.BLOCK.register(ContentPacks.id("custom_texture"), CustomTextureModifier.createCodec(), CustomTextureModifier::get);
    }

    @Override
    public void onContentLoaded() {
        //
    }
}
