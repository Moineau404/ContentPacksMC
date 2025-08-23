package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.CustomTextureProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(ChestBlock.class)
public class ChestBlockMixin {
    @Shadow
    public static final MapCodec<ChestBlock> CODEC = CustomTextureProvider.createCodec(AbstractBlock.createCodec((settings) -> new ChestBlock(() -> BlockEntityType.CHEST, settings)));
}
