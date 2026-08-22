package mod.moineau.contentpacks.mixin.block;

import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.world.level.block.BaseEntityBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaseEntityBlock.class)
public abstract class BaseEntityBlockMixin implements CustomTextureProvider<BaseEntityBlock> {

}
