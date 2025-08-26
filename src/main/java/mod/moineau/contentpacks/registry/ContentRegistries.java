package mod.moineau.contentpacks.registry;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.contextpredicate.BlockContextPredicateType;
import mod.moineau.contentpacks.block.contextpredicate.entitytyped.EntityTypedBlockContextPredicateType;
import mod.moineau.contentpacks.block.statepredicate.BlockStatePredicateType;
import mod.moineau.contentpacks.item.ItemTypes;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.block.WoodType;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class ContentRegistries {
    private static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Registries");
    private static final List<Runnable> INITIALIZERS = new LinkedList<>();
    public static final Registry<BlockStatePredicateType<?>> BLOCK_STATE_PREDICATE_TYPE = create(ContentRegistryKeys.BLOCK_STATE_PREDICATE_TYPE, BlockStatePredicateType::initialize);
    public static final Registry<BlockContextPredicateType<?>> BLOCK_CONTEXT_PREDICATE_TYPE = create(ContentRegistryKeys.BLOCK_CONTEXT_PREDICATE_TYPE, BlockContextPredicateType::initialize);
    public static final Registry<EntityTypedBlockContextPredicateType<?>> ENTITY_TYPED_BLOCK_CONTEXT_PREDICATE_TYPE = create(ContentRegistryKeys.ENTITY_TYPED_BLOCK_CONTEXT_PREDICATE_TYPE, EntityTypedBlockContextPredicateType::initialize);
    public static final Registry<BlockSoundGroup> BLOCK_SOUND_GROUP = create(ContentRegistryKeys.BLOCK_SOUND_GROUP, VanillaBlockSoundGroups::initialize);
    public static final Registry<BlockSetType> BLOCK_SET_TYPE = create(ContentRegistryKeys.BLOCK_SET_TYPE, VanillaBlockSetTypes::initialize);
    public static final Registry<WoodType> WOOD_TYPE = create(ContentRegistryKeys.WOOD_TYPE, VanillaWoodTypes::initialize);
    public static final Registry<SaplingGenerator> SAPLING_GENERATOR = create(ContentRegistryKeys.SAPLING_GENERATOR, VanillaSaplingGenerators::initialize);
    public static final Registry<ToolMaterial> TOOL_MATERIAL = create(ContentRegistryKeys.TOOL_MATERIAL, VanillaToolMaterials::initialize);
    public static final Registry<ArmorMaterial> ARMOR_MATERIAL = create(ContentRegistryKeys.ARMOR_MATERIAL, VanillaArmorMaterials::initialize);
    public static final Registry<MapCodec<? extends Item>> ITEM_TYPE = create(ContentRegistryKeys.ITEM_TYPE, ItemTypes::initialize);

    private static <T> SimpleRegistry<T> create(RegistryKey<Registry<T>> registryKey) {
        LOGGER.debug("Creating registry: {}", registryKey);
        return FabricRegistryBuilder.createSimple(registryKey).attribute(RegistryAttribute.SYNCED).buildAndRegister();
    }

    private static <T> SimpleRegistry<T> create(RegistryKey<Registry<T>> registryKey, Consumer<Registry<T>> initializer) {
        SimpleRegistry<T> registry = create(registryKey);
        INITIALIZERS.add(() -> initializer.accept(registry));
        return registry;
    }

    private static <T> DefaultedRegistry<T> create(RegistryKey<Registry<T>> registryKey, Identifier defaultId) {
        return FabricRegistryBuilder.createDefaulted(registryKey, defaultId).buildAndRegister();
    }

    private static <T> DefaultedRegistry<T> create(RegistryKey<Registry<T>> registryKey, Identifier defaultId, Consumer<Registry<T>> initializer) {
        DefaultedRegistry<T> registry = create(registryKey, defaultId);
        INITIALIZERS.add(() -> initializer.accept(registry));
        return registry;
    }

    public static void bootstrap() {
        INITIALIZERS.forEach(Runnable::run);
    }
}
