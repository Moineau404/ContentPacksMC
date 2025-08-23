package mod.moineau.contentpacks.mixin.item;

import mod.moineau.contentpacks.item.ContentItemAccessor;
import mod.moineau.contentpacks.item.ContentItemSettingsAccessor;
import mod.moineau.contentpacks.resource.ContentManager;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Store item settings instead of just storing component map
 */
@Mixin(Item.class)
public class ItemMixin implements ContentItemAccessor {
    @Mutable
    @Final
    @Shadow
    private @Nullable Item recipeRemainder;

    @Unique
    private Item.Settings contentpacks$settings;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void inject$init(Item.Settings settings, CallbackInfo ci) {
        ((ContentItemSettingsAccessor) settings).contentpacks$getLazyRecipeRemainder()
                .ifPresent(recipeRemainder -> ContentManager.ITEMS.registerListener(
                        (entries) -> recipeRemainder.resolveValue(Registries.ITEM).ifPresent(item -> this.recipeRemainder = item)));
        this.contentpacks$settings = settings;
    }

    @Override
    public Item.Settings contentpacks$getSettings() {
        return this.contentpacks$settings;
    }
}
