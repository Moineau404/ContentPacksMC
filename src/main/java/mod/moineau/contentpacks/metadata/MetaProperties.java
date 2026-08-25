package mod.moineau.contentpacks.metadata;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.block.FlattenableBlockStateMap;
import mod.moineau.contentpacks.block.StrippableBlockStateMap;
import mod.moineau.contentpacks.codec.FabricCodecs;
import mod.moineau.contentpacks.state.VariantMap;
import net.fabricmc.fabric.api.registry.*;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MetaProperties {
    private static final Map<ResourceKey<? extends Registry<?>>, MetaPropertyRegistry<?>> REGISTRIES = new HashMap<>();
    public static final MetaProperty<Block, FlammableBlockRegistry.Entry> FLAMMABLE = new MetaProperty<>("flammable", FabricCodecs.FLAMMABLE_BLOCK_REGISTRY_ENTRY, FlammableBlockRegistry.getDefaultInstance()::add,
            block -> Optional.of(FlammableBlockRegistry.getDefaultInstance().get(block)).filter(entry -> entry.getIgniteOdds() != 0 || entry.getBurnOdds() != 0));
    public static final MetaProperty<Block, Block> STRIPPABLE = new MetaProperty<>("strippable", BuiltInRegistries.BLOCK.byNameCodec(), StrippableBlockRegistry::register, block -> Optional.ofNullable(AxeItemAccessor.getStrippables().get(block)));
    public static final MetaProperty<Block, BlockState> FLATTENABLE = new MetaProperty<>("flattenable", BlockState.CODEC, FlattenableBlockRegistry::register, block -> Optional.ofNullable(ShovelItemAccessor.getFlattenables().get(block)));
    public static final MetaProperty<Block, BlockEntityType<?>> BLOCK_ENTITY = new MetaProperty<>("block_entity_type", BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec(), (block, blockEntityType) -> blockEntityType.addValidBlock(block));
    public static final MetaProperty<BlockEntityType<?>, List<Block>> VALID_BLOCKS = new MetaProperty<>("valid_blocks", BuiltInRegistries.BLOCK.byNameCodec().listOf(), (blockEntityType, blocks) -> blocks.forEach(blockEntityType::addValidBlock));
    public static final MetaProperty<Item, Float> COMPOSTING_CHANCE = new MetaProperty<>("composting_chance", ExtraCodecs.POSITIVE_FLOAT, CompostableRegistry.INSTANCE::add);
    public static final MetaProperty<Item, Float> FUEL = new MetaProperty<>("fuel", ExtraCodecs.POSITIVE_FLOAT, (item, value) -> FuelValueEvents.BUILD.register((builder, context) -> builder.add(item, (int) (context.baseSmeltTime() * value))));
    public static final MetaProperty<Block, VariantMap<BlockState>> STRIPPABLE_STATE = new MetaProperty<>(ContentPacks.id("strippable"), VariantMap.createCodec(BlockState.CODEC), StrippableBlockStateMap::put);
    public static final MetaProperty<Block, VariantMap<BlockState>> FLATTENABLE_STATE = new MetaProperty<>(ContentPacks.id("flattenable"), VariantMap.createCodec(BlockState.CODEC), FlattenableBlockStateMap::put);

    @SuppressWarnings({"rawtypes","unchecked"})
    public static <O, T> MetaProperty<? super O, T> register(ResourceKey<? extends Registry<O>> registryName, MetaProperty<? super O, T> property) {
        MetaPropertyRegistry<O> registry = (MetaPropertyRegistry) REGISTRIES.computeIfAbsent(registryName, _ -> new MetaPropertyRegistry<>());
        registry.register(property);
        return property;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public static <O> List<MetaProperty.Value<O, ?>> get(ResourceKey<? extends Registry<O>> registryName, ResourceMetadata metadata) {
        MetaPropertyRegistry<O> registry = (MetaPropertyRegistry) REGISTRIES.get(registryName);
        return registry != null ? (List) registry.get(metadata) : List.of();
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public static <O> List<MetaProperty.Value<O, ?>> get(ResourceKey<? extends Registry<O>> registryName, O object) {
        MetaPropertyRegistry<O> registry = (MetaPropertyRegistry) REGISTRIES.get(registryName);
        return registry != null ? (List) registry.get(object) : List.of();
    }

    public static void bootStrap() {
        register(Registries.BLOCK, FLAMMABLE);
        register(Registries.BLOCK, STRIPPABLE);
        register(Registries.BLOCK, FLATTENABLE);
        register(Registries.BLOCK, BLOCK_ENTITY);
        register(Registries.BLOCK, STRIPPABLE_STATE);
        register(Registries.BLOCK, FLATTENABLE_STATE);
        register(Registries.BLOCK_ENTITY_TYPE, VALID_BLOCKS);
        register(Registries.ITEM, COMPOSTING_CHANCE);
        register(Registries.ITEM, FUEL);
    }
}
