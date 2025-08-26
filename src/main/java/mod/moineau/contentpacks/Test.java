package mod.moineau.contentpacks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.state.PropertiesPredicate;
import mod.moineau.contentpacks.state.PropertyPredicate;
import mod.moineau.contentpacks.state.StateDefinition;
import net.minecraft.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Test");

    public static void run() {
//        test_log();
//        test_door();
//        test_leaves();
        test_definition();
    }

    public static void test_log() {
        PropertyPredicate.Unbaked axisY = PropertyPredicate.parse("axis=y").getOrThrow();
        PropertiesPredicate propertiesPredicate = PropertiesPredicate.of(axisY);
        Blocks.OAK_LOG.getStateManager().getStates().forEach(state -> {
            LOGGER.error("{}: {}", state, propertiesPredicate.test(state));
        });
    }

    public static void test_door() {
        PropertyPredicate.Unbaked facingEast = PropertyPredicate.parse("facing=east").getOrThrow();
        PropertyPredicate.Unbaked hingeRight = PropertyPredicate.parse("hinge=right").getOrThrow();
        PropertyPredicate.Unbaked halfUpper = PropertyPredicate.parse("half=upper").getOrThrow();
        PropertiesPredicate propertiesPredicate1 = PropertiesPredicate.of(facingEast, hingeRight, halfUpper);
        PropertiesPredicate propertiesPredicate2 = PropertiesPredicate.parse("facing=west,hinge=left,half=lower").getOrThrow();
        Blocks.OAK_DOOR.getStateManager().getStates().forEach(state -> {
            LOGGER.error("{}: {}", state, propertiesPredicate1.test(state));
        });
        Blocks.BIRCH_DOOR.getStateManager().getStates().forEach(state -> {
            LOGGER.error("{}: {}", state, propertiesPredicate2.test(state));
        });
    }

    public static void test_leaves() {
        PropertiesPredicate propertiesPredicate = PropertiesPredicate.parse("distance>=4,waterlogged=true").getOrThrow();
        Blocks.OAK_LEAVES.getStateManager().getStates().forEach(state -> {
            LOGGER.error("{}: {}", state, propertiesPredicate.test(state));
        });
    }

    public static void test_definition() {
        String json = "{\"distance>=4,waterlogged=true\":1,\"distance2,waterlogged=false\":2}";
        JsonElement jsonElement = new Gson().fromJson(json, JsonElement.class);
        Codec<StateDefinition<Integer>> codec = StateDefinition.createCodec(Codec.INT);
//        StateDefinition<Integer> definition = codec.parse(JsonOps.INSTANCE, jsonElement)
//                .ifError(error -> LOGGER.error(error.message())).getPartialOrThrow();
//        StateDefinition.Baked<Integer> baked = definition.bake(Blocks.OAK_LEAVES.getStateManager())
//                .ifError(error -> LOGGER.error(error.message())).getPartialOrThrow();
    }
}
