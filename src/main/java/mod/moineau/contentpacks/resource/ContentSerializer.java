package mod.moineau.contentpacks.resource;

import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.block.*;
import mod.moineau.contentpacks.block.statefunction.BlockStateFunction;
import mod.moineau.contentpacks.block.statefunction.DefinitionBlockStateFunction;
import mod.moineau.contentpacks.codec.BlockSetTypeCodecs;
import mod.moineau.contentpacks.codec.BlockSoundGroupCodecs;
import mod.moineau.contentpacks.codec.SaplingGeneratorCodecs;
import mod.moineau.contentpacks.codec.WoodTypeCodecs;
import mod.moineau.contentpacks.codec.ArmorMaterialCodecs;
import mod.moineau.contentpacks.codec.ItemGroupCodecs;
import mod.moineau.contentpacks.item.ItemTypes;
import mod.moineau.contentpacks.codec.ToolMaterialCodecs;
import mod.moineau.contentpacks.registry.ContentRegistries;
import mod.moineau.contentpacks.block.MapColors;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

@ApiStatus.Internal
public final class ContentSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Serialization");
    private static final Gson GSON = new Gson();
    private static final String DIRECTORY = "./.contentpacks.out/content/";
    private static final RegistrySerializer<BlockSoundGroup> BLOCK_SOUND_GROUP = new RegistrySerializer<>(ContentRegistries.BLOCK_SOUND_GROUP, BlockSoundGroupCodecs.CODEC);
    private static final RegistrySerializer<BlockSetType> BLOCK_SET_TYPE = new RegistrySerializer<>(ContentRegistries.BLOCK_SET_TYPE, BlockSetTypeCodecs.CODEC);
    private static final RegistrySerializer<WoodType> WOOD_TYPE = new RegistrySerializer<>(ContentRegistries.WOOD_TYPE, WoodTypeCodecs.CODEC);
    private static final RegistrySerializer<SaplingGenerator> SAPLING_GENERATOR = new RegistrySerializer<>(ContentRegistries.SAPLING_GENERATOR, SaplingGeneratorCodecs.CODEC);
    private static final RegistrySerializer<Block> BLOCK = new RegistrySerializer<>(Registries.BLOCK, BlockTypes.CODEC.codec());
    private static final RegistrySerializer<ToolMaterial> TOOL_MATERIAL = new RegistrySerializer<>(ContentRegistries.TOOL_MATERIAL, ToolMaterialCodecs.CODEC);
    private static final RegistrySerializer<ArmorMaterial> ARMOR_MATERIAL = new RegistrySerializer<>(ContentRegistries.ARMOR_MATERIAL, ArmorMaterialCodecs.CODEC);
    private static final RegistrySerializer<Item> ITEM = new RegistrySerializer<>(Registries.ITEM, ItemTypes.CODEC.codec());
    private static final RegistrySerializer<ItemGroup> ITEM_GROUP = new RegistrySerializer<>(Registries.ITEM_GROUP, ItemGroupCodecs.CODEC);

    public static void output() {
        if (true) return;
        new File("./.contentpacks.out/").mkdir();
        new File(DIRECTORY).mkdir();
        BLOCK_SOUND_GROUP.writeAll();
        BLOCK_SET_TYPE.writeAll();
        WOOD_TYPE.writeAll();
        SAPLING_GENERATOR.writeAll();
        BLOCK.writeAll();
        TOOL_MATERIAL.writeAll();
        ARMOR_MATERIAL.writeAll();
        ITEM.writeAll();
        ITEM_GROUP.writeAll();
        //writeAllMapColorProviders();
        //writeAllLuminances();
    }

    private static void test_BlockStateDefinition1() {
        LOGGER.warn(BlockStateDefinition.createCodec(MapColors.CODEC)
                .encodeStart(JsonOps.INSTANCE, BlockStateDefinition.recreate(Blocks.OAK_PLANKS.getStateManager(), Blocks.OAK_PLANKS.getSettings().mapColorProvider)).toString());
    }

    public static <T> BlockStateFunction<T> convert(StateManager<Block, BlockState> stateManager, Function<BlockState, T> function) {
        return new DefinitionBlockStateFunction<>(BlockStateDefinition.recreate(stateManager, function), null);
    }

    public static void writeAllMapColorProviders() {
        new File(DIRECTORY + ".map_color_provider/").mkdir();
        for (Block block : Registries.BLOCK) {
            StateManager<Block, BlockState> stateManager = block.getStateManager();
            BlockStateDefinition<MapColor> definiton = BlockStateDefinition.recreate(stateManager, block.getSettings().mapColorProvider);
            Codec<BlockStateDefinition<MapColor>> codec = BlockStateDefinition.createCodec(MapColors.CODEC);
            DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, definiton);
            Identifier id = Registries.BLOCK.getId(block);
            result.ifSuccess(jsonElement -> toFile(jsonElement, ".map_color_provider/" + id.getNamespace() + "." + id.getPath()));
        }
    }

    public static void writeAllLuminances() {
        new File(DIRECTORY + ".luminance/").mkdir();
        for (Block block : Registries.BLOCK) {
            StateManager<Block, BlockState> stateManager = block.getStateManager();
            ToIntFunction<BlockState> function = block.getSettings().luminance;
            BlockStateDefinition<Integer> definiton = BlockStateDefinition.recreate(stateManager, function::applyAsInt);
            Codec<BlockStateDefinition<Integer>> codec = BlockStateDefinition.createCodec(Codec.INT);
            DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, definiton);
            Identifier id = Registries.BLOCK.getId(block);
            result.ifSuccess(jsonElement -> toFile(jsonElement, ".luminance/" + id.getNamespace() + "." + id.getPath()));
        }
    }

    public static void toFile(JsonElement jsonElement, String fileNameNoExtension) {
        try {
            File file = new File(DIRECTORY + fileNameNoExtension + ".json");
            file.createNewFile();
            JsonWriter jsonWriter = new JsonWriter(new FileWriter(file));
            jsonWriter.setFormattingStyle(FormattingStyle.PRETTY);
            GSON.toJson(jsonElement, jsonWriter);
            jsonWriter.flush();
            jsonWriter.close();
        } catch (IOException e) {
            LOGGER.error("Failed to write file {}.json | {}", fileNameNoExtension, e);
        }
    }

    private static final class RegistrySerializer<T> {
        private final Registry<T> registry;
        private final Codec<T> codec;
        private final transient String type;

        private RegistrySerializer(Registry<T> registry, Codec<T> codec) {
            this.registry = registry;
            this.codec = codec;
            this.type = this.registry.getKey().getValue().getPath();
        }

        private void writeAll() {
            for (Map.Entry<RegistryKey<T>, T> entry : registry.getEntrySet()) {
                try {
                    DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, entry.getValue());
                    if (result.hasResultOrPartial()) {
                        Identifier id = entry.getKey().getValue();
                        new File(DIRECTORY + id.getNamespace()).mkdir();
                        new File(DIRECTORY + id.getNamespace() + '/' + type).mkdir();
                        toFile(result.getPartialOrThrow(), id.getNamespace() + '/' + type + '/' + id.getPath());
                        if (result.isError()) {
                            LOGGER.error("Partially encoded {} {} | {}", type, id, result.error().get().message());
                        } else {
                            LOGGER.debug("Successfully encoded {} {}", type, id);
                        }
                    } else {
                        LOGGER.error("Failed to encode {} {} | {}", type, entry.getKey().getValue(), result.error().get().message());
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to encode {} {} | {}", type, entry.getKey().getValue().toString(), e);
                }
            }
        }
    }
}
