package mod.moineau.contentpacks.interaction.itemtag;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record FabricCompostingChanceItemTagInteraction(float compostingChance) implements Interaction<TagKey<Item>> {
    public static final MapCodec<FabricCompostingChanceItemTagInteraction> CODEC = ExtraCodecs.POSITIVE_FLOAT
            .xmap(FabricCompostingChanceItemTagInteraction::new, FabricCompostingChanceItemTagInteraction::compostingChance).fieldOf("value");

    @Override
    public void register(TagKey<Item> tag) {
        CompostableRegistry.INSTANCE.add(tag, compostingChance);
    }

    @Override
    public InteractionType<TagKey<Item>, ?> getType() {
        return ItemTagInteractionTypes.FABRIC_COMPOSTING_CHANCE;
    }
}
