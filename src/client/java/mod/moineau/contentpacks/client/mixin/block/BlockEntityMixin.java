package mod.moineau.contentpacks.client.mixin.block;

import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements CustomTextureProvider<BlockEntity> {

}
