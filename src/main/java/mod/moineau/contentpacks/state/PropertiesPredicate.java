package mod.moineau.contentpacks.state;

import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
    public static final Codec<PropertiesPredicate> CACHING_CODEC = Codec.STRING.comapFlatMap(PropertiesPredicate::parseCaching, PropertiesPredicate::toString);

    public static PropertiesPredicate.Unbaked of(PropertyPredicate.Unbaked... predicates) {
        return new Unbaked(List.of(predicates));
    }

    public static PropertiesPredicate.Baked of(PropertyPredicate.Baked<?>... predicates) {
        return new Baked(List.of(predicates));
    }

    public static DataResult<PropertiesPredicate.Unbaked> parse(String statements) {
        return parseList(statements).map(Unbaked::new);
    }

    public static DataResult<PropertiesPredicate.Caching> parseCaching(String statements) {
        return parseList(statements).map(Caching::new);
    }

    private static DataResult<List<PropertyPredicate.Unbaked>> parseList(String statements) {
        DataResult<List<PropertyPredicate.Unbaked>> predicates = DataResult.success(new ArrayList<>());
        for (String statement : SPLITTER.split(statements)) {
            predicates = predicates.apply2stable((list, predicate) -> {
                list.add(predicate);
                return list;
            }, PropertyPredicate.parse(statement));
        }
        return predicates.mapError(error -> "Failed to parse properties predicate \"" + statements + "\": " + error);
    }

    @Override
    public boolean test(State<?, ?> state) {
        for (PropertyPredicate predicate : this.getPredicates()) {
            if (!predicate.test(state)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final String toString() {
        return this.getPredicates().stream().map(PropertyPredicate::toString).collect(Collectors.joining(","));
    }

    protected abstract List<? extends PropertyPredicate> getPredicates();

    public static non-sealed class Unbaked extends PropertiesPredicate {
        private final List<PropertyPredicate.Unbaked> predicates;

        private Unbaked(List<PropertyPredicate.Unbaked> predicates) {
            this.predicates = predicates;
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

    public static final class Caching extends Unbaked {
        private final LoadingCache<State<?, ?>, Boolean> cache = CacheBuilder.newBuilder().build(CacheLoader.from(super::test));

        private Caching(List<PropertyPredicate.Unbaked> predicates) {
            super(predicates);
        }

        @Override
        public boolean test(State<?, ?> state) {
            try {
                return this.cache.get(state);
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static final class Baked extends PropertiesPredicate {
        private final List<PropertyPredicate.Baked<?>> predicates;

        private Baked(List<PropertyPredicate.Baked<?>> predicates) {
            this.predicates = predicates;
        }

        @Override
        public List<PropertyPredicate.Baked<?>> getPredicates() {
            return this.predicates;
        }
    }
}
