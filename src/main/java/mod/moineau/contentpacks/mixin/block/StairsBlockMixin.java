package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StairsBlock.class)
public class StairsBlockMixin extends Block {
    @Shadow
    public static final MapCodec<StairsBlock> CODEC = createCodec(settings -> new StairsBlock(null, settings));

    @Mutable
    @Shadow
    @Final
    private Block baseBlock;

    @Mutable
    @Shadow
    @Final
    protected BlockState baseBlockState;

    /**
     * @author Moineau
     * @reason Fix useless thing that prevent stairs from being correctly deserialized when baseBlockState ("base_state") is
     * (of a block that may not be loaded yet) and all just to get that one block's resistance.
     */
    @Overwrite
    public float getBlastResistance() {
        return this.settings.resistance;
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
    private Block redirect$init_baseBlockState(BlockState baseBlockState) {
        return baseBlockState != null ? baseBlockState.getBlock() : this;
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void inject$init(BlockState baseBlockState, Settings settings, CallbackInfo ci) {
        if (baseBlockState != null) {
            this.settings.resistance = baseBlockState.getBlock().getBlastResistance();
        } else {
            this.baseBlockState = this.getDefaultState();
        }
    }

    public StairsBlockMixin(AbstractBlock.Settings settings) {
        super(settings);
    }
}
/*

    @Mutable
    @Shadow
    @Final
    private Block baseBlock;

    @Mutable
    @Shadow
    @Final
    public static MapCodec<StairsBlock> CODEC;

    @Mutable
    @Shadow
    @Final
    protected BlockState baseBlockState;

    @Unique
    private LazyRegistryEntryReference<Block> lazyBaseBlock;

    @Unique
    private static MapCodec<StairsBlock> BASE_CODEC;

     * Overly complicated workaround to make stairs serializable when they reference blocks that are not loaded yet... -_-

@SuppressWarnings({"DataFlowIssue", "deprecation"})
@Unique
private static final MapCodec<StairsBlock> LAZY_CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                        Codec.mapEither(BlockState.CODEC.fieldOf("base_state"), RegistryKey.createCodec(RegistryKeys.BLOCK).fieldOf("base_block"))
                                .forGetter(block -> {
                                    if (((StairsBlockMixin) (Object) block).lazyBaseBlock == null) {
                                        return Either.left(((StairsBlockMixin) (Object) block).baseBlockState);
                                    }
                                    return Either.right(((StairsBlockMixin) (Object) block).baseBlock.getRegistryEntry().registryKey());
                                }),
                        AbstractBlockAccessor.invoke$createSettingsCodec())
                .apply(instance, (either, settings) -> either.map(
                        baseBlockState -> new StairsBlock(baseBlockState, settings),
                        registryKey -> {
                            StairsBlock block = new StairsBlock(Blocks.AIR.getDefaultState(), settings);
                            ((StairsBlockMixin) (Object) block).lazyBaseBlock = new LazyRegistryEntryReference<>(registryKey);
                            ContentManager.BLOCKS.registerListener(entries -> {
                                ((StairsBlockMixin) (Object) block).lazyBaseBlock.resolveValue(Registries.BLOCK).ifPresent(baseBlock -> {
                                    ((StairsBlockMixin) (Object) block).baseBlock = baseBlock;
                                    ((StairsBlockMixin) (Object) block).baseBlockState = baseBlock.getDefaultState();
                                });
                            });
                            return block;
                        }
                ))
);

        @Inject(method = "<clinit>", at = @At(value = "TAIL"))
        private static void inject$clinit(CallbackInfo ci) {
            BASE_CODEC = CODEC;
            CODEC = LAZY_CODEC;
        }
 */