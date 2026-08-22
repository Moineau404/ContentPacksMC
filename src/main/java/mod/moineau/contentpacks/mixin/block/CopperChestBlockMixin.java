package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.world.level.block.CopperChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(CopperChestBlock.class)
public class CopperChestBlockMixin {
    @Shadow
    public static final MapCodec<CopperChestBlock> CODEC = CustomTextureProvider.createCodec(CopperChestBlock.CODEC, CustomTextureProvider.MULTI_CHEST_BLOCKS::add);
}
