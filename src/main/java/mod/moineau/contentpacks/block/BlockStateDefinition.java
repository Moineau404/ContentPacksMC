package mod.moineau.contentpacks.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.ContentPacks;
import net.fabricmc.fabric.impl.dimension.FailSoftMapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Function;

/**
 * {@link BlockStateDefinition Unbaked block state definition} binds a {@link BlockPropertiesPredicate predicate} to a value.
 * When passing a {@link BlockState block state}, method {@link #get(Object) get} returns the associated value of the first predicate to return {@link Boolean true}.
 * Method {@link #bake(StateManager) bake} returns a {@link Baked baked} copy of this block state definition which directly binds a {@link BlockState block state} to a value.
 * @param <T> Type of values
 */
@Deprecated
public sealed class BlockStateDefinition<T> extends AbstractMap<BlockState, T> {
    public static final BlockStateDefinition<?> EMPTY = new BlockStateDefinition<>(Map.of());

    @SuppressWarnings("unchecked")
    public static <T> BlockStateDefinition<@Nullable T> empty() {
        return (BlockStateDefinition<T>) EMPTY;
    }

    public static <T> BlockStateDefinition<@NotNull T> unit(@NotNull T value) {
        return new Unit<>(value);
    }

    public static <T> Codec<BlockStateDefinition<T>> createCodec(Codec<T> elementCodec) {
        return Codec.unboundedMap(BlockPropertiesPredicate.CODEC, elementCodec)
                .xmap(BlockStateDefinition::new, definition -> definition.predicates);
    }

