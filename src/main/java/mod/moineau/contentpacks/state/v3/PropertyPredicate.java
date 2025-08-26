package mod.moineau.contentpacks.state.v3;

import com.mojang.serialization.DataResult;
import mod.moineau.contentpacks.api.function.predicate.Comparator;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;

import java.util.Map;
import java.util.function.Predicate;

public class PropertyPredicate<T extends Comparable<T>> implements Predicate<State<?, ?>> {
    private final Property<T> property;
    private final Comparator comparator;
    private final T value;

    private PropertyPredicate(Property<T> property, Comparator comparator, T value) {
        this.property = property;
        this.comparator = comparator;
        this.value = value;
    }

    @Override
    public boolean test(State<?, ?> state) {
        T comparable = state.get(property);
        if (comparable != null) {
            return comparator.compare(comparable, value);
        }
        return false;
    }

    @Override
    public String toString() {
        return property.getName() + comparator.toString() + property.name(value);
    }

    public static final class Unbaked implements Predicate<State<?, ?>> {
        private final String property;
        private final Comparator comparator;
        private final String value;

        private Unbaked(String property, Comparator comparator, String value) {
            this.property = property;
            this.comparator = comparator;
            this.value = value;
        }

        @Override
        public boolean test(State<?, ?> state) {
            throw new UnsupportedOperationException("Trying to use unbaked property predicate!");
        }

        @Override
        public String toString() {
            return property + comparator.toString() + value;
        }

        public DataResult<PropertyPredicate<?>> bake(Map<String, Property<?>> propertyMap) {
            return DataResult.<String, Property<?>>partialGet(propertyMap::get, () -> "Unkown property '" + this.property + "';").apply(this.property)
                    .flatMap(property -> property.parse(this.value).map(value -> new PropertyPredicate(property, this.comparator, value))
                            .<DataResult<PropertyPredicate<?>>>map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unkown value '" + this.value + "' for property '" + this.property + "';")));
        }
    }
}
