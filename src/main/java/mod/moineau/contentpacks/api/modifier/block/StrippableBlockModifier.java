package mod.moineau.contentpacks.api.modifier.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.block.StrippableBlockStateMap;
import mod.moineau.contentpacks.api.modifier.Modifier;
import mod.moineau.contentpacks.state.VariantMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record StrippableBlockModifier(VariantMap<BlockState> variants) implements Modifier<Block> {
    public static final Codec<StrippableBlockModifier> CODEC = VariantMap.createCodec(BlockState.CODEC)
            .xmap(StrippableBlockModifier::new, StrippableBlockModifier::variants);

    @Override
    public void apply(Block target) {
        for (BlockState state : target.getStateDefinition().getPossibleStates()) {
            BlockState value = variants.get(state);
            if (value != null) {
                StrippableBlockStateMap.put(state, value);
            }
        }
    }
}
