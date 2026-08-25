package mod.moineau.contentpacks.integration;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.statepredicates.StatePredicate;
import mod.moineau.contentpacks.block.statepredicates.StatePredicateType;
import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicate;
import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicateType;
import mod.moineau.contentpacks.extra.fluid.ContentFluid;
import mod.moineau.contentpacks.item.ItemTypes;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class ContentPacksExtension extends AbstractContentPacksExtension {
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
}