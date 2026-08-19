package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.FlattenableBlockStateMap;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import mod.moineau.contentpacks.state.VariantMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record FlattenableBlockInteraction(VariantMap<BlockState> variants) implements Interaction<Block> {
    public static final MapCodec<FlattenableBlockInteraction> CODEC = VariantMap.createCodec(BlockState.CODEC)
            .xmap(FlattenableBlockInteraction::new, FlattenableBlockInteraction::variants).fieldOf("variants");

    @Override
    public void register(Block target) {
        for (var state : target.getStateDefinition().getPossibleStates()) {
            var value = variants.get(state);
            if (value != null) {
                FlattenableBlockStateMap.put(state, value);
            }
        }
    }

    @Override
    public InteractionType<Block, ?> getType() {
        return BlockInteractionTypes.FLATTENABLE;
    }
}
