package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;

public record FabricFlattenableBlockInteraction(BlockState flattened) implements Interaction<Block> {
    public static final MapCodec<FabricFlattenableBlockInteraction> CODEC = BlockState.CODEC
            .xmap(FabricFlattenableBlockInteraction::new, FabricFlattenableBlockInteraction::flattened).fieldOf("flattened");

    @Override
    public void register(Block target) {
        FlattenableBlockRegistry.register(target, flattened);
    }

    @Override
    public InteractionType<Block, ?> getType() {
        return BlockInteractionTypes.FABRIC_FLATTENABLE;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Optional<FabricFlattenableBlockInteraction> get(Block block) {
        return Optional.ofNullable(ShovelItemAccessor.getFlattenables().get(block))
                .map(FabricFlattenableBlockInteraction::new);
    }
}
