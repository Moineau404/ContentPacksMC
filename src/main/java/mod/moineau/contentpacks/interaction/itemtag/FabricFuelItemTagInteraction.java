package mod.moineau.contentpacks.interaction.itemtag;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.dynamic.Codecs;

public record FabricFuelItemTagInteraction(float smeltTimeRatio) implements Interaction<TagKey<Item>> {
    public static final MapCodec<FabricFuelItemTagInteraction> CODEC = Codecs.POSITIVE_FLOAT
            .xmap(FabricFuelItemTagInteraction::new, FabricFuelItemTagInteraction::smeltTimeRatio).fieldOf("smelt_time_ratio");

    @Override
    public void register(TagKey<Item> tag) {
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(tag, (int) (context.baseSmeltTime() * smeltTimeRatio));
        });
    }

    @Override
    public InteractionType<TagKey<Item>, ?> getType() {
        return ItemTagInteractionTypes.FABRIC_FUEL;
    }
}
