package mod.moineau.contentpacks.interaction.itemtag;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.dynamic.Codecs;

public record FabricCompostingChanceItemTagInteraction(float compostingChance) implements Interaction<TagKey<Item>> {
    public static final MapCodec<FabricCompostingChanceItemTagInteraction> CODEC = Codecs.POSITIVE_FLOAT
            .xmap(FabricCompostingChanceItemTagInteraction::new, FabricCompostingChanceItemTagInteraction::compostingChance).fieldOf("value");

    @Override
    public void register(TagKey<Item> tag) {
        CompostingChanceRegistry.INSTANCE.add(tag, compostingChance);
    }

    @Override
    public InteractionType<TagKey<Item>, ?> getType() {
        return ItemTagInteractionTypes.FABRIC_COMPOSTING_CHANCE;
    }
}
