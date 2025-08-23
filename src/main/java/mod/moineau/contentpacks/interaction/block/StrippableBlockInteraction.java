package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.BlockStateDefinition;
import mod.moineau.contentpacks.block.StrippableBlockStateMap;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

public record StrippableBlockInteraction(BlockStateDefinition<BlockState> definition) implements Interaction<Block> {
    public static final MapCodec<StrippableBlockInteraction> CODEC = BlockStateDefinition.createCodec(BlockState.CODEC)
            .xmap(StrippableBlockInteraction::new, StrippableBlockInteraction::definition).fieldOf("definition");

    @Override
    public void register(Block target) {
        definition.bake(target.getStateManager()).forEach(StrippableBlockStateMap::put);
    }

    @Override
    public InteractionType<Block, ?> getType() {
        return BlockInteractionTypes.STRIPPABLE;
    }

    @ApiStatus.Experimental
    public static Optional<StrippableBlockInteraction> get(Block block) {
        return Optional.of(StrippableBlockStateMap.getAll(block))
                .map(map -> BlockStateDefinition.recreate(block.getStateManager(), map::get))
                .map(StrippableBlockInteraction::new);
    }
}
