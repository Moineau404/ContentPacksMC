package mod.moineau.contentpacks.state;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class TestState {
    record Property(String name, String... values) {}
    static Property AXIS = new Property("axis", "x", "y", "z");
    static Property SIZE = new Property("size", "1", "2", "3");
    static Property OPAQUE = new Property("opaque", "false", "true");
    record State(Map<Property, String> map) {
        @Override
        public String toString() {
            return '[' + map.entrySet().stream().map(entry -> entry.getKey().name + '=' + entry.getValue()).collect(Collectors.joining(", ")) + ']';
        }
    }
    static List<State> STATES = new LinkedList<>();
    static {
        for (String axis : AXIS.values) {
            for (String size : SIZE.values) {
                for (String opaque : OPAQUE.values) {
                    STATES.add(new State(new LinkedHashMap<>() {{
                        put(AXIS, axis);
                        put(SIZE, size);
                        put(OPAQUE, opaque);
                    }}));
                }
            }
        }
    }
    record Predicate(Property property, String value) {
        boolean test(State state) {
            return state.map.get(property).equals(value);
        }

        @Override
        public @NotNull String toString() {
            return property.name + '=' + value;
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
        ListMultimap<String, State> value2State = MultimapBuilder.linkedHashKeys().linkedListValues().build();
        for (Map.Entry<State, String> entry : stateMap.entrySet()) {
            value2State.put(entry.getValue(), entry.getKey());
        }
        System.out.println(print(value2State.asMap()));
        Map<Predicates, String> predicatesMap2 = new LinkedHashMap<>() {{
            for (var entry : value2State.asMap().entrySet()) {
                for (var predicates : simplify(entry.getValue())) {
                    put(predicates, entry.getKey());
                }
            }
        }};
        System.out.println(print(predicatesMap2));
    }

    static String print(Map<?, ?> map) {
        return map.entrySet().stream().map(entry -> entry.getKey().toString() + " -> " + entry.getValue().toString()).collect(Collectors.joining("\n"));
    }

    static List<Predicates> simplify(Collection<State> states) {
        List<Predicate> predicates = new LinkedList<>();

        State first = states.stream().findFirst().get();
        for (var entry : first.map.entrySet()) {
            Property property = entry.getKey();
            String value = entry.getValue();

            if (states.stream().allMatch(state -> state.map.get(property).equals(value))) {
                predicates.add(new Predicate(property, value));
            }
        }

        if (!predicates.isEmpty()) {
            return List.of(new Predicates(predicates.toArray(Predicate[]::new)));
        } else {
            return states.stream().map(state -> new Predicates(state.map.entrySet().stream()
                    .map(entry -> new Predicate(entry.getKey(), entry.getValue())).toArray(Predicate[]::new))).toList();
        }
    }
}