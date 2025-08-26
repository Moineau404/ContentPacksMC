package mod.moineau.contentpacks.state;

import mod.moineau.contentpacks.api.function.predicate.Comparator;
import mod.moineau.contentpacks.api.util.Workaround;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Workaround
@ApiStatus.Internal
public final class PropertiesRegistry {
    private static final Map<Object, Map<String, Property<?>>> OWNER2PROPERTIES = new IdentityHashMap<>();
    private static final Map<State<?, ?>, Map<String, Function<String, Integer>>> STATE2PREDICATES = new IdentityHashMap<>();
    private static final Map<State<?, ?>, Map<String, Map<String, Map<Comparator, Boolean>>>> STATE2PREDICATES2 = new IdentityHashMap<>();
    private static final Map<State<?, ?>, Map<String, String>> STATE2PROPERTY2VALUE = new IdentityHashMap<>();

    public static void register(Object owner, Map<String, Property<?>> properties) {
        OWNER2PROPERTIES.put(owner, properties);
    }

    public static @Nullable Property<?> get(Object owner, String property) {
        return OWNER2PROPERTIES.get(owner).get(property);
    }

    public static Optional<Property<?>> getOrEmpty(Object owner, String property) {
        return Optional.ofNullable(get(owner, property));
    }

    public static void register(State<?, ?> state) {
        OWNER2PROPERTIES.put(state, new HashMap<>());
        state.getEntries().forEach((property, value) -> STATE2PREDICATES.get(state).put(property.getName(), bake(property, value)));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Function<String, Integer> bake(Property<T> property, Object value) {
        return name -> ((T) value).compareTo(property.parse(name)
                .orElseThrow(() -> new IllegalStateException("No such value '" + name + "' for property '" + property.getName() + "'")));
    }

    public static int get(State<?, ?> state, String property, String value) {
        return STATE2PREDICATES.get(state).get(property).apply(value);
    }

    @SuppressWarnings("unchecked")
    public static void register2(State<?, ?> state) {
        Map<String, Map<String, Map<Comparator, Boolean>>> propertyMap = new HashMap<>();
        state.getEntries().forEach((property, value) -> {
            Map<String, Map<Comparator, Boolean>> valueMap = new HashMap<>();
            property.getValues().forEach(value1 -> {
//                Map<Comparator, Boolean> comparatorMap = new HashMap<>();
//                for (Comparator comparator : Comparator.values()) {
//                    comparatorMap.put(comparator, comparator.compare((Comparable) value, value1));
//                }
//                valueMap.put(((Property) property).name(value1), comparatorMap);
            });
            propertyMap.put(property.getName(), valueMap);
        });
        STATE2PREDICATES2.put(state, propertyMap);
    }

    public static boolean get(State<?, ?> state, String property, Comparator comparator, String value) {
        return STATE2PREDICATES2.get(state).get(property).get(value).get(comparator);
    }

    @SuppressWarnings("unchecked")
    public static void register3(State<?, ?> state) {
        Map<String, String> map = new HashMap<>();
        state.getEntries().forEach(((property, value) -> map.put(property.getName(), ((Property) property).name(value))));
        STATE2PROPERTY2VALUE.put(state, map);
    }
}