package mod.moineau.contentpacks.api.client.mixin.block;

import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntityRenderState.class)
public class BlockEntityRenderStateMixin implements CustomTextureProvider<BlockEntityRenderState> {

}
