package mod.moineau.contentpacks.block;

import mod.moineau.contentpacks.api.function.predicate.Comparison;
import mod.moineau.contentpacks.block.contextpredicate.BlockContextPredicate;
import mod.moineau.contentpacks.block.contextpredicate.entitytyped.EntityTypedBlockContextPredicate;
import mod.moineau.contentpacks.block.statefunction.BlockStateFunction;
import mod.moineau.contentpacks.block.statefunction.BlockStateToIntFunction;
import mod.moineau.contentpacks.block.statepredicate.BlockStatePredicate;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public interface ContentBlockSettings {
    Function<BlockState, MapColor> DEFAULT_MAP_COLOR_PROVIDER = BlockStateFunction.constant(MapColor.CLEAR);
    ToIntFunction<BlockState> DEFAULT_LUMINANCE_PROVIDER = BlockStateToIntFunction.constant(0);
    AbstractBlock.TypedContextPredicate<EntityType<?>> DEFAULT_ALLOWS_SWPANING_PREDICATE = EntityTypedBlockContextPredicate.allOf(
            EntityTypedBlockContextPredicate.isSideSolidFullCube(Direction.UP),
            EntityTypedBlockContextPredicate.matchingState(BlockStatePredicate.luminance(Comparison.less(14))));
    AbstractBlock.ContextPredicate DEFAULT_SOLID_BLOCK_PREDICATE = BlockContextPredicate.isFullCube();
    AbstractBlock.ContextPredicate DEFAULT_SUFFOCATION_PREDICATE = BlockContextPredicate.allOf(
            BlockContextPredicate.matchingState(BlockStatePredicate.blocksMovement()),
            BlockContextPredicate.isFullCube());
    AbstractBlock.ContextPredicate DEFAULT_BLOCK_VISION_PREDICATE = BlockContextPredicate.allOf(
            BlockContextPredicate.matchingState(BlockStatePredicate.blocksMovement()),
            BlockContextPredicate.isFullCube());
    AbstractBlock.ContextPredicate DEFAULT_POST_PROCESS_PREDICATE = BlockContextPredicate.alwaysFalse();
    AbstractBlock.ContextPredicate DEFAULT_EMISIVE_RENDERING_PREDICATE = BlockContextPredicate.alwaysFalse();

    @Nullable Block contentpacks$getCopy();

    boolean contentpacks$isCopyShallow();

    @Nullable Optional<RegistryKey<LootTable>> contentpacks$getLootTableOverride();

    @Nullable String contentpacks$getTranslationKeyOverride();

    AbstractBlock.OffsetType contentpacks$getOffsetType();
}
