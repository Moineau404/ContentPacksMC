package mod.moineau.contentpacks.registry;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.block.statepredicates.StatePredicateType;
import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicateType;
import mod.moineau.contentpacks.fluid.ContentFluid;
import mod.moineau.contentpacks.fluid.FluidTypes;
import mod.moineau.contentpacks.item.ItemTypes;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class ContentRegistries {
    private static final Logger LOGGER = ContentPacks.LOGGER;
    private static final List<Runnable> INITIALIZERS = new LinkedList<>();
    public static final Registry<Property<?>> PROPERTIES = create(ContentRegistryKeys.PROPERTIES, VanillaProperties::initialize);
    public static final Registry<StatePredicateType<?>> BLOCK_CONTEXT_PREDICATE_TYPE = create(ContentRegistryKeys.BLOCK_CONTEXT_PREDICATE_TYPE, StatePredicateType::initialize);
    public static final Registry<EntityTypedStatePredicateType<?>> ENTITY_TYPED_BLOCK_CONTEXT_PREDICATE_TYPE = create(ContentRegistryKeys.ENTITY_TYPED_BLOCK_CONTEXT_PREDICATE_TYPE, EntityTypedStatePredicateType::initialize);
    public static final Registry<SoundType> SOUND_TYPE = create(ContentRegistryKeys.SOUND_TYPE, VanillaSoundTypes::initialize);
    public static final Registry<BlockSetType> BLOCK_SET_TYPE = create(ContentRegistryKeys.BLOCK_SET_TYPE, VanillaBlockSetTypes::initialize);
    public static final Registry<WoodType> WOOD_TYPE = create(ContentRegistryKeys.WOOD_TYPE, VanillaWoodTypes::initialize);
    public static final Registry<TreeGrower> TREE_GROWER = create(ContentRegistryKeys.SAPLING_GENERATOR, VanillaTreeGrowers::initialize);
    public static final Registry<MapCodec<? extends ContentFluid>> FLUID_TYPE = create(ContentRegistryKeys.FLUID_TYPE, FluidTypes::initialize);
    public static final Registry<ToolMaterial> TOOL_MATERIAL = create(ContentRegistryKeys.TOOL_MATERIAL, VanillaToolMaterials::initialize);
    public static final Registry<ArmorMaterial> ARMOR_MATERIAL = create(ContentRegistryKeys.ARMOR_MATERIAL, VanillaArmorMaterials::initialize);
    public static final Registry<MapCodec<? extends Item>> ITEM_TYPE = create(ContentRegistryKeys.ITEM_TYPE, ItemTypes::initialize);
    public static final Registry<MapCodec<? extends EntityType.EntityFactory<?>>> ENTITY_FACTORY = create(ContentRegistryKeys.ENTITY_FACTORY, VanillaEntityFactories::initialize);

    private static <T> MappedRegistry<T> create(ResourceKey<Registry<T>> resourceKey) {
        LOGGER.debug("Creating registry: {}", resourceKey);
        return FabricRegistryBuilder.create(resourceKey).attribute(RegistryAttribute.SYNCED).buildAndRegister();
    }

    private static <T> MappedRegistry<T> create(ResourceKey<Registry<T>> resourceKey, Consumer<Registry<T>> initializer) {
        MappedRegistry<T> registry = create(resourceKey);
        INITIALIZERS.add(() -> initializer.accept(registry));
        return registry;
    }

    private static <T> DefaultedRegistry<T> create(ResourceKey<Registry<T>> resourceKey, Identifier defaultId) {
        return FabricRegistryBuilder.createDefaulted(resourceKey, defaultId).buildAndRegister();
    }

    private static <T> DefaultedRegistry<T> create(ResourceKey<Registry<T>> resourceKey, Identifier defaultId, Consumer<Registry<T>> initializer) {
        DefaultedRegistry<T> registry = create(resourceKey, defaultId);
        INITIALIZERS.add(() -> initializer.accept(registry));
        return registry;
    }

    public static void bootStrap() {
        INITIALIZERS.forEach(Runnable::run);
    }
}
