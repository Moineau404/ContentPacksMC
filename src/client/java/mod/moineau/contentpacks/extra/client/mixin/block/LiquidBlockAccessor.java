package mod.moineau.contentpacks.extra.client.mixin.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// TODO : Separate Liquid Block and Fluid (and put this mixin on main side)
@Mixin(LiquidBlock.class)
public interface LiquidBlockAccessor {
    @Accessor("fluid")
    FlowingFluid getFluid();
}
