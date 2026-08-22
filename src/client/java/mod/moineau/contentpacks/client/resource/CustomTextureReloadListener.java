package mod.moineau.contentpacks.client.resource;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class CustomTextureReloadListener<T> extends DependentReloadListener<CustomTextureProvider<T>, Optional<Identifier>> {
    private static final Codec<Optional<Identifier>> CODEC = Identifier.CODEC.optionalFieldOf("texture").codec();
    private final Registry<T> registry;

    public CustomTextureReloadListener(String directory, Registry<T> registry) {
        super(directory, CODEC);
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected @Nullable CustomTextureProvider<T> getDependence(Identifier id) {
        T owner = registry.getValue(id);
        if (owner instanceof CustomTextureProvider<?> provider) {
            return (CustomTextureProvider<T>) provider;
        }
        return null;
    }

    @Override
    protected void loadEntry(CustomTextureProvider<T> provider, Optional<Identifier> texture, Identifier id) {
        provider.contentpacks$setTexture(texture);
    }
}