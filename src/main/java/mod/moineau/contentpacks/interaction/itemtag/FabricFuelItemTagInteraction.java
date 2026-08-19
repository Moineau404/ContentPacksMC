package mod.moineau.contentpacks.interaction.itemtag;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record FabricFuelItemTagInteraction(float smeltTimeRatio) implements Interaction<TagKey<Item>> {
    public static final MapCodec<FabricFuelItemTagInteraction> CODEC = ExtraCodecs.POSITIVE_FLOAT
            .xmap(FabricFuelItemTagInteraction::new, FabricFuelItemTagInteraction::smeltTimeRatio).fieldOf("smelt_time_ratio");

    @Override
    public void register(TagKey<Item> tag) {
        FuelValueEvents.BUILD.register((builder, context) -> {
            builder.add(tag, (int) (context.baseSmeltTime() * smeltTimeRatio));
        });
    }

    @Override
    public InteractionType<TagKey<Item>, ?> getType() {
        return ItemTagInteractionTypes.FABRIC_FUEL;
    }
}
