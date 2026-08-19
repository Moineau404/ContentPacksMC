package mod.moineau.contentpacks.interaction.itemtag;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public final class ItemTagInteractionTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<TagKey<Item>, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final InteractionType<TagKey<Item>, FabricFuelItemTagInteraction> FABRIC_FUEL = () -> FabricFuelItemTagInteraction.CODEC;
    public static final InteractionType<TagKey<Item>, FabricCompostingChanceItemTagInteraction> FABRIC_COMPOSTING_CHANCE = () -> FabricCompostingChanceItemTagInteraction.CODEC;

    public static void bootStrap() {
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "fuel"), FABRIC_FUEL);
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "composting_chance"), FABRIC_COMPOSTING_CHANCE);
    }
}
