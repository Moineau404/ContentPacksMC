package mod.moineau.contentpacks.state;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class TestState2 {
    record Property(String name, Object... values) {}
    static Property AXIS = new Property("axis", "x", "y", "z");
    static Property SIZE = new Property("size", "1", "2", "3");
    static Property OPAQUE = new Property("opaque", "false", "true");
    record State(Map<Property, Object> map) {
        @Override
        public String toString() {
            return '[' + map.entrySet().stream().map(entry -> entry.getKey().name + '=' + entry.getValue()).collect(Collectors.joining(", ")) + ']';
        }
    }
    static List<State> STATES = new LinkedList<>();
    static {
        for (Object axis : AXIS.values) {
            for (Object size : SIZE.values) {
                for (Object opaque : OPAQUE.values) {
                    STATES.add(new State(new LinkedHashMap<>() {{
                        put(AXIS, axis);
                        put(SIZE, size);
                        put(OPAQUE, opaque);
                    }}));
                }
            }
        }
    }
    record Predicate(Property property, Object value) {
        boolean test(State state) {
            return state.map.get(property).equals(value);
        }

        @Override
        public @NotNull String toString() {
            return property.name + '=' + value.toString();
        }
    }
    record Predicates(Predicate... predicates) {
        boolean test(State state) {
            for (Predicate predicate : predicates) {
                if (!predicate.test(state)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public @NotNull String toString() {
            return '(' + Arrays.stream(predicates).map(Object::toString).collect(Collectors.joining(", ")) + ')';
        }
    }

    public static void main(String[] args) {
        System.out.println(STATES.stream().map(Object::toString).collect(Collectors.joining("\n")));
        Map<Predicates, String> predicatesMap = new LinkedHashMap<>() {{
            put(new Predicates(new Predicate(AXIS, "x"), new Predicate(OPAQUE, "false")), "blue");
            put(new Predicates(new Predicate(AXIS, "x"), new Predicate(OPAQUE, "true")), "green");
            put(new Predicates(new Predicate(AXIS, "y"), new Predicate(OPAQUE, "false")), "green");
            put(new Predicates(new Predicate(AXIS, "y"), new Predicate(OPAQUE, "true")), "red");
            put(new Predicates(new Predicate(AXIS, "z"), new Predicate(OPAQUE, "false")), "yellow");
            put(new Predicates(new Predicate(AXIS, "z"), new Predicate(OPAQUE, "true")), "blue");
        }};
        System.out.println(print(predicatesMap));
        Map<State, String> stateMap = new LinkedHashMap<>() {{
            for (State state : STATES) {
                for (Map.Entry<Predicates, String> entry : predicatesMap.entrySet()) {
                    if (entry.getKey().test(state)) {
                        put(state, entry.getValue());
                        break;
                    }
                }
            }
        }};
        System.out.println(print(stateMap));
        Map<Predicates, String> predicatesMap2 = unbake(stateMap);
        System.out.println(print(predicatesMap2));
    }

    static String print(Map<?, ?> map) {
        return map.entrySet().stream().map(entry -> entry.getKey().toString() + " -> " + entry.getValue().toString()).collect(Collectors.joining("\n"));
    }

    static Map<Predicates, String> unbake(Map<State, String> stateMap) {
        Map<Property, SetMultimap<String, Object>> property_to_value2objects = new LinkedHashMap<>();
        Map<Map<Property, Object>, String> property2object_to_value = new LinkedHashMap<>();
        Set<Property> properties = new LinkedHashSet<>();

        for (var stateMapEntry : stateMap.entrySet()) {
            var state = stateMapEntry.getKey();
            var value = stateMapEntry.getValue();
            Map<Property, Object> property2object = new LinkedHashMap<>();

            for (var propertyMapEntry : state.map.entrySet()) {
                var property = propertyMapEntry.getKey();
                var object = propertyMapEntry.getValue();

                property2object.put(property, object);
                property_to_value2objects.computeIfAbsent(property, (ignore) -> MultimapBuilder.linkedHashKeys().linkedHashSetValues().build())
                        .put(value, object);
                properties.add(property);
            }

            property2object_to_value.put(property2object, value);
        }

        var filteredProperties = Sets.filter(properties, property -> property_to_value2objects.get(property).values().size() == 1);
        for (var property2value : property2object_to_value.keySet()) {
            for (var filteredProperty : filteredProperties) {
                property2value.remove(filteredProperty);
            }
        }

        Map<Predicates, String> predicatesMap = new LinkedHashMap<>();
        for (var property2object_to_valueEntry : property2object_to_value.entrySet()) {
            var property2object = property2object_to_valueEntry.getKey();
            var value = property2object_to_valueEntry.getValue();

            List<Predicate> predicates = new LinkedList<>();

            for (var property2objectEntry : property2object.entrySet()) {
                var property = property2objectEntry.getKey();
                var object = property2objectEntry.getValue();

                predicates.add(new Predicate(property, object));
            }

            predicatesMap.put(new Predicates(predicates.toArray(Predicate[]::new)), value);
        }

        return predicatesMap;
    }
}