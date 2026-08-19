package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.world.level.block.EnderChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(EnderChestBlock.class)
public class EnderChestBlockMixin {
    @Shadow
    public static final MapCodec<EnderChestBlock> CODEC = CustomTextureProvider.createCodec(EnderChestBlock.CODEC);
}
