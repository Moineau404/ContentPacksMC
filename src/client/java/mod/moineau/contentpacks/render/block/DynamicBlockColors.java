package mod.moineau.contentpacks.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;

public interface DynamicBlockColors {
    void contentpacks$addProvider(Block block, BlockColorProvider provider);

    void contentpacks$clearProviders();
}
