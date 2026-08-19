package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.world.level.block.ChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(ChestBlock.class)
public class ChestBlockMixin {
    @Shadow
    public static final MapCodec<ChestBlock> CODEC = CustomTextureProvider.createCodec(ChestBlock.CODEC);
}
