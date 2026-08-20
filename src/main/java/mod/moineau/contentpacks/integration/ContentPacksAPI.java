package mod.moineau.contentpacks.integration;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.block.CustomShapedBlock;
import net.minecraft.resources.Identifier;

public class ContentPacksAPI extends ContentPacksExtension {
    @Override
    public void beforeContentLoaded() {
        registerBlockType(Identifier.fromNamespaceAndPath(ContentPacks.MOD_ID, "custom_shaped"), CustomShapedBlock.CODEC);
    }
}
