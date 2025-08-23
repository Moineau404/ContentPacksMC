package mod.moineau.contentpacks.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.block.ContentBlockSettings;
import mod.moineau.contentpacks.block.statefunction.ConstantBlockStateFunction;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;

// TODO Copy & copy shallow
@Mixin(AbstractBlock.Settings.class)
public abstract class AbstractBlockSettingsMixin implements ContentBlockSettings {
    @Shadow
    public Function<BlockState, MapColor> mapColorProvider = DEFAULT_MAP_COLOR_PROVIDER;

    @Shadow
    public ToIntFunction<BlockState> luminance = DEFAULT_LUMINANCE_PROVIDER;

    @Shadow
    public AbstractBlock.TypedContextPredicate<EntityType<?>> allowsSpawningPredicate = DEFAULT_ALLOWS_SWPANING_PREDICATE;

    @Shadow
    public AbstractBlock.ContextPredicate solidBlockPredicate = DEFAULT_SOLID_BLOCK_PREDICATE;

    @Shadow
    public AbstractBlock.ContextPredicate suffocationPredicate = DEFAULT_SUFFOCATION_PREDICATE;

    @Shadow
    public AbstractBlock.ContextPredicate blockVisionPredicate = DEFAULT_BLOCK_VISION_PREDICATE;

    @Shadow
    public AbstractBlock.ContextPredicate postProcessPredicate = DEFAULT_POST_PROCESS_PREDICATE;

    @Shadow
    public AbstractBlock.ContextPredicate emissiveLightingPredicate = DEFAULT_EMISIVE_RENDERING_PREDICATE;

    @Unique
    public @Nullable Block contentpacks$copy;

    @Unique
    public boolean contentpacks$copyShallow;

    @Unique
    public @Nullable Optional<RegistryKey<LootTable>> contentpacks$lootTableOverride;

    @Unique
    public @Nullable String contentpacks$translationKeyOverride;

    @Unique
    public AbstractBlock.OffsetType contentpacks$offsetType = AbstractBlock.OffsetType.NONE;

    @Inject(method = "copy", at = @At(value = "TAIL"))
    private static void inject$copy(AbstractBlock block, CallbackInfoReturnable<AbstractBlock.Settings> cir, @Local(ordinal = 0) AbstractBlock.Settings settings) {
        ((AbstractBlockSettingsMixin) (Object) settings).contentpacks$copyShallow = false;
    }

    @Inject(method = "copyShallow", at = @At(value = "TAIL"))
    private static void inject$copyShallow(AbstractBlock block, CallbackInfoReturnable<AbstractBlock.Settings> cir, @Local(ordinal = 0) AbstractBlock.Settings settings) {
        if (block instanceof Block block1) {
            ((AbstractBlockSettingsMixin) (Object) settings).contentpacks$copy = block1;
        }
    }

    /**
     * @author
     * @reason
     */
    @Deprecated
    @Overwrite
    public AbstractBlock.Settings mapColor(DyeColor color) {
        this.mapColorProvider = new ConstantBlockStateFunction<>(color.getMapColor());
        return ((AbstractBlock.Settings) (Object) this);
    }

    /**
     * @author
     * @reason
     */
    @Deprecated
    @Overwrite
    public AbstractBlock.Settings mapColor(MapColor color) {
        this.mapColorProvider = new ConstantBlockStateFunction<>(color);
        return ((AbstractBlock.Settings) (Object) this);
    }

    @Inject(method = "overrideTranslationKey", at = @At(value = "TAIL"))
    public void inject$overrideTranslationKey(String translationKey, CallbackInfoReturnable<AbstractBlock.Settings> cir) {
        this.contentpacks$translationKeyOverride = translationKey;
    }

    @Inject(method = "offset", at = @At(value = "TAIL"))
    public void inject$offset(AbstractBlock.OffsetType offsetType, CallbackInfoReturnable<AbstractBlock.Settings> cir) {
        this.contentpacks$offsetType = offsetType;
    }

    @Unique
    @Override
    public @Nullable Block contentpacks$getCopy() {
        return contentpacks$copy;
    }

    @Unique
    @Override
    public boolean contentpacks$isCopyShallow() {
        return contentpacks$copyShallow;
    }

    @Unique
    @Override
    public @Nullable Optional<RegistryKey<LootTable>> contentpacks$getLootTableOverride() {
        return contentpacks$lootTableOverride;
    }

    @Unique
    @Override
    public @Nullable String contentpacks$getTranslationKeyOverride() {
        return contentpacks$translationKeyOverride;
    }

    @Unique
    @Override
    public AbstractBlock.OffsetType contentpacks$getOffsetType() {
        return contentpacks$offsetType;
    }
}