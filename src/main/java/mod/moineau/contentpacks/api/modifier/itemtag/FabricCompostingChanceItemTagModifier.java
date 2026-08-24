package mod.moineau.contentpacks.api.modifier.itemtag;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record FabricCompostingChanceItemTagModifier(float compostingChance) implements Modifier<TagKey<Item>> {
    public static final Codec<FabricCompostingChanceItemTagModifier> CODEC = ExtraCodecs.POSITIVE_FLOAT
            .xmap(FabricCompostingChanceItemTagModifier::new, FabricCompostingChanceItemTagModifier::compostingChance);

    @Override
    public void apply(TagKey<Item> tag) {
        CompostableRegistry.INSTANCE.add(tag, compostingChance);
    }
}
