package mod.moineau.contentpacks.api.modifier.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.block.FlattenableBlockStateMap;
import mod.moineau.contentpacks.api.modifier.Modifier;
import mod.moineau.contentpacks.state.VariantMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record FlattenableBlockModifier(VariantMap<BlockState> variants) implements Modifier<Block> {
    public static final Codec<FlattenableBlockModifier> CODEC = VariantMap.createCodec(BlockState.CODEC)
            .xmap(FlattenableBlockModifier::new, FlattenableBlockModifier::variants);

    @Override
    public void apply(Block target) {
        for (BlockState state : target.getStateDefinition().getPossibleStates()) {
            BlockState value = variants.get(state);
            if (value != null) {
                FlattenableBlockStateMap.put(state, value);
            }
        }
    }
}
