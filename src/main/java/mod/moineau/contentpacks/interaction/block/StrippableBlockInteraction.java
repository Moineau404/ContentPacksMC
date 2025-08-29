package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.StrippableBlockStateMap;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import mod.moineau.contentpacks.state.VariantMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public record StrippableBlockInteraction(VariantMap<BlockState> variants) implements Interaction<Block> {
    public static final MapCodec<StrippableBlockInteraction> CODEC = VariantMap.createCodec(BlockState.CODEC)
            .xmap(StrippableBlockInteraction::new, StrippableBlockInteraction::variants).fieldOf("variants");

    @Override
    public void register(Block target) {
        for (var state : target.getStateManager().getStates()) {
            var value = variants.get(state);
            if (value != null) {
                StrippableBlockStateMap.put(state, value);
            }
        }
    }

    @Override
    public InteractionType<Block, ?> getType() {
        return BlockInteractionTypes.STRIPPABLE;
    }
}
