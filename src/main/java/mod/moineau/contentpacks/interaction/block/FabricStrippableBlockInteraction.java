package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;

import java.util.Optional;

public record FabricStrippableBlockInteraction(Block stripped) implements Interaction<Block> {
    public static final MapCodec<FabricStrippableBlockInteraction> CODEC = Registries.BLOCK.getCodec()
            .xmap(FabricStrippableBlockInteraction::new, FabricStrippableBlockInteraction::stripped).fieldOf("stripped");

    @Override
    public void register(Block target) {
        StrippableBlockRegistry.register(target, stripped);
    }

    @Override
    public InteractionType<Block, ?> getType() {
        return BlockInteractionTypes.FABRIC_STRIPPABLE;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Optional<FabricStrippableBlockInteraction> get(Block block) {
        return Optional.ofNullable(AxeItemAccessor.getStrippedBlocks().get(block))
                .map(FabricStrippableBlockInteraction::new);
    }
}
