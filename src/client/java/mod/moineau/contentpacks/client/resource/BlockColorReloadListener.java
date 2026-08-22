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

public class BlockColorReloadListener extends DependentReloadListener<Block, Optional<List<BlockTintSource>>> {
    private static final Codec<Optional<List<BlockTintSource>>> CODEC = BlockTintSourceTypes.CODEC.listOf().optionalFieldOf("tints").codec();
    private final ContentBlockColors blockColors;

    public BlockColorReloadListener(BlockColors blockColors) {
        super("blockstates", CODEC);
        this.blockColors = (ContentBlockColors) blockColors;
    }

    @Override
    protected @Nullable Block getDependence(Identifier id) {
        return BuiltInRegistries.BLOCK.getValue(id);
    }

    @Override
    protected void loadEntry(Block block, Optional<List<BlockTintSource>> tints, Identifier id) {
        tints.ifPresent(object -> blockColors.contentpacks$addSourceOverrides(block, new ArrayList<>(object)));
    }

    @Override
    protected void handleNullError(Identifier id) {
        ContentPacksClient.LOGGER.debug("Discovered unknown block color {}, ignoring", id);
    }

    @Override
    protected void handleReadingError(Identifier id, String pack, String message) {
        ContentPacksClient.LOGGER.error("Failed to load block tints for block {} from pack {}: {}", id, pack, message);
    }

    @Override
    protected void handlePartialError(Identifier id, String pack, String message) {
        ContentPacksClient.LOGGER.error("Partially loaded block tints for block {} from pack {}: {}", id, pack, message);
    }
}