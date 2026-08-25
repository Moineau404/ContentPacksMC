package mod.moineau.contentpacks.extra;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.extra.block.CustomShapedBlock;
import mod.moineau.contentpacks.integration.ContentPacksExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentPacksExtra extends ContentPacksExtension {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacksExtra");

    @Override
    public void onInitialize() {
        registerBlockType(ContentPacks.id("custom_shaped"), CustomShapedBlock.CODEC);
    }

    @Override
    public void onContentLoaded() {
        //
    }
}
