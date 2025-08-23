package mod.moineau.contentpacks.interaction.item;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.item.Item;
import net.minecraft.util.dynamic.Codecs;

public record FabricFuelItemInteraction(float smeltTimeFactor) implements Interaction<Item> {
    public static final MapCodec<FabricFuelItemInteraction> CODEC = Codecs.POSITIVE_FLOAT
            .xmap(FabricFuelItemInteraction::new, FabricFuelItemInteraction::smeltTimeFactor).fieldOf("smelt_time_factor");

    @Override
    public void register(Item target) {
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(target, (int) (context.baseSmeltTime() * smeltTimeFactor));
        });
    }

    @Override
    public InteractionType<Item, ?> getType() {
        return ItemInteractionTypes.FABRIC_FUEL;
    }
}
