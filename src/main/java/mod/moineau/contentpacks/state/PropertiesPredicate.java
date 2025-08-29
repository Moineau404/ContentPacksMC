package mod.moineau.contentpacks.state;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public sealed abstract class PropertiesPredicate implements Predicate<State<?, ?>> {
    private static final Splitter SPLITTER = Splitter.on(',');
    public static final Codec<PropertiesPredicate> CODEC = Codec.STRING.comapFlatMap(PropertiesPredicate::parse, PropertiesPredicate::toString);

    public static Baked of(PropertyPredicate.Baked<?>... predicates) {
        return new Baked(List.of(predicates));
    }

    public static Baked empty() {
        return new Baked(List.of());
    }

    private static DataResult<PropertiesPredicate> parse(String statements) {
        if (statements.isEmpty()) {
            return DataResult.success(empty());
        }
        DataResult<List<PropertyPredicate.Unbaked>> predicates = DataResult.success(new ArrayList<>());
        for (String statement : SPLITTER.split(statements)) {
            predicates = predicates.apply2stable((list, predicate) -> {
                list.add(predicate);
                return list;
            }, PropertyPredicate.parse(statement));
        }
        return predicates.mapError(error -> "Failed to parse properties predicate '" + statements + "': " + error).map(Unbaked::new);
    }

    @Override
    public final String toString() {
        return this.getPredicates().stream().map(PropertyPredicate::toString).collect(Collectors.joining(","));
    }

    protected abstract List<? extends PropertyPredicate> getPredicates();

    public static final class Unbaked extends PropertiesPredicate {
        private final ImmutableList<PropertyPredicate.Unbaked> predicates;

        private Unbaked(List<PropertyPredicate.Unbaked> predicates) {
            this.predicates = ImmutableList.copyOf(predicates);
        }

        @Override
        public boolean test(State<?, ?> state) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<PropertyPredicate.Unbaked> getPredicates() {
            return this.predicates;
        }

        public DataResult<Baked> bake(Collection<Property<?>> properties) {
            DataResult<List<PropertyPredicate.Baked<?>>> predicates = DataResult.success(new ArrayList<>());
            for (PropertyPredicate.Unbaked predicate : this.predicates) {
                predicates = predicates.apply2stable((list, baked) -> {
                    list.add(baked);
                    return list;
                }, predicate.bake(properties));
            }
            return predicates.map(Baked::new).mapError(error -> "Failed to bake properties predicate \"" + this + "\": " + error);
        }
    }

    public static final class Baked extends PropertiesPredicate {
        private final List<PropertyPredicate.Baked<?>> predicates;

        private Baked(List<PropertyPredicate.Baked<?>> predicates) {
            this.predicates = predicates;
        }

        @Override
        public boolean test(State<?, ?> state) {
            for (PropertyPredicate.Baked<?> predicate : this.getPredicates()) {
                if (!predicate.test(state)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public List<PropertyPredicate.Baked<?>> getPredicates() {
            return this.predicates;
        }
    }
}
