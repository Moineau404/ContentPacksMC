package mod.moineau.contentpacks.render.block.tint;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColorProvider;

@Environment(EnvType.CLIENT)
public interface BlockTintSource extends BlockColorProvider {
    ConstantBlockTintSource NO_TINT = new ConstantBlockTintSource(-1);

    MapCodec<? extends BlockTintSource> getCodec();
}