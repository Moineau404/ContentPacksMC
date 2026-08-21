package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.item.ContentItemPropertiesAccessor;
import mod.moineau.contentpacks.mixin.item.ItemPropertiesAccessor;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.Optional;
import java.util.function.Function;

public final class ItemPropertiesCodecs {
    public static final Codec<Item.Properties> BASE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DataComponentMap.CODEC.optionalFieldOf("components", DataComponents.COMMON_ITEM_COMPONENTS)
                    .forGetter(properties -> ((ContentItemPropertiesAccessor) properties).contentpacks$getUnfinalizedComponents()),
            ItemStackTemplate.CODEC.optionalFieldOf("crafting_remaining_item")
                    .forGetter(properties -> Optional.ofNullable(((ItemPropertiesAccessor) properties).getCraftingRemainingItem())),
            FeatureFlags.CODEC.optionalFieldOf("required_features", FeatureFlags.VANILLA_SET)
                    .forGetter(properties -> properties.requiredFeatures),
            Codec.STRING.optionalFieldOf("description")
                    .forGetter(properties -> Optional.ofNullable(((ContentItemPropertiesAccessor) properties).contentpacks$getDescriptionIdOverride()))
    ).apply(instance, ItemPropertiesCodecs::create));
    public static final MapCodec<Item.Properties> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BASE_CODEC.optionalFieldOf("properties")
                    .xmap(optional -> optional.orElseGet(Item.Properties::new), Optional::of).forGetter(Function.identity()),
            VanillaCodecs.fillInjectRegistryKey(Registries.ITEM)
    ).apply(instance, Item.Properties::setId));

    private static Item.Properties create(
            DataComponentMap components,
            Optional<ItemStackTemplate> craftingRemainingItem,
            FeatureFlagSet requiredFeatures,
            Optional<String> descriptionId
    ) {
        Item.Properties properties = new Item.Properties();
        components.stream().forEachOrdered(component -> properties.component((DataComponentType) component.type(), component.value()));
        craftingRemainingItem.ifPresent(properties::craftRemainder);
        properties.requiredFeatures = requiredFeatures;
        descriptionId.ifPresent(properties::overrideDescription);
        return properties;
    }
}