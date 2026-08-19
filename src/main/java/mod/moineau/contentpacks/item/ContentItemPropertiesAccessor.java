package mod.moineau.contentpacks.item;

import net.minecraft.core.component.DataComponentMap;
import org.jetbrains.annotations.Nullable;

public interface ContentItemPropertiesAccessor {
    DataComponentMap contentpacks$getUnfinalizedComponents();

//    Optional<EitherHolder<Item>> contentpacks$getLazyRecipeRemainder();

//    void contentpacks$setLazyRecipeRemainder(EitherHolder<Item> recipeRemainder);

    @Nullable String contentpacks$getDescriptionIdOverride();
}
