package mod.moineau.contentpacks.state.v3;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class StateDefinition<T> implements Function<State<?, ?>, @Nullable T> {
    private final ImmutableMap<State<?, ?>, T> stateMap;
    private Map<String, T> stringMap;

    private StateDefinition(Map<State<?, ?>, T> stateMap, Map<String, T> stringMap) {
        this.stateMap = ImmutableMap.copyOf(stateMap);
        this.stringMap = stringMap;
    }

    private StateDefinition(Map<State<?, ?>, T> stateMap) {
        this.stateMap = ImmutableMap.copyOf(stateMap);
        Map<String, T> map = new HashMap<>();
        for (Map.Entry<State<?, ?>, T> entry : stateMap.entrySet()) {
        }
    }

    @Override
    public @Nullable T apply(State<?, ?> state) {
        return stateMap.get(state);
    }

    public Optional<T> applyOrEmpty(State<?, ?> state) {
        return Optional.ofNullable(apply(state));
    }

    public static final class Unbaked<T> {
        private final ImmutableMap<PropertiesPredicate.Unbaked, T> predicateMap;

        private Unbaked(ImmutableMap<PropertiesPredicate.Unbaked, T> predicateMap) {
            this.predicateMap = predicateMap;
        }

        public Map<String, T> toStringMap() {
            Map<String, T> map = new HashMap<>();
            for (Map.Entry<PropertiesPredicate.Unbaked, T> entry : predicateMap.entrySet()) {
                map.put(entry.getKey().toString(), entry.getValue());
            }
            return map;
        }

        public DataResult<StateDefinition<T>> bake(StateManager<?, ?> stateManager) {
            Map<String, Property<?>> propertyMap = new HashMap<>();
            for (Property<?> property : stateManager.getProperties()) {
                propertyMap.put(property.getName(), property);
            }
            DataResult<Map<PropertiesPredicate, T>> bakedPredicateMapResult = DataResult.success(new HashMap<>());
            for (Map.Entry<PropertiesPredicate.Unbaked, T> entry : predicateMap.entrySet()) {
                bakedPredicateMapResult = bakedPredicateMapResult.apply2stable((map, baked) -> {
                    map.put(baked, entry.getValue());
                    return map;
                }, entry.getKey().bake(propertyMap));
            }
            return bakedPredicateMapResult.flatMap(bakedPredicateMap -> {
                Function<State<?, ?>, DataResult<T>> partialGet = DataResult.partialGet(state -> {
                    for (Map.Entry<PropertiesPredicate, T> value : bakedPredicateMap.entrySet()) {
                        if (value.getKey().test(state)) {
                            return value.getValue();
                        }
                    }
                    return null;
                }, () -> "Missing definition for blockstate: ");
                Reference2ObjectArrayMap<State<?, ?>, @Nullable T> stateMap = new Reference2ObjectArrayMap<>();
                DataResult<Unit> finalResult = stateManager.getStates().stream().reduce(
                        DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                        (result, state) -> result.apply2stable((u, v) -> u, partialGet.apply(state).ifSuccess(value -> stateMap.put(state, value))),
                        (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
                );
                return finalResult.map(ignore -> stateMap).setPartial(stateMap);
            }).map(stateMap -> new StateDefinition<>(stateMap, toStringMap()));
        }
    }
}