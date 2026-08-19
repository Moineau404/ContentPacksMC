package mod.moineau.contentpacks.resource;

import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.block.BlockWithEntityTypes;
import mod.moineau.contentpacks.codec.*;
import mod.moineau.contentpacks.entity.BoatType;
import mod.moineau.contentpacks.event.ContentPacksEvents;
import mod.moineau.contentpacks.fluid.ContentFluid;
import mod.moineau.contentpacks.item.ItemTypes;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockTypes;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public final class ContentManager {
    private static final TreeMap<Integer, List<ResourceLoader>> LOADERS = new TreeMap<>(Comparator.reverseOrder());
    public static final RegistryLoader<SoundType> SOUND_TYPES = new RegistryLoader<>(ContentRegistries.SOUND_TYPE, SoundTypeCodecs.CODEC);
    public static final RegistryLoader<BlockSetType> BLOCK_SET_TYPES = new RegistryLoader<>(ContentRegistries.BLOCK_SET_TYPE, BlockSetTypeCodecs.CODEC, true);
    public static final RegistryLoader<WoodType> WOOD_TYPES = new RegistryLoader<>(ContentRegistries.WOOD_TYPE, WoodTypeCodecs.CODEC, true);
    public static final RegistryLoader<TreeGrower> TREE_GROWERS = new RegistryLoader<>(ContentRegistries.TREE_GROWER, TreeGrowerCodecs.CODEC, true);
    public static final RegistryLoader<Fluid> FLUIDS = new RegistryLoader<>(BuiltInRegistries.FLUID, ContentFluid.DOWNGRADED_CODEC);
    public static final RegistryLoader<Block> BLOCKS = new RegistryLoader<>(BuiltInRegistries.BLOCK, BlockTypes.CODEC.codec(), true);
    // TODO boat_type -> entity_type
    public static final RegistryLoader<EntityType<?>> BOAT_TYPES = new RegistryLoader<>(BuiltInRegistries.ENTITY_TYPE, CodecUtil.downgrade(BoatType.CODEC), true, "boat_type");
    public static final RegistryLoader<ToolMaterial> TOOL_MATERIALS = new RegistryLoader<>(ContentRegistries.TOOL_MATERIAL, ToolMaterialCodecs.CODEC);
    public static final RegistryLoader<ArmorMaterial> ARMOR_MATERIALS = new RegistryLoader<>(ContentRegistries.ARMOR_MATERIAL, ArmorMaterialCodecs.CODEC);
    public static final RegistryLoader<Item> ITEMS = new RegistryLoader<>(BuiltInRegistries.ITEM, ItemTypes.CODEC.codec(), true);
    public static final RegistryLoader<CreativeModeTab> CREATIVE_MODE_TABS = new RegistryLoader<>(BuiltInRegistries.CREATIVE_MODE_TAB, CreativeModTabCodecs.CODEC);

    public static void registerLoader(ResourceLoader loader) {
        LOADERS.computeIfAbsent(loader.getPriority(), _ -> new LinkedList<>()).add(loader);
    }

    //TODO Make all loaders output a list of errors and write all on a file
    public static void load(ResourceManager resourceManager) {
        LOADERS.forEach((_, list) -> list.forEach(loader -> loader.load(resourceManager)));
        ContentPacksEvents.CONTENT_LOADED.invoker().onContentLoaded();
    }

    public static List<ResourceLoader> getLoaders() {
        List<ResourceLoader> list = new LinkedList<>();
        LOADERS.forEach((_, loaderList) -> list.addAll(loaderList));
        return list;
    }

    static {
        BLOCK_SET_TYPES.registerListener(entries -> entries.forEach(entry -> BlockSetType.register(entry.value())));
        WOOD_TYPES.registerListener(entries -> entries.forEach(entry -> WoodType.register(entry.value())));
        FLUIDS.registerListener(entries -> entries.forEach(entry -> {
            for (FluidState fluidState : entry.value().getStateDefinition().getPossibleStates()) {
                Fluid.FLUID_STATE_REGISTRY.add(fluidState);
            }
        }));
        BLOCKS.registerListener(entries -> entries.forEach(entry -> {
            Block block = entry.value();
            if (block instanceof EntityBlock) {
                BlockWithEntityTypes.register(block);
            }
        }));
        registerLoader(SOUND_TYPES);
        registerLoader(BLOCK_SET_TYPES);
        registerLoader(WOOD_TYPES);
        registerLoader(TREE_GROWERS);
        registerLoader(FLUIDS);
        registerLoader(BLOCKS);
        registerLoader(BOAT_TYPES);
        registerLoader(TOOL_MATERIALS);
        registerLoader(ARMOR_MATERIALS);
        registerLoader(ITEMS);
        registerLoader(CREATIVE_MODE_TABS);
    }
}
