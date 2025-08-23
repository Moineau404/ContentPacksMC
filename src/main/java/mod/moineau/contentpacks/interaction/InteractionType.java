package mod.moineau.contentpacks.interaction;

import com.mojang.serialization.MapCodec;

public interface InteractionType<T, I extends Interaction<T>> {
    MapCodec<I> codec();
}
