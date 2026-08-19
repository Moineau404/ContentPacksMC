package mod.moineau.contentpacks.mixin.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.Properties.class)
public interface ItemPropertiesAccessor {
    @Accessor("craftingRemainingItem")
    @Nullable ItemStackTemplate getCraftingRemainingItem();
}
