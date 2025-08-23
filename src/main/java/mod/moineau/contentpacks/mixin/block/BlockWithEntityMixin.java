package mod.moineau.contentpacks.mixin.block;

import mod.moineau.contentpacks.block.CustomTextureProvider;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(BlockWithEntity.class)
public abstract class BlockWithEntityMixin implements CustomTextureProvider {
    @Unique
    private Optional<Identifier> contentpacks$customTexture = Optional.empty();

    @Unique
    @Override
    public Optional<Identifier> contentpacks$getCustomTexture() {
        return contentpacks$customTexture;
    }

    @Unique
    @Override
    public void contentpacks$setCustomTexture(Identifier id) {
        this.contentpacks$customTexture = Optional.of(id);
    }
}
