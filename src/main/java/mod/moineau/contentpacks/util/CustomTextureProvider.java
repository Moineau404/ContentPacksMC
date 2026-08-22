package mod.moineau.contentpacks.util;

import com.google.common.base.Functions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.AbstractChestBlock;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface CustomTextureProvider<T> {
    Map<Object, Function<Object, Identifier>> TEXTURES = new Reference2ObjectOpenHashMap<>();
    List<CustomTextureProvider<? extends AbstractChestBlock<?>>> MULTI_CHEST_BLOCKS = new ArrayList<>();
    List<CustomTextureProvider<? extends AbstractChestBlock<?>>> SINGLE_CHEST_BLOCKS = new ArrayList<>();

    default @Nullable Identifier contentpacks$getTexture() {
        Function<Object, Identifier> provider = TEXTURES.get(this);
        if (provider != null) {
            return provider.apply(this);
        }
        return null;
    }

    default Optional<Identifier> contentpacks$getOptionalTexture() {
        return Optional.ofNullable(contentpacks$getTexture());
    }

    default void contentpacks$setTexture(@Nullable Identifier id) {
        if (id != null) {
            TEXTURES.put(this, Functions.constant(id));
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    default void contentpacks$setTexture(Optional<Identifier> optional) {
        optional.ifPresent(this::contentpacks$setTexture);
    }

    default T getOwner() {
        return (T) this;
    }

    @SuppressWarnings("rawtypes")
    static @Nullable Identifier getTexture(Object object) {
        return object instanceof CustomTextureProvider textured ? textured.contentpacks$getTexture() : null;
    }

    static Optional<Identifier> getOptionalTexture(Object object) {
        return Optional.ofNullable(getTexture(object));
    }

    @SuppressWarnings("rawtypes")
    static void setTexture(Object object, @Nullable Identifier id) {
        if (object instanceof CustomTextureProvider textured) {
            textured.contentpacks$setTexture(id);
        }
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "rawtypes", "unchecked"})
    static void setTexture(Object object, Optional<Identifier> optional) {
        if (object instanceof CustomTextureProvider textured) {
            textured.contentpacks$setTexture(optional);
        }
    }

    static void passTexture(Object donor, Object recipient) {
        CustomTextureProvider.setTexture(recipient, CustomTextureProvider.getTexture(donor));
    }

    static <T> MapCodec<T> createCodec(MapCodec<T> codec) {
        return createCodec(codec, _ -> {});
    }

    // TODO : Use "dependant" codec
    @SuppressWarnings({"rawtypes", "unchecked"})
    static <T> MapCodec<T> createCodec(MapCodec<T> codec, Consumer<CustomTextureProvider<T>> listener) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                codec.forGetter(Function.identity()),
                Identifier.CODEC.optionalFieldOf("texture").forGetter(CustomTextureProvider::getOptionalTexture)
        ).apply(instance, (object, optional) -> {
            optional.ifPresent(texture -> {
                if (object instanceof CustomTextureProvider textured) {
                    textured.contentpacks$setTexture(texture);
                    listener.accept(textured);
                }
            });
            return object;
        }));
    }
}
