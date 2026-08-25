package mod.moineau.contentpacks.block;

import mod.moineau.contentpacks.state.VariantMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class StrippableBlockStateMap {
    private static final Map<BlockState, BlockState> TEMP = new HashMap<>();
    private static BiConsumer<BlockState, BlockState> HANDLER = TEMP::put;
    private static Function<BlockState, BlockState> GETTER = TEMP::get;
    private static Runnable CLEARER = TEMP::clear;

    public static void put(BlockState input, BlockState output) {
        HANDLER.accept(input, output);
    }

    public static void put(Block input, VariantMap<BlockState> outputs) {
        for (BlockState state : input.getStateDefinition().getPossibleStates()) {
            BlockState value = outputs.get(state);
            if (value != null) {
                put(state, value);
            }
        }
    }

    public static Optional<BlockState> get(BlockState state) {
        return Optional.ofNullable(GETTER.apply(state));
    }

    public static Map<BlockState, BlockState> getAll(Block block) {
        Map<BlockState, BlockState> map = new HashMap<>();
        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            get(state).ifPresent(stripped -> map.put(state, stripped));
        }
        return map;
    }

    public static void clear() {
        CLEARER.run();
    }

    public static void setup(BiConsumer<BlockState, BlockState> vanillaHandler, Function<BlockState, BlockState> vanillaGetter, Runnable vanillaClearer) {
        TEMP.forEach(vanillaHandler);

        HANDLER = vanillaHandler;
        GETTER = vanillaGetter;
        CLEARER = vanillaClearer;
    }
}