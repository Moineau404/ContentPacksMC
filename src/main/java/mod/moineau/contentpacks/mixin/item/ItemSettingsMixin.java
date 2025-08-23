package mod.moineau.contentpacks.mixin.item;

import mod.moineau.contentpacks.item.ContentItemSettingsAccessor;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Store a copy of added components before validation (for serialization)
 */
@Mixin(Item.Settings.class)
public abstract class ItemSettingsMixin implements ContentItemSettingsAccessor {
    @Unique
    private ComponentMap contentpacks$nonValidatedComponents;

    @Unique
    private final ComponentMap.Builder contentpacks$nonValidatedComponentsBuilder = ComponentMap.builder();

    @Unique
    private @Nullable LazyRegistryEntryReference<Item> contentpacks$recipeRemainder;

    @Unique
    public @Nullable String contentpacks$translationKeyOverride;

    @Inject(method = "getValidatedComponents", at = @At(value = "HEAD"))
    public void inject$getValidatedComponents(Text name, Identifier modelId, CallbackInfoReturnable<ComponentMap> cir) {
        this.contentpacks$nonValidatedComponents = contentpacks$nonValidatedComponentsBuilder.build();
    }

    @Inject(method = "component", at = @At(value = "TAIL"))
    public <T> void inject$component(ComponentType<T> type, T value, CallbackInfoReturnable<Item.Settings> cir) {
        this.contentpacks$nonValidatedComponentsBuilder.add(type, value);
    }

    @Unique
    @Override
    public ComponentMap contentpacks$getNonValidatedComponents() {
        return contentpacks$nonValidatedComponents;
    }

    @Unique
    @Override
    public Optional<LazyRegistryEntryReference<Item>> contentpacks$getLazyRecipeRemainder() {
        return Optional.ofNullable(this.contentpacks$recipeRemainder);
    }

    @Unique
    @Override
    public void contentpacks$setLazyRecipeRemainder(LazyRegistryEntryReference<Item> recipeRemainder) {
        this.contentpacks$recipeRemainder = recipeRemainder;
    }

    @Unique
    @Override
    public @Nullable String contentpacks$getTranslationKeyOverride() {
        return this.contentpacks$translationKeyOverride;
    }
}
