package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public record FabricFlammableBlockInteraction(int burn, int spread) implements Interaction<Block> {
    public static final MapCodec<FabricFlammableBlockInteraction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.NON_NEGATIVE_INT.fieldOf("burn_chance").forGetter(FabricFlammableBlockInteraction::burn),
            Codecs.NON_NEGATIVE_INT.fieldOf("spread_chance").forGetter(FabricFlammableBlockInteraction::spread)
    ).apply(instance, FabricFlammableBlockInteraction::new));

    @Override
    public void register(Block target) {
        FlammableBlockRegistry.getDefaultInstance().add(target, burn, spread);
    }

    @Override
    public InteractionType<Block, ?> getType() {
        return BlockInteractionTypes.FABRIC_FLAMMABLE;
    }

    public static Optional<FabricFlammableBlockInteraction> get(Block block) {
        return Optional.ofNullable(FlammableBlockRegistry.getDefaultInstance().get(block))
                .map(entry -> new FabricFlammableBlockInteraction(entry.getBurnChance(), entry.getSpreadChance()));
    }
}
