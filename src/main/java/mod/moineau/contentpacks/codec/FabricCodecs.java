package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public class FabricCodecs {
    public static final Codec<FlammableBlockRegistry.Entry> FLAMMABLE_BLOCK_REGISTRY_ENTRY = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("ignite_odds").forGetter(FlammableBlockRegistry.Entry::getIgniteOdds),
            Codec.INT.fieldOf("burn_odds").forGetter(FlammableBlockRegistry.Entry::getBurnOdds)
    ).apply(instance, FlammableBlockRegistry.Entry::new));
}
