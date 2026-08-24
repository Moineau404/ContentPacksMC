package mod.moineau.contentpacks.integration;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.fluid.ContentFluid;
import mod.moineau.contentpacks.block.statepredicates.StatePredicate;
import mod.moineau.contentpacks.block.statepredicates.StatePredicateType;
import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicate;
import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicateType;
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
import mod.moineau.contentpacks.resource.RegistryManager;
import mod.moineau.contentpacks.resource.ResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.function.Consumer;

public abstract class ContentPacksExtension {
    public abstract void onInitialize();

    public abstract void onContentLoaded();

    protected static <T> void subscribeRegistry(ResourceKey<? extends Registry<T>> registry, RegistryManager.Callback<T> callback) {
        ContentPacks.getInstance().getRegistryManager().subscribe(registry, callback);
    }

    protected static <T> void subscribeRegistry(ResourceKey<? extends Registry<T>> registry, Consumer<T> consumer) {
        ContentPacks.getInstance().getRegistryManager().subscribe(registry, consumer);
    }

    protected static <T> void subscribeRegistry(ResourceKey<? extends Registry<T>> registry, List<RegistryManager.Callback<T>> callbacks) {
        ContentPacks.getInstance().getRegistryManager().subscribe(registry, callbacks);
    }

    protected static void registerLoader(ResourceLoader loader) {
        ContentPacks.getInstance().registerLoader(loader);
    }

    protected static void registerLoader(ResourceLoader loader, int priority) {
        ContentPacks.getInstance().registerLoader(loader, priority);
    }

    protected static void registerBlockType(Identifier id, MapCodec<? extends Block> type) {
        Registry.register(BuiltInRegistries.BLOCK_TYPE, id, type);
    }

    protected static void registerFluidType(Identifier id, MapCodec<? extends ContentFluid> type) {
        Registry.register(ContentRegistries.FLUID_TYPE, id, type);
    }

    protected static void registerItemType(Identifier id, MapCodec<? extends Item> type, Class<? extends Item> clazz) {
        Registry.register(ContentRegistries.ITEM_TYPE, id, type);
        ItemTypes.register(clazz, type);
    }

    protected static void registerStatePredicateType(Identifier id, StatePredicateType<? extends StatePredicate> type) {
        Registry.register(ContentRegistries.STATE_PREDICATE_TYPE, id, type);
    }

    protected static void registerEntityTypedStatePredicateType(Identifier id, EntityTypedStatePredicateType<? extends EntityTypedStatePredicate> type) {
        Registry.register(ContentRegistries.ENTITY_TYPED_STATE_PREDICATE_TYPE, id, type);
    }

    protected static void registerBlockInteractionType(Identifier id, InteractionType<Block, ?> type) {
        BlockInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerBlockTagInteractionType(Identifier id, InteractionType<TagKey<Block>, ?> type) {
        BlockTagInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerFluidInteractionType(Identifier id, InteractionType<Fluid, ?> type) {
        FluidInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerFluidTagInteractionType(Identifier id, InteractionType<TagKey<Fluid>, ?> type) {
        FluidTagInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerItemInteractionType(Identifier id, InteractionType<Item, ?> type) {
        ItemInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerItemTagInteractionType(Identifier id, InteractionType<TagKey<Item>, ?> type) {
        ItemTagInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerEntityTypeInteractionType(Identifier id, InteractionType<EntityType<?>, ?> type) {
        EntityTypeInteractionTypes.ID_MAPPER.put(id, type);
    }

    protected static void registerBlockEntityTypeInteractionType(Identifier id, InteractionType<BlockEntityType<?>, ?> type) {
        BlockEntityTypeInteractionTypes.ID_MAPPER.put(id, type);
    }
}