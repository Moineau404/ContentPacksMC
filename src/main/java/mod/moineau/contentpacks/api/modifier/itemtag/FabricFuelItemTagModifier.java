package mod.moineau.contentpacks.api.modifier.itemtag;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record FabricFuelItemTagModifier(float factor) implements Modifier<TagKey<Item>> {
    public static final Codec<FabricFuelItemTagModifier> CODEC = ExtraCodecs.POSITIVE_FLOAT
            .xmap(FabricFuelItemTagModifier::new, FabricFuelItemTagModifier::factor);

    @Override
    public void apply(TagKey<Item> tag) {
        FuelValueEvents.BUILD.register((builder, context) -> {
            builder.add(tag, (int) (context.baseSmeltTime() * factor));
        });
    }
}
