package mod.moineau.contentpacks.api.modifier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.modifier.block.*;
import mod.moineau.contentpacks.api.modifier.blockentitytype.FabricValidBlocksBlockEntityTypeModifier;
import mod.moineau.contentpacks.api.modifier.blocktag.FabricFlammableBlockTagModifier;
import mod.moineau.contentpacks.api.modifier.item.FabricCompostingChanceItemModifier;
import mod.moineau.contentpacks.api.modifier.item.FabricFuelItemModifier;
import mod.moineau.contentpacks.api.modifier.itemtag.FabricCompostingChanceItemTagModifier;
import mod.moineau.contentpacks.api.modifier.itemtag.FabricFuelItemTagModifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.function.Function;

public class ModifierType<T> {
    private static final Map<ResourceKey<? extends Registry<?>>, ModifierType<?>> INSTANCES = new HashMap<>();
    public static final ModifierType<Block> BLOCK = new ModifierType<>(Registries.BLOCK);
    public static final ModifierType<BlockEntityType<?>> BLOCK_ENTITY_TYPE = new ModifierType<>(Registries.BLOCK_ENTITY_TYPE);
    public static final ModifierType<TagKey<Block>> BLOCK_TAG = new ModifierType<>();
    public static final ModifierType<EntityType<?>> ENTITY_TYPE = new ModifierType<>(Registries.ENTITY_TYPE);
    public static final ModifierType<Fluid> FLUID = new ModifierType<>(Registries.FLUID);
    public static final ModifierType<TagKey<Fluid>> FLUID_TAG = new ModifierType<>();
    public static final ModifierType<Item> ITEM = new ModifierType<>(Registries.ITEM);
    public static final ModifierType<TagKey<Item>> ITEM_TAG = new ModifierType<>();
    private final ResourceKey<? extends Registry<T>> registry;
    private final Map<MetadataSectionType<? extends Modifier<T>>, Function<T, Optional<? extends Modifier<T>>>> sections = new HashMap<>();

    public ModifierType() {
        this.registry = null;
    }

    public ModifierType(ResourceKey<? extends Registry<T>> registry) {
        this.registry = registry;
        INSTANCES.put(registry, this);
    }

    public List<Modifier<T>> getModifiers(ResourceMetadata metadata) {
        List<Modifier<T>> modifiers = new LinkedList<>();
        this.sections.forEach((section, _) -> metadata.getSection(section).ifPresent(modifiers::add));
        return modifiers;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Pair<Modifier<T>, Codec<Modifier<T>>>> getModifiers(T value) {
        Map<String, Pair<Modifier<T>, Codec<Modifier<T>>>> modifiers = new HashMap<>();
        this.sections.forEach((section, getter) -> getter.apply(value).ifPresent(modifier -> modifiers.put(section.name(), Pair.of(modifier, (Codec<Modifier<T>>) section.codec()))));
        return modifiers;
    }

    public void register(Identifier id, Codec<? extends Modifier<T>> codec, Function<T, Optional<? extends Modifier<T>>> getter) {
        this.sections.put(new MetadataSectionType<>(id.toString(), codec), getter);
    }

    public void register(Identifier id, Codec<? extends Modifier<T>> codec) {
        this.register(id, codec, _ -> Optional.empty());
    }

    public ResourceKey<? extends Registry<T>> getRegistry() {
        return registry;
    }

    public static Map<ResourceKey<? extends Registry<?>>, ModifierType<?>> getInstances() {
        return INSTANCES;
    }

    public static void bootStrap() {
        BLOCK.register(ContentPacks.id("strippable"), StrippableBlockModifier.CODEC);
        BLOCK.register(ContentPacks.id("flattenable"), FlattenableBlockModifier.CODEC);
        BLOCK.register(Identifier.fromNamespaceAndPath("fabric", "strippable"), FabricStrippableBlockModifier.CODEC, FabricStrippableBlockModifier::get);
        BLOCK.register(Identifier.fromNamespaceAndPath("fabric", "flattenable"), FabricFlattenableBlockModifier.CODEC, FabricFlattenableBlockModifier::get);
        BLOCK.register(Identifier.fromNamespaceAndPath("fabric", "flammable"), FabricFlammableBlockModifier.CODEC, FabricFlammableBlockModifier::get);
        BLOCK_ENTITY_TYPE.register(Identifier.fromNamespaceAndPath("fabric", "valid_blocks"), FabricValidBlocksBlockEntityTypeModifier.CODEC, FabricValidBlocksBlockEntityTypeModifier::get);
        BLOCK_TAG.register(Identifier.fromNamespaceAndPath("fabric", "flammable"), FabricFlammableBlockTagModifier.CODEC);
        ITEM.register(Identifier.fromNamespaceAndPath("fabric", "fuel"), FabricFuelItemModifier.CODEC);
        ITEM.register(Identifier.fromNamespaceAndPath("fabric", "composting_chance"), FabricCompostingChanceItemModifier.CODEC, FabricCompostingChanceItemModifier::get);
        ITEM_TAG.register(Identifier.fromNamespaceAndPath("fabric", "fuel"), FabricFuelItemTagModifier.CODEC);
        ITEM_TAG.register(Identifier.fromNamespaceAndPath("fabric", "composting_chance"), FabricCompostingChanceItemTagModifier.CODEC);
    }
}
