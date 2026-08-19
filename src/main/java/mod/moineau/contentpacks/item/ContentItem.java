package mod.moineau.contentpacks.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.Item;

/**
 * Implement this in your {@link Item} class to make it content-driven.
 * If you want to make an existing item content-driven, use Mixin interface injection or {@link ItemTypes#register(Class, MapCodec)}.
 */
public interface ContentItem {
    MapCodec<? extends Item> getCodec();
}
