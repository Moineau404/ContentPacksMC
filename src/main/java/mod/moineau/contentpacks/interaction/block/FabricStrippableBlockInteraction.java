package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record FabricStrippableBlockInteraction(Block stripped) implements Interaction<Block> {
    public static final MapCodec<FabricStrippableBlockInteraction> CODEC = BuiltInRegistries.BLOCK.byNameCodec()
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
        return Optional.ofNullable(AxeItemAccessor.getStrippables().get(block))
                .map(FabricStrippableBlockInteraction::new);
    }
}
