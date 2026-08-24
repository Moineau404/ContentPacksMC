package mod.moineau.contentpacks.api.modifier.blocktag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;

public record FabricFlammableBlockTagModifier(int burn, int spread) implements Modifier<TagKey<Block>> {
    public static final Codec<FabricFlammableBlockTagModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("burn_chance").forGetter(FabricFlammableBlockTagModifier::burn),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("spread_chance").forGetter(FabricFlammableBlockTagModifier::spread)
    ).apply(instance, FabricFlammableBlockTagModifier::new));

    @Override
    public void apply(TagKey<Block> target) {
        FlammableBlockRegistry.getDefaultInstance().add(target, burn, spread);
    }
}
