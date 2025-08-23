package mod.moineau.contentpacks.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Function;

public interface CustomTextureProvider {
    Optional<Identifier> contentpacks$getCustomTexture();

    void contentpacks$setCustomTexture(Identifier id);

    default boolean contentpacks$hasCustomTexture() {
        return contentpacks$getCustomTexture().isPresent();
    }

    static <O> Optional<Identifier> getCustomTexture(O object) {
        return object instanceof CustomTextureProvider textured ? textured.contentpacks$getCustomTexture() : Optional.empty();
    }

    static <O> void setCustomTexture(O object, Identifier id) {
        if (object instanceof CustomTextureProvider textured) {
            textured.contentpacks$setCustomTexture(id);
        };
    }

    static <O> boolean hasCustomTexture(O object) {
        return object instanceof CustomTextureProvider textured && textured.contentpacks$hasCustomTexture();
    }

    static <T> MapCodec<T> createCodec(MapCodec<T> codec) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                codec.forGetter(Function.identity()),
                Identifier.CODEC.optionalFieldOf("texture").forGetter(CustomTextureProvider::getCustomTexture)
        ).apply(instance, (object, optional) -> {
            optional.ifPresent(texture -> setCustomTexture(object, texture));
            return object;
        }));
    }
}
