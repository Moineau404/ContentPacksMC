package mod.moineau.contentpacks.client.resource;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.render.block.ContentBlockColors;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSource;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSourceTypes;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockColorReloadListener extends BoundReloadListener<Block, Optional<List<BlockTintSource>>> {
    private static final String DIRECTORY = "blockstates";
    private static final Codec<Optional<List<BlockTintSource>>> CODEC = BlockTintSourceTypes.CODEC.listOf().optionalFieldOf("tints").codec();
    private final ContentBlockColors blockColors;

    public BlockColorReloadListener(BlockColors blockColors) {
        super(DIRECTORY, CODEC);
        this.blockColors = (ContentBlockColors) blockColors;
    }

    @Override
    protected @Nullable Block getBound(Identifier id) {
        return BuiltInRegistries.BLOCK.getValue(id);
    }

    @Override
    protected void loadEntry(Block bound, Optional<List<BlockTintSource>> optional, Identifier id) {
        optional.ifPresent(object -> blockColors.contentpacks$addSourceOverrides(bound, new ArrayList<>(object)));
    }

    @Override
    protected void nullErrorProvider(Identifier id) {
        ContentPacksClient.LOGGER.debug("Discovered unknown block color {}, ignoring", id);
    }

    @Override
    protected void readingErrorProvider(Identifier id, String pack, String message) {
        ContentPacksClient.LOGGER.error("Failed to load block tints for block {} from pack {}: {}", id, pack, message);
    }
}