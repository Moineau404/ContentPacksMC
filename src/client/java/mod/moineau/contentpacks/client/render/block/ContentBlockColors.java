package mod.moineau.contentpacks.client.render.block;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;

import java.util.List;

public interface ContentBlockColors {
    void contentpacks$addSourceOverride(Block block, BlockTintSource tintSource);

    void contentpacks$addSourceOverrides(Block block, List<BlockTintSource> tintSources);

    void contentpacks$clearSourceOverrides();
}
