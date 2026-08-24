package mod.moineau.contentpacks.resource;

import mod.moineau.contentpacks.interaction.block.BlockInteractionTypes;
import mod.moineau.contentpacks.interaction.blockentitytype.BlockEntityTypeInteractionTypes;
import mod.moineau.contentpacks.interaction.blocktag.BlockTagInteractionTypes;
import mod.moineau.contentpacks.interaction.entitytype.EntityTypeInteractionTypes;
import mod.moineau.contentpacks.interaction.item.ItemInteractionTypes;
import mod.moineau.contentpacks.interaction.itemtag.ItemTagInteractionTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class InteractionManager {
    private static final List<InteractionLoader<?>> LOADERS = new LinkedList<>();
    public static final InteractionLoader<Block> BLOCKS = InteractionLoader.create(BuiltInRegistries.BLOCK, BlockInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<Item> ITEMS = InteractionLoader.create(BuiltInRegistries.ITEM, ItemInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<TagKey<Block>> BLOCK_TAGS = InteractionLoader.create(Registries.BLOCK, BlockTagInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<TagKey<Item>> ITEM_TAGS = InteractionLoader.create(Registries.ITEM, ItemTagInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<EntityType<?>> ENTITY_TYPES = InteractionLoader.create(BuiltInRegistries.ENTITY_TYPE, EntityTypeInteractionTypes.ID_MAPPER);
    public static final InteractionLoader<BlockEntityType<?>> BLOCK_ENTITY_TYPES = InteractionLoader.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BlockEntityTypeInteractionTypes.ID_MAPPER);

    public static void registerLoader(InteractionLoader<?> loader) {
        LOADERS.add(loader);
    }

    public static void load(ResourceManager resourceManager, Consumer<String> errorHandler) {
        LOADERS.forEach(loader -> loader.load(resourceManager, errorHandler));
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
