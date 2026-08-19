package mod.moineau.contentpacks.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface CustomTextureProvider {
    List<CustomTextureProvider> CHESTS = new ArrayList<>();

    Optional<Identifier> contentpacks$getCustomTexture();

    void contentpacks$setCustomTexture(@Nullable Identifier id);

    static <O> Optional<Identifier> getCustomTexture(O object) {
        return object instanceof CustomTextureProvider textured ? textured.contentpacks$getCustomTexture() : Optional.empty();
    }

    static <O> void setCustomTexture(O object, @Nullable Identifier id) {
        if (object instanceof CustomTextureProvider textured) {
            textured.contentpacks$setCustomTexture(id);
        }
    }

    static <T> MapCodec<T> createCodec(MapCodec<T> codec) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                codec.forGetter(Function.identity()),
                Identifier.CODEC.optionalFieldOf("texture").forGetter(CustomTextureProvider::getCustomTexture)
        ).apply(instance, (object, textureId) -> {
            textureId.ifPresent(texture -> setCustomTexture(object, texture));
            return object;
        }));
    }
}
