package mod.moineau.contentpacks.registry;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.block.statepredicates.StatePredicateType;
import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicateType;
import mod.moineau.contentpacks.extra.fluid.ContentFluid;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ContentRegistryKeys {
    public static final ResourceKey<Registry<Property<?>>> PROPERTIES = ofVanilla("properties");
    public static final ResourceKey<Registry<StatePredicateType<?>>> STATE_PREDICATE_TYPE = of("state_predicate_type");
    public static final ResourceKey<Registry<EntityTypedStatePredicateType<?>>> ENTITY_TYPED_STATE_PREDICATE_TYPE = of("entity_typed_state_predicate_type");
    public static final ResourceKey<Registry<SoundType>> SOUND_TYPE = ofVanilla("sound_type");
    public static final ResourceKey<Registry<BlockSetType>> BLOCK_SET_TYPE = ofVanilla("block_set_type");
    public static final ResourceKey<Registry<WoodType>> WOOD_TYPE = ofVanilla("wood_type");
    public static final ResourceKey<Registry<TreeGrower>> SAPLING_GENERATOR = ofVanilla("tree_grower");
    public static final ResourceKey<Registry<MapCodec<? extends ContentFluid>>> FLUID_TYPE = of("fluid_type");
    public static final ResourceKey<Registry<ToolMaterial>> TOOL_MATERIAL = ofVanilla("tool_material");
    public static final ResourceKey<Registry<ArmorMaterial>> ARMOR_MATERIAL = ofVanilla("armor_material");
    public static final ResourceKey<Registry<MapCodec<? extends Item>>> ITEM_TYPE = of("item_type");
    public static final ResourceKey<Registry<MapCodec<? extends EntityType.EntityFactory<?>>>> ENTITY_FACTORY = of("entity_factory");

    private static <T> ResourceKey<Registry<T>> of(String id) {
        return ResourceKey.createRegistryKey(ContentPacks.id(id));
    }

    private static <T> ResourceKey<Registry<T>> ofVanilla(String id) {
        return ResourceKey.createRegistryKey(Identifier.withDefaultNamespace(id));
    }
}
