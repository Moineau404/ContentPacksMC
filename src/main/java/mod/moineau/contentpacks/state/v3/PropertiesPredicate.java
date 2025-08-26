package mod.moineau.contentpacks.state.v3;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.DataResult;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PropertiesPredicate implements Predicate<State<?, ?>> {
    private final ImmutableList<PropertyPredicate<?>> predicates;

    private PropertiesPredicate(List<PropertyPredicate<?>> predicates) {
        this.predicates = ImmutableList.copyOf(predicates);
    }

    @Override
    public boolean test(State<?, ?> state) {
        for (PropertyPredicate<?> predicate : predicates) {
            if (!predicate.test(state)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final String toString() {
        return predicates.stream().map(PropertyPredicate::toString).collect(Collectors.joining(","));
    }

    public static final class Unbaked implements Predicate<State<?, ?>> {
        private final ImmutableList<PropertyPredicate.Unbaked> predicates;

        public Unbaked(List<PropertyPredicate.Unbaked> predicates) {
            this.predicates = ImmutableList.copyOf(predicates);
        }

        @Override
        public boolean test(State<?, ?> state) {
            throw new UnsupportedOperationException("Trying to use unbaked properties predicate!");
        }

        @Override
        public String toString() {
            return predicates.stream().map(PropertyPredicate.Unbaked::toString).collect(Collectors.joining(","));
        }

        public DataResult<PropertiesPredicate> bake(Map<String, Property<?>> propertyMap) {
            DataResult<List<PropertyPredicate<?>>> predicates = DataResult.success(new ArrayList<>());
            for (PropertyPredicate.Unbaked predicate : this.predicates) {
                predicates = predicates.apply2stable((list, baked) -> {
                    list.add(baked);
                    return list;
                }, predicate.bake(propertyMap));
            }
            return predicates.map(PropertiesPredicate::new).mapError(error -> "Failed to bake properties predicate '" + this + "': " + error);
        }
    }
}