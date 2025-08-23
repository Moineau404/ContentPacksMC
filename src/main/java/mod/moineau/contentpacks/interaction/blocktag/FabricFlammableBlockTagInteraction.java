package mod.moineau.contentpacks.interaction.blocktag;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.dynamic.Codecs;

public record FabricFlammableBlockTagInteraction(int burn, int spread) implements Interaction<TagKey<Block>> {
    public static final MapCodec<FabricFlammableBlockTagInteraction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.NON_NEGATIVE_INT.fieldOf("burn_chance").forGetter(FabricFlammableBlockTagInteraction::burn),
            Codecs.NON_NEGATIVE_INT.fieldOf("spread_chance").forGetter(FabricFlammableBlockTagInteraction::spread)
    ).apply(instance, FabricFlammableBlockTagInteraction::new));

    @Override
    public void register(TagKey<Block> target) {
        FlammableBlockRegistry.getDefaultInstance().add(target, burn, spread);
    }

    @Override
    public InteractionType<TagKey<Block>, ?> getType() {
        return BlockTagInteractionTypes.FABRIC_FLAMMABLE;
    }
}
