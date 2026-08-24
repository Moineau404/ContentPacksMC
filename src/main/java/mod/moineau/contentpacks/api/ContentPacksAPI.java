package mod.moineau.contentpacks.api;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.block.CustomShapedBlock;
import mod.moineau.contentpacks.integration.ContentPacksExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentPacksAPI extends ContentPacksExtension {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacksAPI");

    @Override
    public void onInitialize() {
        registerBlockType(ContentPacks.id("custom_shaped"), CustomShapedBlock.CODEC);
    }

    @Override
    public void onContentLoaded() {
        //
    }
}
