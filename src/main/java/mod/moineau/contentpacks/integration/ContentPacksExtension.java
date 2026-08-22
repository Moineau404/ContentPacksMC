package mod.moineau.contentpacks.integration;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.InteractionType;
import mod.moineau.contentpacks.interaction.block.BlockInteractionTypes;
import mod.moineau.contentpacks.interaction.blockentitytype.BlockEntityTypeInteractionTypes;
import mod.moineau.contentpacks.interaction.blocktag.BlockTagInteractionTypes;
import mod.moineau.contentpacks.interaction.entitytype.EntityTypeInteractionTypes;
import mod.moineau.contentpacks.interaction.fluid.FluidInteractionTypes;
import mod.moineau.contentpacks.interaction.fluidtag.FluidTagInteractionTypes;
import mod.moineau.contentpacks.interaction.item.ItemInteractionTypes;
import mod.moineau.contentpacks.interaction.itemtag.ItemTagInteractionTypes;
import mod.moineau.contentpacks.item.ItemTypes;
import mod.moineau.contentpacks.registry.ContentRegistries;
import mod.moineau.contentpacks.resource.ContentManager;
import mod.moineau.contentpacks.resource.ResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

public abstract class ContentPacksExtension {
    public void beforeContentLoaded() {}

    public void afterContentLoaded() {}

    // TODO Add doc
    protected static void registerLoader(ResourceLoader loader) {
        ContentManager.registerLoader(loader);
    }

    protected static void registerLoader(ResourceLoader loader, int priority) {
        ContentManager.registerLoader(new ResourceLoader() {
            @Override
            public void load(ResourceManager resourceManager) {
                loader.load(resourceManager);
            }

            @Override
            public int getPriority() {
                return priority;
            }
        });
    }

    protected static void registerBlockType(Identifier id, MapCodec<? extends Block> type) {
        Registry.register(BuiltInRegistries.BLOCK_TYPE, id, type);
    }

    protected static void registerItemType(Identifier id, MapCodec<? extends Item> type, Class<? extends Item> clazz) {
        Registry.register(ContentRegistries.ITEM_TYPE, id, type);
        ItemTypes.register(clazz, type);
    }

    protected static void registerBlockInteraction(Identifier id, InteractionType<Block, ?> type) {
        BlockInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerBlockTagInteraction(Identifier id, InteractionType<TagKey<Block>, ?> type) {
        BlockTagInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerFluidInteraction(Identifier id, InteractionType<Fluid, ?> type) {
        FluidInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerFluidTagInteraction(Identifier id, InteractionType<TagKey<Fluid>, ?> type) {
        FluidTagInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerItemInteraction(Identifier id, InteractionType<Item, ?> type) {
        ItemInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerItemTagInteraction(Identifier id, InteractionType<TagKey<Item>, ?> type) {
        ItemTagInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerEntityTypeInteraction(Identifier id, InteractionType<EntityType<?>, ?> type) {
        EntityTypeInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerBlockEntityTypeInteraction(Identifier id, InteractionType<BlockEntityType<?>, ?> type) {
        BlockEntityTypeInteractionTypes.ID_MAPPER.put(id, type);
    }
}