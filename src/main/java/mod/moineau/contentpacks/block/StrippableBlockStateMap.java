package mod.moineau.contentpacks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

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

    public static Optional<BlockState> get(BlockState state) {
        return Optional.ofNullable(GETTER.apply(state));
    }

    public static Map<BlockState, BlockState> getAll(Block block) {
        Map<BlockState, BlockState> map = new HashMap<>();
        for (BlockState state : block.getStateManager().getStates()) {
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