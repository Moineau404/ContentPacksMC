package mod.moineau.contentpacks.interaction.item;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import java.util.Optional;

public record FabricCompostingChanceItemInteraction(float compostingChance) implements Interaction<Item> {
    public static final MapCodec<FabricCompostingChanceItemInteraction> CODEC = ExtraCodecs.POSITIVE_FLOAT
            .xmap(FabricCompostingChanceItemInteraction::new, FabricCompostingChanceItemInteraction::compostingChance).fieldOf("value");

    @Override
    public void register(Item target) {
        CompostableRegistry.INSTANCE.add(target, compostingChance);
    }

    @Override
    public InteractionType<Item, ?> getType() {
        return ItemInteractionTypes.FABRIC_COMPOSTING_CHANCE;
    }

    public static Optional<FabricCompostingChanceItemInteraction> get(Item item) {
        return Optional.of(CompostableRegistry.INSTANCE.get(item))
                .map(FabricCompostingChanceItemInteraction::new);
    }
}
