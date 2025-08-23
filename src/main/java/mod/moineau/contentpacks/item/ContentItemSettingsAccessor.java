package mod.moineau.contentpacks.item;

import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ContentItemSettingsAccessor {
    ComponentMap contentpacks$getNonValidatedComponents();

    Optional<LazyRegistryEntryReference<Item>> contentpacks$getLazyRecipeRemainder();

    void contentpacks$setLazyRecipeRemainder(LazyRegistryEntryReference<Item> recipeRemainder);

    @Nullable String contentpacks$getTranslationKeyOverride();
}
