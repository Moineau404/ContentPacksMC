package mod.moineau.contentpacks.mixin.item;

import mod.moineau.contentpacks.item.ContentItemAccessor;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Store item settings instead of just storing component map
 */
@Mixin(Item.class)
public class ItemMixin implements ContentItemAccessor {

    @Unique
    private Item.Properties contentpacks$properties;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void inject$init(Item.Properties properties, CallbackInfo ci) {
        this.contentpacks$properties = properties;
    }

    @Override
    public Item.Properties contentpacks$getProperties() {
        return this.contentpacks$properties;
    }
}
