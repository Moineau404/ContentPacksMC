package mod.moineau.contentpacks.registry;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.block.contextpredicate.BlockContextPredicateType;
import mod.moineau.contentpacks.block.contextpredicate.entitytyped.EntityTypedBlockContextPredicateType;
import mod.moineau.contentpacks.fluid.ContentFluid;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.block.WoodType;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

public final class ContentRegistryKeys {
    public static final RegistryKey<Registry<Property<?>>> PROPERTIES = ofVanilla("properties");
    public static final RegistryKey<Registry<BlockContextPredicateType<?>>> BLOCK_CONTEXT_PREDICATE_TYPE = of("block_context_predicate_type");
    public static final RegistryKey<Registry<EntityTypedBlockContextPredicateType<?>>> ENTITY_TYPED_BLOCK_CONTEXT_PREDICATE_TYPE = of("entity_typed_block_context_predicate_type");
    public static final RegistryKey<Registry<BlockSoundGroup>> BLOCK_SOUND_GROUP = ofVanilla("sound_type");
    public static final RegistryKey<Registry<BlockSetType>> BLOCK_SET_TYPE = ofVanilla("block_set_type");
    public static final RegistryKey<Registry<WoodType>> WOOD_TYPE = ofVanilla("wood_type");
    public static final RegistryKey<Registry<SaplingGenerator>> SAPLING_GENERATOR = ofVanilla("tree_grower");
    public static final RegistryKey<Registry<MapCodec<? extends ContentFluid>>> FLUID_TYPE = of("fluid_type");
    public static final RegistryKey<Registry<ToolMaterial>> TOOL_MATERIAL = ofVanilla("tool_material");
    public static final RegistryKey<Registry<ArmorMaterial>> ARMOR_MATERIAL = ofVanilla("armor_material");
    public static final RegistryKey<Registry<MapCodec<? extends Item>>> ITEM_TYPE = of("item_type");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(Identifier.of(ContentPacks.MOD_ID, id));
    }

    private static <T> RegistryKey<Registry<T>> ofVanilla(String id) {
        return RegistryKey.ofRegistry(Identifier.ofVanilla(id));
    }
}
