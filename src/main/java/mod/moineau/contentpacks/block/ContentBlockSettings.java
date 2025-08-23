package mod.moineau.contentpacks.block;

import mod.moineau.contentpacks.api.function.predicate.Comparator;
import mod.moineau.contentpacks.api.function.predicate.IntComparison;
import mod.moineau.contentpacks.block.contextpredicate.*;
import mod.moineau.contentpacks.block.contextpredicate.entitytyped.AllOfEntityTypedBlockContextPredicate;
import mod.moineau.contentpacks.block.contextpredicate.entitytyped.ContextEntityTypedBlockContextPredicate;
import mod.moineau.contentpacks.block.statefunction.BlockStateToIntFunction;
import mod.moineau.contentpacks.block.statefunction.ConstantBlockStateFunction;
import mod.moineau.contentpacks.block.statepredicate.BlocksMovementBlockStatePredicate;
import mod.moineau.contentpacks.block.statepredicate.LuminanceBlockStatePredicate;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public interface ContentBlockSettings {
    Function<BlockState, MapColor> DEFAULT_MAP_COLOR_PROVIDER = new ConstantBlockStateFunction<>(MapColor.CLEAR);
    ToIntFunction<BlockState> DEFAULT_LUMINANCE_PROVIDER = BlockStateToIntFunction.ZERO;
    AbstractBlock.TypedContextPredicate<EntityType<?>> DEFAULT_ALLOWS_SWPANING_PREDICATE = new AllOfEntityTypedBlockContextPredicate(List.of(
            new ContextEntityTypedBlockContextPredicate(
                    new IsSideSolidFullCubeBlockContextPredicate(Direction.UP)),
            new ContextEntityTypedBlockContextPredicate(
                    new StateBlockContextPredicate(
                            new LuminanceBlockStatePredicate(
                                    new IntComparison(Comparator.LESS, 14))))));
    AbstractBlock.ContextPredicate DEFAULT_SOLID_BLOCK_PREDICATE = IsFullCubeBlockContextPredicate.INSTANCE;
    AbstractBlock.ContextPredicate DEFAULT_SUFFOCATION_PREDICATE = new AllOfBlockContextPredicate(
            List.of(new StateBlockContextPredicate(BlocksMovementBlockStatePredicate.INSTANCE), IsFullCubeBlockContextPredicate.INSTANCE));
    AbstractBlock.ContextPredicate DEFAULT_BLOCK_VISION_PREDICATE = new AllOfBlockContextPredicate(
            List.of(new StateBlockContextPredicate(BlocksMovementBlockStatePredicate.INSTANCE), IsFullCubeBlockContextPredicate.INSTANCE));
    AbstractBlock.ContextPredicate DEFAULT_POST_PROCESS_PREDICATE = AlwaysBlockContextPredicate.FALSE;
    AbstractBlock.ContextPredicate DEFAULT_EMISIVE_RENDERING_PREDICATE = AlwaysBlockContextPredicate.FALSE;

    @Nullable Block contentpacks$getCopy();

    boolean contentpacks$isCopyShallow();

    @Nullable Optional<RegistryKey<LootTable>> contentpacks$getLootTableOverride();

    @Nullable String contentpacks$getTranslationKeyOverride();

    AbstractBlock.OffsetType contentpacks$getOffsetType();
}
