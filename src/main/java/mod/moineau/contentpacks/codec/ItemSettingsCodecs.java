package mod.moineau.contentpacks.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.item.ContentItemSettingsAccessor;
import mod.moineau.contentpacks.mixin.item.ItemSettingsAccessor;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;

import java.util.Optional;
import java.util.function.Function;

public final class ItemSettingsCodecs {
    public static final Codec<Item.Settings> BASE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentMap.CODEC.optionalFieldOf("components", DataComponentTypes.DEFAULT_ITEM_COMPONENTS)
                    .forGetter(settings -> ((ContentItemSettingsAccessor) settings).contentpacks$getNonValidatedComponents()),
            Codec.either(Item.ENTRY_CODEC, RegistryKey.createCodec(RegistryKeys.ITEM))
                    .optionalFieldOf("crafting_remaining_item")
                    .forGetter(settings -> Optional.ofNullable(((ItemSettingsAccessor) settings).getRecipeRemainder()).map(Item::getRegistryEntry).map(Either::left)),
            FeatureFlags.CODEC.optionalFieldOf("required_features", FeatureFlags.VANILLA_FEATURES)
                    .forGetter(settings -> settings.requiredFeatures),
            Codec.STRING.optionalFieldOf("description")
                    .forGetter(settings -> Optional.ofNullable(((ContentItemSettingsAccessor) settings).contentpacks$getTranslationKeyOverride()))
    ).apply(instance, ItemSettingsCodecs::create));
    public static final MapCodec<Item.Settings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BASE_CODEC.optionalFieldOf("properties")
                    .xmap(optional -> optional.orElseGet(Item.Settings::new), Optional::of).forGetter(Function.identity()),
            VanillaCodecs.fillInjectRegistryKey(RegistryKeys.ITEM)
    ).apply(instance, Item.Settings::registryKey));

    private static Item.Settings create(
            ComponentMap components,
            Optional<Either<RegistryEntry<Item>, RegistryKey<Item>>> recipeRemainder,
            FeatureSet requiredFeatures,
            Optional<String> translationKey
    ) {
        Item.Settings settings = new Item.Settings();
        settings.components.addAll(components);
        recipeRemainder.ifPresent(either -> either
                .mapBoth(RegistryEntry::value, LazyRegistryEntryReference::new)
                .ifLeft(settings::recipeRemainder)
                .ifRight(((ContentItemSettingsAccessor) settings)::contentpacks$setLazyRecipeRemainder));
        settings.requiredFeatures = requiredFeatures;
        translationKey.ifPresent(settings::translationKey);
        return settings;
    }
}