package mod.moineau.contentpacks.interaction.item;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public final class ItemInteractionTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<Item, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final InteractionType<Item, FabricFuelItemInteraction> FABRIC_FUEL = () -> FabricFuelItemInteraction.CODEC;
    public static final InteractionType<Item, FabricCompostingChanceItemInteraction> FABRIC_COMPOSTING_CHANCE = () -> FabricCompostingChanceItemInteraction.CODEC;

    public static void bootStrap() {
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "fuel"), FABRIC_FUEL);
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "composting_chance"), FABRIC_COMPOSTING_CHANCE);
    }
}
