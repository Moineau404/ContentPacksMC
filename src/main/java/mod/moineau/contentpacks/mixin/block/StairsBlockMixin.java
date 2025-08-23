package mod.moineau.contentpacks.mixin.block;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.Workaround;
import mod.moineau.contentpacks.resource.ContentManager;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO Documentation for stairs because of specific lazy codec
@Workaround
@Mixin(StairsBlock.class)
public class StairsBlockMixin {
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

    /**
     * Overly complicated workaround to make stairs serializable when they reference blocks that are not loaded yet... -_-
     */
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
}
