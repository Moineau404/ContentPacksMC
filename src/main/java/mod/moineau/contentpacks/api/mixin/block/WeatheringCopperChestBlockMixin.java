package mod.moineau.contentpacks.api.mixin.block;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.world.level.block.WeatheringCopperChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(WeatheringCopperChestBlock.class)
public class WeatheringCopperChestBlockMixin {
    @Shadow
    public static final MapCodec<WeatheringCopperChestBlock> CODEC = CustomTextureProvider.createCodec(WeatheringCopperChestBlock.CODEC, CustomTextureProvider.MULTI_CHEST_BLOCKS::add);
}
