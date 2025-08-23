package mod.moineau.contentpacks.render.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Deprecated
public class DynamicBlockColorProviders {
    private static final Map<BlockState, BlockColorProvider> TEMP = new HashMap<>();
    private static BiConsumer<BlockState, BlockColorProvider> HANDLER = TEMP::put;
    private static Runnable CLEARER = () -> {};
    private static BlockColors BLOCK_COLORS;

    public static void put(BlockState state, BlockColorProvider provider) {
        HANDLER.accept(state, provider);
    }

    public static void clear() {
        CLEARER.run();
    }

    public static void setup(BlockColors blockColors, BiConsumer<BlockState, BlockColorProvider> vanillaHandler, Runnable vanillaClearer) {
        if (BLOCK_COLORS == null) {
            BLOCK_COLORS = blockColors;

            TEMP.forEach(vanillaHandler);

            HANDLER = vanillaHandler;
            CLEARER = vanillaClearer;
        } else {
            if (BLOCK_COLORS != blockColors) throw new IllegalStateException("Cannot set block colors twice");
        }
    }
}