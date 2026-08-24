package mod.moineau.contentpacks.api;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.block.CustomShapedBlock;
import mod.moineau.contentpacks.api.modifier.Modifier;
import mod.moineau.contentpacks.api.modifier.ModifierType;
import mod.moineau.contentpacks.integration.ContentPacksExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentPacksAPI extends ContentPacksExtension {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacksAPI");

    @Override
    public void onInitialize() {
        ModifierType.bootStrap();
        //
        registerBlockType(ContentPacks.id("custom_shaped"), CustomShapedBlock.CODEC);
        //
        ModifierType.getInstances().values().forEach(modifierType -> {
            subscribeRegistry(modifierType.getRegistry(), (id, value, metadata) -> {
                //noinspection unchecked,rawtypes
                modifierType.getModifiers(metadata).forEach(modifier -> ((Modifier) modifier).apply(value));
            });
        });
    }

    @Override
    public void onContentLoaded() {
        //
    }
}
