package mod.moineau.contentpacks.interaction.item;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class ItemInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<Item, ?>> ID_MAPPER = new Codecs.IdMapper<>();
    public static final InteractionType<Item, FabricFuelItemInteraction> FABRIC_FUEL = () -> FabricFuelItemInteraction.CODEC;
    public static final InteractionType<Item, FabricCompostingChanceItemInteraction> FABRIC_COMPOSTING_CHANCE = () -> FabricCompostingChanceItemInteraction.CODEC;

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.of("fabric", "fuel"), FABRIC_FUEL);
        ID_MAPPER.put(Identifier.of("fabric", "composting_chance"), FABRIC_COMPOSTING_CHANCE);
    }
}
