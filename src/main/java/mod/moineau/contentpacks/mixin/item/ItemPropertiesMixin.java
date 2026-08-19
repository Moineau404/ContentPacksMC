package mod.moineau.contentpacks.mixin.item;

import mod.moineau.contentpacks.item.ContentItemPropertiesAccessor;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Store a copy of added components before validation (for serialization)
 */
@Mixin(Item.Properties.class)
public abstract class ItemPropertiesMixin implements ContentItemPropertiesAccessor {
    @Unique
    private DataComponentMap contentpacks$unfinalizedComponents;

    @Unique
    private final DataComponentMap.Builder contentpacks$unfinalizedInitializer = DataComponentMap.builder();

    @Unique
    public @Nullable String contentpacks$descriptionIdOverride;

    @Inject(method = "finalizeInitializer", at = @At(value = "HEAD"))
    public void inject$finalizeInitializer(Component name, Identifier model, CallbackInfoReturnable<DataComponentMap> cir) {
        this.contentpacks$unfinalizedComponents = contentpacks$unfinalizedInitializer.build();
    }

    @Inject(method = "component", at = @At(value = "TAIL"))
    public <T> void inject$component(DataComponentType<T> type, T value, CallbackInfoReturnable<Item.Properties> cir) {
        this.contentpacks$unfinalizedInitializer.set(type, value);
    }

    @Unique
    @Override
    public DataComponentMap contentpacks$getUnfinalizedComponents() {
        return contentpacks$unfinalizedComponents;
    }

    @Unique
    @Override
    public @Nullable String contentpacks$getDescriptionIdOverride() {
        return this.contentpacks$descriptionIdOverride;
    }
}
