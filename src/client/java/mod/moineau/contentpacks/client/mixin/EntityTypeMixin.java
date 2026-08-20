package mod.moineau.contentpacks.client.mixin;

import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(EntityType.class)
public class EntityTypeMixin implements CustomTextureProvider {
    @Unique
    private @Nullable Identifier contentpacks$customTexture;

    @Override
    public Optional<Identifier> contentpacks$getCustomTexture() {
        return Optional.ofNullable(this.contentpacks$customTexture);
    }

    @Override
    public void contentpacks$setCustomTexture(@Nullable Identifier id) {
        this.contentpacks$customTexture = id;
    }
}
