package mod.moineau.contentpacks.api;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.block.CustomShapedBlock;
import mod.moineau.contentpacks.integration.ContentPacksExtension;
import net.minecraft.resources.Identifier;

public class ContentPacksAPI extends ContentPacksExtension {
    @Override
    public void beforeContentLoaded() {
        registerBlockType(Identifier.fromNamespaceAndPath(ContentPacks.MOD_ID, "custom_shaped"), CustomShapedBlock.CODEC);
    }
}
