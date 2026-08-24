package mod.moineau.contentpacks.api.modifier.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record FabricFlammableBlockModifier(int burn, int spread) implements Modifier<Block> {
    public static final Codec<FabricFlammableBlockModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("burn_chance").forGetter(FabricFlammableBlockModifier::burn),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("spread_chance").forGetter(FabricFlammableBlockModifier::spread)
    ).apply(instance, FabricFlammableBlockModifier::new));

    @Override
    public void apply(Block target) {
        FlammableBlockRegistry.getDefaultInstance().add(target, burn, spread);
    }

    @SuppressWarnings("OptionalOfNullableMisuse")
    public static Optional<FabricFlammableBlockModifier> get(Block block) {
        return Optional.ofNullable(FlammableBlockRegistry.getDefaultInstance().get(block))
                .filter(entry -> entry.getIgniteOdds() != 0 || entry.getBurnOdds() != 0)
                .map(entry -> new FabricFlammableBlockModifier(entry.getIgniteOdds(), entry.getBurnOdds()));
    }
}
