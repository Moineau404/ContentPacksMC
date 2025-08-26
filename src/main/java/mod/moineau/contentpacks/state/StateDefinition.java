package mod.moineau.contentpacks.state;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import mod.moineau.contentpacks.api.util.EasyDebug;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

// TODO Create a new format of block model definition ("assets/blocks" like "assets/items") like item model definition
public sealed abstract class StateDefinition<T> {
    protected final Map<PropertiesPredicate, T> predicates;
    protected final @Nullable T fallback;

    public static <T> Codec<StateDefinition<T>> createCodec(Codec<T> elementCodec) {
        return Codec.unboundedMap(PropertiesPredicate.CODEC, elementCodec).xmap(StateDefinition.Unbaked::new, definition -> definition.predicates);
    }

    public static <T> Codec<StateDefinition<T>> createCodec(Codec<T> elementCodec, T fallback) {
        return Codec.unboundedMap(PropertiesPredicate.CODEC, elementCodec).xmap(predicates -> new Unbaked<>(predicates, fallback), definition -> definition.predicates);
    }

    private StateDefinition(Map<PropertiesPredicate, T> predicates, @Nullable T fallback) {
        this.predicates = predicates;
        this.fallback = fallback;
    }

    public static <T> StateDefinition.Unbaked<T> of(Map<PropertiesPredicate, T> predicates, T fallback) {
        return new StateDefinition.Unbaked<>(predicates, fallback);
    }

    public static <T> StateDefinition.Unbaked<T> of(Map<PropertiesPredicate, T> predicates) {
        return new StateDefinition.Unbaked<>(predicates, null);
    }

    public abstract @Nullable T get(State<?, ?> state);

    public final Optional<T> getOrEmpty(State<?, ?> state) {
        return Optional.ofNullable(get((state)));
    }

    public static final class Unbaked<T> extends StateDefinition<T> {
        private Unbaked(Map<PropertiesPredicate, T> predicates, T fallback) {
            super(predicates, fallback);
        }

        private Unbaked(Map<PropertiesPredicate, T> predicates) {
            this(predicates, null);
        }

        @Override
        public @Nullable T get(State<?, ?> state) {
            for (Map.Entry<PropertiesPredicate, T> value : this.predicates.entrySet()) {
                if (value.getKey().test(state)) {
                    return value.getValue();
                }
            }
            return this.fallback;
        }

        @EasyDebug
        public DataResult<Baked<T>> bake(StateManager<?, ?> stateManager) {
            Collection<Property<?>> properties = stateManager.getProperties();
            DataResult<Map<PropertiesPredicate, T>> bakedPredicatesResult = DataResult.success(new HashMap<>());
            for (Map.Entry<PropertiesPredicate, T> value : this.predicates.entrySet()) {
                PropertiesPredicate predicate = value.getKey();
                DataResult<? extends PropertiesPredicate> result = predicate instanceof PropertiesPredicate.Unbaked unbaked ? unbaked.bake(properties) : DataResult.success(predicate);
                bakedPredicatesResult = bakedPredicatesResult.apply2stable((map, baked) -> {
                    map.put(baked, value.getValue());
                    return map;
                }, result);
            }
            return bakedPredicatesResult.flatMap(bakedPredicates -> {
                Function<State<?, ?>, DataResult<T>> partialGet = DataResult.partialGet(state -> {
                    for (Map.Entry<? extends PropertiesPredicate, T> value : bakedPredicates.entrySet()) {
                        if (value.getKey().test(state)) {
                            return value.getValue();
                        }
                    }
                    return null;
                }, () -> "Missing definition for blockstate: ");
                Reference2ObjectArrayMap<State<?, ?>, @Nullable T> states = new Reference2ObjectArrayMap<>();
                DataResult<Unit> finalResult = stateManager.getStates().stream().reduce(
                        DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                        (result, state) -> result.apply2stable((u, v) -> u, partialGet.apply(state).ifSuccess(value -> states.put(state, value))),
                        (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
                );
                return finalResult.map(ignore -> states).setPartial(states);
            }).map(states -> new Baked<>(this.predicates, states, this.fallback));
        }

        @Override
        public String toString() {
            return "Unbaked{" +
                    "predicates=" + predicates +
                    ", fallback=" + fallback +
                    '}';
        }
    }

    public static final class Baked<T> extends StateDefinition<@Nullable T> {
        private final Reference2ObjectArrayMap<State<?, ?>, @Nullable T> states;

        private Baked(Map<PropertiesPredicate, T> predicates, Reference2ObjectArrayMap<State<?, ?>, @Nullable T> states, T fallback) {
            super(predicates, fallback);
            this.states = states;
        }

        private Baked(Map<PropertiesPredicate, T> predicates, Reference2ObjectArrayMap<State<?, ?>, @Nullable T> states) {
            this(predicates, states, null);
        }

        @Override
        public @Nullable T get(State<?, ?> state) {
            return this.states.get(state);
        }

        @Override
        public String toString() {
            return "Baked{" +
                    "states=" + states +
                    ", fallback=" + fallback +
                    '}';
        }
    }
}
