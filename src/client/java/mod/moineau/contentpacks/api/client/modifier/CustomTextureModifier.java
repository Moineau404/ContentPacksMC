package mod.moineau.contentpacks.api.client.modifier;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.client.render.CustomTextureRegistry;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record CustomTextureModifier<T>(Identifier texture) implements Modifier<T> {
    public static <T> Codec<CustomTextureModifier<T>> createCodec() {
        return Identifier.CODEC.xmap(CustomTextureModifier::new, CustomTextureModifier::texture);
    }

    @Override
    public void apply(T target) {
        CustomTextureRegistry.register(target, texture);
    }

    public static <T> Optional<CustomTextureModifier<T>> get(T object) {
        return Optional.ofNullable(CustomTextureRegistry.get(object)).map(CustomTextureModifier::new);
    }
}