    public static <T> Codec<BlockStateDefinition<T>> createRawCodec(Codec<T> elementCodec) {
        return Codec.unboundedMap(RawBlockPropertiesPredicate.CODEC, elementCodec)
                .xmap(BlockStateDefinition::new, definition -> definition.predicates);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> Codec<BlockStateDefinition<T>> createLenientCodec(Codec<T> elementCodec) {
        return new FailSoftMapCodec<>(BlockPropertiesPredicate.CODEC, elementCodec)
                .xmap(BlockStateDefinition::new, definition -> definition.predicates);
    }

    protected final ImmutableMap<BlockPropertiesPredicate, T> predicates;

    protected BlockStateDefinition(Map<BlockPropertiesPredicate, T> predicates) {
        this.predicates = ImmutableMap.copyOf(predicates);
    }

    @Override
    public int size() {
        return predicates.size();
    }

    @Override
    public boolean isEmpty() {
        return predicates.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return predicates.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return predicates.containsValue(value);
    }

    @Override
    public @Nullable T get(Object key) {
        if (key instanceof BlockState state) {
            for (Map.Entry<BlockPropertiesPredicate, T> entry : predicates.entrySet()) {
                if (entry.getKey().test(state)) {
                    return entry.getValue();
                }
            }
        }
        return predicates.get(key);
    }

    @Unmodifiable
    @Override
    public final @Nullable T put(BlockState key, T value) {
        throw new UnsupportedOperationException("Cannot modify block state definition");
    }

    @Override
    public final T remove(Object key) {
        throw new UnsupportedOperationException("Cannot modify block state definition");
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException("Cannot modify block state definition");
    }

    @Override
    public @NotNull Set<BlockState> keySet() {
        throw new UnsupportedOperationException("Cannot get key set from unbaked block state definition");
    }

    @Override
    public @NotNull Set<Entry<BlockState, T>> entrySet() {
        throw new UnsupportedOperationException("Cannot get entry set from unbaked block state definition");
    }

    @Override
    public @NotNull Collection<T> values() {
        return predicates.values();
    }

    public @NotNull Baked<T> bake(StateManager<Block, BlockState> manager) {
        return new Baked<>(manager, predicates);
    }

    @Unmodifiable
    public static final class Baked<T> extends BlockStateDefinition<T> {
        private final ImmutableMap<BlockState, T> blockstates;
        private final Block owner;

        private Baked(StateManager<Block, BlockState> manager, Map<BlockPropertiesPredicate, T> predicates) {
            super(predicates);
            this.blockstates = ImmutableMap.copyOf(bake(manager, predicates));
            this.owner = manager.getOwner();
        }

        public Block getOwner() {
            return owner;
        }

        @Override
        public boolean containsKey(Object key) {
            if (key instanceof BlockState) {
                return blockstates.containsKey(key);
            }
            return super.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return blockstates.containsValue(value);
        }

        @Override
        public @Nullable T get(Object key) {
            return blockstates.get(key);
        }

        @Override
        public @NotNull Set<BlockState> keySet() {
            return blockstates.keySet();
        }

        @Override
        public @NotNull Set<Entry<BlockState, T>> entrySet() {
            return blockstates.entrySet();
        }

        @Override
        public @NotNull Collection<T> values() {
            return blockstates.values();
        }

        @Override
        public @NotNull Baked<T> bake(StateManager<Block, BlockState> manager) {
            return this;
        }
    }

    private static final class Unit<T> extends BlockStateDefinition<T> {
        private final T value;

        private Unit(@NotNull T value) {
            super(Map.of(BlockPropertiesPredicate.of(""), value));
            this.value = value;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return this.equals(value);
        }

        @Override
        public @NotNull T get(Object key) {
            return value;
        }

        @Override
        public @NotNull Collection<T> values() {
            return Collections.singleton(value);
        }
    }

    public static <T> Map<BlockState, T> bake(StateManager<Block, BlockState> manager, Map<BlockPropertiesPredicate, T> unbaked) {
        Map<BlockState, T> map = new HashMap<>();

        unbaked.forEach((predicate, value) -> {
            try {
                for (BlockState blockState : manager.getStates()) {
                    if (predicate.test(blockState)) {
                        map.put(blockState, value);
                    }
                }
            } catch (Exception var9) {
                ContentPacks.LOGGER.warn("Exception loading block state definition: '{}' for variant: {}", predicate, var9.getMessage());
            }
        });

        return map;
    }

    /**
     * Will be used to deserialize Minecraft vanilla content
     */
    @ApiStatus.Internal
    private static <T> Map<String, T> unbake(Map<BlockState, T> unbaked) {
        Map<Property<?>, Map<T, Set<Comparable<?>>>> map = new HashMap<>();
        Map<Map<Property<?>, Comparable<?>>, T> map0 = new HashMap<>();
        Set<Property<?>> properties = new HashSet<>();

        unbaked.forEach((state, value) -> {
            Map<Property<?>, Comparable<?>> map1 = new HashMap<>();
            map0.put(map1, value);
            state.getProperties().forEach(property -> {
                map1.put(property, state.get(property));
            });
            map1.forEach((property, comparable) -> {
                map.computeIfAbsent(property, (ignore) -> new HashMap<>())
                        .computeIfAbsent(value, (ignore) -> new HashSet<>()).add(comparable);
                properties.add(property);
            });
        });

        Set<Property<?>> filtered = Sets.filter(properties, property -> new HashSet<>(map.get(property).values()).size() == 1);
        map0.keySet().forEach(map3 -> filtered.forEach(map3::remove));

        Map<String, T> predicates = new HashMap<>();
        map0.forEach((map4, value) -> {
            StringBuilder builder = new StringBuilder();
            map4.forEach((property, comparable) -> {
                if (!builder.isEmpty()) {
                    builder.append(",");
                }
                builder.append(property.getName()).append("=").append(comparable.toString().toLowerCase());
            });
            predicates.put(builder.toString(), value);
        });

        return predicates;
    }

    /**
     * Will be used to deserialize Minecraft vanilla content
     */
    @ApiStatus.Internal
    private static <T> Map<BlockState, T> recreateBlockStateMap(StateManager<Block, BlockState> stateManager, Function<BlockState, T> function) {
        Map<BlockState, T> map = new HashMap<>();
        stateManager.getStates().forEach(state -> {
                map.put(state, function.apply(state));
        });
        return map;
    }

    /**
     * Will be used to deserialize Minecraft vanilla content
     */
    @ApiStatus.Internal
    public static <T> BlockStateDefinition<T> recreate(StateManager<Block, BlockState> stateManager, Function<BlockState, T> function) {
        Map<String, T> map = unbake(recreateBlockStateMap(stateManager, function));

        Map<BlockPropertiesPredicate, T> map1 = new HashMap<>();
        map.forEach((string, value) -> {
            map1.put(BlockPropertiesPredicate.of(string), value);
        });
        return new BlockStateDefinition<>(map1);
    }
}