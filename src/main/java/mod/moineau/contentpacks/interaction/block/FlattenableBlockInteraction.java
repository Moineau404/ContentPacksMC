package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.BlockStateDefinition;
import mod.moineau.contentpacks.block.FlattenableBlockStateMap;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

public record FlattenableBlockInteraction(BlockStateDefinition<BlockState> definition) implements Interaction<Block> {
    public static final MapCodec<FlattenableBlockInteraction> CODEC = BlockStateDefinition.createCodec(BlockState.CODEC)
            .xmap(FlattenableBlockInteraction::new, FlattenableBlockInteraction::definition).fieldOf("definition");

    @Override
    public void register(Block target) {
        definition.bake(target.getStateManager()).forEach(FlattenableBlockStateMap::put);
    }

    @Override
    public InteractionType<Block, ?> getType() {
        return BlockInteractionTypes.FLATTENABLE;
    }

    @ApiStatus.Experimental
    public static Optional<FlattenableBlockInteraction> get(Block block) {
        return Optional.of(FlattenableBlockStateMap.getAll(block))
                .map(map -> BlockStateDefinition.recreate(block.getStateManager(), map::get))
                .map(FlattenableBlockInteraction::new);
    }
}
