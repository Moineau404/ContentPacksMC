package mod.moineau.contentpacks.api.modifier.item;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record FabricFuelItemModifier(float factor) implements Modifier<Item> {
    public static final Codec<FabricFuelItemModifier> CODEC = ExtraCodecs.POSITIVE_FLOAT
            .xmap(FabricFuelItemModifier::new, FabricFuelItemModifier::factor);

    @Override
    public void apply(Item target) {
        FuelValueEvents.BUILD.register((builder, context) -> {
            builder.add(target, (int) (context.baseSmeltTime() * factor));
        });
    }
}
