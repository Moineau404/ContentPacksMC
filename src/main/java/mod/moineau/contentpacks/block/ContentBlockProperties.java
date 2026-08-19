package mod.moineau.contentpacks.block;

import mod.moineau.contentpacks.api.math.Comparison;
import mod.moineau.contentpacks.block.statepredicates.StatePredicate;
import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicate;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public interface ContentBlockProperties {
    Function<BlockState, MapColor> DEFAULT_MAP_COLOR = MapColorProvider.of(MapColor.NONE);
    ToIntFunction<BlockState> DEFAULT_LIGHT_EMISSION = LightEmissionProvider.of(0);
    BlockBehaviour.StateArgumentPredicate<EntityType<?>> DEFAULT_IS_VALID_SPAWN = EntityTypedStatePredicate.allOf(
            EntityTypedStatePredicate.isSideSolidFullCube(Direction.UP),
            EntityTypedStatePredicate.lightEmission(Comparison.lessThan(14)));
    BlockBehaviour.StatePredicate DEFAULT_IS_REDSTONE_CONDUCTOR = StatePredicate.isFullCube();
    BlockBehaviour.StatePredicate DEFAULT_IS_SUFFOCATING = StatePredicate.allOf(
            StatePredicate.blocksMovement(),
            StatePredicate.isFullCube());
    BlockBehaviour.StatePredicate DEFAULT_IS_VIEW_BLOCKING = StatePredicate.allOf(
            StatePredicate.blocksMovement(),
            StatePredicate.isFullCube());
    BlockBehaviour.PostProcess DEFAULT_POST_PROCESS = (_, _, _) -> null;
    Predicate<BlockState> DEFAULT_EMISSIVE_RENDERING = _ -> false;

    @Nullable Block contentpacks$getFullCopyOf();

    boolean contentpacks$isLegacyCopy();

    @Nullable Optional<ResourceKey<LootTable>> contentpacks$getLootTableOverride();

    @Nullable String contentpacks$getDescriptionIdOverride();

    BlockBehaviour.OffsetType contentpacks$getOffsetType();
}
