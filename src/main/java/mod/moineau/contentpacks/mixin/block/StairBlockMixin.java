package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StairBlock.class)
public class StairBlockMixin extends Block {
    @Shadow
    public static final MapCodec<StairBlock> CODEC = simpleCodec(settings -> new StairBlock(null, settings));

    @Mutable
    @Shadow
    @Final
    private Block base;

    @Mutable
    @Shadow
    @Final
    protected BlockState baseState;

    /**
     * @author Moineau
     * @reason Fix useless thing that prevent stairs from being correctly deserialized when baseState ("base_state") is
     * (of a block that may not be loaded yet) and all just to get that one block's resistance.
     */
    @Overwrite
    public float getExplosionResistance() {
        return this.properties.explosionResistance;
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"))
    private Block redirect$init_baseState(BlockState baseState) {
        return baseState != null ? baseState.getBlock() : this;
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void inject$init(BlockState baseState, Properties properties, CallbackInfo ci) {
        if (baseState != null) {
            this.properties.explosionResistance = baseState.getBlock().getExplosionResistance();
        } else {
            this.baseState = this.defaultBlockState();
        }
    }

    public StairBlockMixin(BlockBehaviour.Properties settings) {
        super(settings);
    }
}