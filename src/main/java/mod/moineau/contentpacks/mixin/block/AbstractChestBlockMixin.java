package mod.moineau.contentpacks.mixin.block;

import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.AbstractChestBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(AbstractChestBlock.class)
public abstract class AbstractChestBlockMixin implements CustomTextureProvider {
    @Unique
    private @Nullable Identifier contentpacks$customTexture;

    @Unique
    @Override
    public Optional<Identifier> contentpacks$getCustomTexture() {
        return Optional.ofNullable(contentpacks$customTexture);
    }

    @Unique
    @Override
    public void contentpacks$setCustomTexture(@Nullable Identifier id) {
        this.contentpacks$customTexture = id;
        CustomTextureProvider.CHESTS.add(this);
    }
}
