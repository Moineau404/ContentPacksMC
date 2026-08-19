package mod.moineau.contentpacks.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.block.ContentBlockProperties;
import mod.moineau.contentpacks.block.MapColorProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
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
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

@Mixin(BlockBehaviour.Properties.class)
public abstract class BlockBehaviourPropertiesMixin implements ContentBlockProperties {
    @Shadow
    public Function<BlockState, MapColor> mapColor = DEFAULT_MAP_COLOR;

    @Shadow
    public ToIntFunction<BlockState> lightEmission = DEFAULT_LIGHT_EMISSION;

    @Shadow
    public BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn = DEFAULT_IS_VALID_SPAWN;

    @Shadow
    public BlockBehaviour.StatePredicate isRedstoneConductor = DEFAULT_IS_REDSTONE_CONDUCTOR;

    @Shadow
    public BlockBehaviour.StatePredicate isSuffocating = DEFAULT_IS_SUFFOCATING;

    @Shadow
    public BlockBehaviour.StatePredicate isViewBlocking = DEFAULT_IS_VIEW_BLOCKING;

    @Shadow
    public BlockBehaviour.PostProcess postProcess = DEFAULT_POST_PROCESS;

    @Shadow
    public Predicate<BlockState> emissiveRendering = DEFAULT_EMISSIVE_RENDERING;

    @Unique
    public @Nullable Block contentpacks$fullCopyOf;

    @Unique
    public boolean contentpacks$legacyCopy;

    @Unique
    public @Nullable Optional<ResourceKey<LootTable>> contentpacks$lootTableOverride;

    @Unique
    public @Nullable String contentpacks$descriptionIdOverride;

    @Unique
    public BlockBehaviour.OffsetType contentpacks$offsetType = BlockBehaviour.OffsetType.NONE;

    @Inject(method = "ofFullCopy", at = @At(value = "TAIL"))
    private static void inject$ofFullCopy(BlockBehaviour block, CallbackInfoReturnable<BlockBehaviour.Properties> cir, @Local(name = "copyTo") BlockBehaviour.Properties copyTo) {
        ((BlockBehaviourPropertiesMixin) (Object) copyTo).contentpacks$legacyCopy = false;
    }

    @Inject(method = "ofLegacyCopy", at = @At(value = "TAIL"))
    private static void inject$ofLegacyCopy(BlockBehaviour block, CallbackInfoReturnable<BlockBehaviour.Properties> cir, @Local(name = "copyTo") BlockBehaviour.Properties copyTo) {
        if (block instanceof Block block1) {
            ((BlockBehaviourPropertiesMixin) (Object) copyTo).contentpacks$fullCopyOf = block1;
        }
    }

    /**
     * @author Moineau
     * @reason Serializable map color providers
     */
    @Overwrite
    public BlockBehaviour.Properties mapColor(DyeColor color) {
        this.mapColor = MapColorProvider.of(color.getMapColor());
        return ((BlockBehaviour.Properties) (Object) this);
    }

    /**
     * @author Moineau
     * @reason Serializable map color providers
     */
    @Overwrite
    public BlockBehaviour.Properties mapColor(MapColor color) {
        this.mapColor = MapColorProvider.of(color);
        return ((BlockBehaviour.Properties) (Object) this);
    }

    @Inject(method = "overrideDescription", at = @At(value = "TAIL"))
    public void inject$overrideDescription(String descriptionId, CallbackInfoReturnable<BlockBehaviour.Properties> cir) {
        this.contentpacks$descriptionIdOverride = descriptionId;
    }

    @Inject(method = "offsetType", at = @At(value = "TAIL"))
    public void inject$offsetType(BlockBehaviour.OffsetType offsetType, CallbackInfoReturnable<BlockBehaviour.Properties> cir) {
        this.contentpacks$offsetType = offsetType;
    }

    @Unique
    @Override
    public @Nullable Block contentpacks$getFullCopyOf() {
        return contentpacks$fullCopyOf;
    }

    @Unique
    @Override
    public boolean contentpacks$isLegacyCopy() {
        return contentpacks$legacyCopy;
    }

    @Unique
    @Override
    public @Nullable Optional<ResourceKey<LootTable>> contentpacks$getLootTableOverride() {
        return contentpacks$lootTableOverride;
    }

    @Unique
    @Override
    public @Nullable String contentpacks$getDescriptionIdOverride() {
        return contentpacks$descriptionIdOverride;
    }

    @Unique
    @Override
    public BlockBehaviour.OffsetType contentpacks$getOffsetType() {
        return contentpacks$offsetType;
    }
}