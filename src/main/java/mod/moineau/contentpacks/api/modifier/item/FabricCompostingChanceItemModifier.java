package mod.moineau.contentpacks.api.modifier.item;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

import java.util.Optional;

public record FabricCompostingChanceItemModifier(float compostingChance) implements Modifier<Item> {
    public static final Codec<FabricCompostingChanceItemModifier> CODEC = ExtraCodecs.POSITIVE_FLOAT
            .xmap(FabricCompostingChanceItemModifier::new, FabricCompostingChanceItemModifier::compostingChance);

    @Override
    public void apply(Item target) {
        CompostableRegistry.INSTANCE.add(target, compostingChance);
    }

    public static Optional<FabricCompostingChanceItemModifier> get(Item item) {
        return Optional.of(CompostableRegistry.INSTANCE.get(item)).map(FabricCompostingChanceItemModifier::new);
    }
}
