package mod.moineau.contentpacks.api.modifier.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;

public record FabricFlattenableBlockModifier(BlockState flattened) implements Modifier<Block> {
    public static final Codec<FabricFlattenableBlockModifier> CODEC = BlockState.CODEC
            .xmap(FabricFlattenableBlockModifier::new, FabricFlattenableBlockModifier::flattened);

    @Override
    public void apply(Block target) {
        FlattenableBlockRegistry.register(target, flattened);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Optional<FabricFlattenableBlockModifier> get(Block block) {
        return Optional.ofNullable(ShovelItemAccessor.getFlattenables().get(block)).map(FabricFlattenableBlockModifier::new);
    }
}
