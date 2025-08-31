package mod.moineau.contentpacks.resource;

import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.block.BlockWithEntityTypes;
import mod.moineau.contentpacks.codec.*;
import mod.moineau.contentpacks.entity.BoatType;
import mod.moineau.contentpacks.event.ContentPacksEvents;
import mod.moineau.contentpacks.item.ItemTypes;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.BlockSoundGroup;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public final class ContentManager {
    private static final TreeMap<Integer, List<ResourceLoader>> LOADERS = new TreeMap<>(Comparator.reverseOrder());
    public static final RegistryLoader<BlockSoundGroup> BLOCK_SOUND_GROUPS = new RegistryLoader<>(ContentRegistries.BLOCK_SOUND_GROUP, BlockSoundGroupCodecs.CODEC);
    public static final RegistryLoader<BlockSetType> BLOCK_SET_TYPES = new RegistryLoader<>(ContentRegistries.BLOCK_SET_TYPE, BlockSetTypeCodecs.CODEC, true);
    public static final RegistryLoader<WoodType> WOOD_TYPES = new RegistryLoader<>(ContentRegistries.WOOD_TYPE, WoodTypeCodecs.CODEC, true);
    public static final RegistryLoader<SaplingGenerator> SAPLING_GENERATORS = new RegistryLoader<>(ContentRegistries.SAPLING_GENERATOR, SaplingGeneratorCodecs.CODEC, true);
    public static final RegistryLoader<Block> BLOCKS = new RegistryLoader<>(Registries.BLOCK, BlockTypes.CODEC.codec(), true);
    // TODO boat_type -> entity_type
    public static final RegistryLoader<EntityType<?>> BOAT_TYPES = new RegistryLoader<>(Registries.ENTITY_TYPE, CodecUtil.downgrade(BoatType.CODEC), true, "boat_type");
    public static final RegistryLoader<ToolMaterial> TOOL_MATERIALS = new RegistryLoader<>(ContentRegistries.TOOL_MATERIAL, ToolMaterialCodecs.CODEC);
    public static final RegistryLoader<ArmorMaterial> ARMOR_MATERIALS = new RegistryLoader<>(ContentRegistries.ARMOR_MATERIAL, ArmorMaterialCodecs.CODEC);
    public static final RegistryLoader<Item> ITEMS = new RegistryLoader<>(Registries.ITEM, ItemTypes.CODEC.codec(), true);
    public static final RegistryLoader<ItemGroup> ITEM_GROUPS = new RegistryLoader<>(Registries.ITEM_GROUP, ItemGroupCodecs.CODEC);

    public static void registerLoader(ResourceLoader loader) {
        LOADERS.computeIfAbsent(loader.getPriority(), (priority) -> new LinkedList<>()).add(loader);
    }

    //TODO Make all loaders output a list of errors and write all on a file (and/or make an highlighted button in content pack screen)
    // - It's crucial to have a good error monitoring for real use of the mod
    public static void load(ResourceManager resourceManager) {
        LOADERS.forEach((priority, list) -> list.forEach(loader -> loader.load(resourceManager)));
        ContentPacksEvents.CONTENT_LOADED.invoker().onContentLoaded();
    }

    static {
        BLOCK_SET_TYPES.registerListener(entries -> entries.forEach(entry -> BlockSetType.register(entry.value())));
        WOOD_TYPES.registerListener(entries -> entries.forEach(entry -> WoodType.register(entry.value())));
        BLOCKS.registerListener(entries -> entries.forEach(entry -> {
            Block block = entry.value();
            if (block instanceof BlockEntityProvider) {
                BlockWithEntityTypes.register(block);
            }
        }));
        registerLoader(BLOCK_SOUND_GROUPS);
        registerLoader(BLOCK_SET_TYPES);
        registerLoader(WOOD_TYPES);
        registerLoader(SAPLING_GENERATORS);
        registerLoader(BLOCKS);
        registerLoader(BOAT_TYPES);
        registerLoader(TOOL_MATERIALS);
        registerLoader(ARMOR_MATERIALS);
        registerLoader(ITEMS);
        registerLoader(ITEM_GROUPS);
    }
}
