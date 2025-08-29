package mod.moineau.contentpacks.resource;

import mod.moineau.contentpacks.interaction.block.BlockInteractionTypes;
import mod.moineau.contentpacks.interaction.blockentitytype.BlockEntityTypeInteractionTypes;
import mod.moineau.contentpacks.interaction.blocktag.BlockTagInteractionTypes;
import mod.moineau.contentpacks.interaction.entitytype.EntityTypeInteractionTypes;
import mod.moineau.contentpacks.interaction.item.ItemInteractionTypes;
import mod.moineau.contentpacks.interaction.itemtag.ItemTagInteractionTypes;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;

import java.util.LinkedList;
import java.util.List;

public final class InteractionManager {
    private static final List<InteractionLoader<?>> LOADERS = new LinkedList<>();
    public static final InteractionLoader<Block> BLOCKS = InteractionLoader.create(Registries.BLOCK, BlockInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<Item> ITEMS = InteractionLoader.create(Registries.ITEM, ItemInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<TagKey<Block>> BLOCK_TAGS = InteractionLoader.create(RegistryKeys.BLOCK, BlockTagInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<TagKey<Item>> ITEM_TAGS = InteractionLoader.create(RegistryKeys.ITEM, ItemTagInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<EntityType<?>> ENTITY_TYPES = InteractionLoader.create(Registries.ENTITY_TYPE, EntityTypeInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<BlockEntityType<?>> BLOCK_ENTITY_TYPES = InteractionLoader.create(Registries.BLOCK_ENTITY_TYPE, BlockEntityTypeInteractionTypes.ID_MAPPER);

    public static void registerLoader(InteractionLoader<?> loader) {
//        LOADERS.add(loader);
        ContentManager.registerLoader(loader);
    }

    public static void load(ResourceManager resourceManager) {
        LOADERS.forEach(loader -> loader.load(resourceManager));
    }

    static {
        registerLoader(BLOCKS);
        registerLoader(ITEMS);
        registerLoader(BLOCK_TAGS);
        registerLoader(ITEM_TAGS);
        registerLoader(ENTITY_TYPES);
        registerLoader(BLOCK_ENTITY_TYPES);
    }
}
