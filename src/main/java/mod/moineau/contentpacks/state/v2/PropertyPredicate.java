package mod.moineau.contentpacks.state.v2;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mod.moineau.contentpacks.api.function.predicate.Comparator;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.regex.Pattern;

final class PropertyPredicate implements Predicate<State<?, ?>> {
    private static final Pattern PATTERN = Pattern.compile("(.+?)(!=|>=|<=|=|>|<)(.+)");
    public static final Codec<PropertyPredicate> CODEC = Codec.STRING.comapFlatMap(PropertyPredicate::parse, PropertyPredicate::toString);
    private Predicate<State<?, ?>> predicate;

    private PropertyPredicate(String property, Comparator comparator, String value) {
        this.predicate = new Unbaked(property, comparator, value);
    }

    public PropertyPredicate(Predicate<State<?, ?>> predicate) {
        this.predicate = predicate;
    }

    public static DataResult<PropertyPredicate> parse(String statement) {
        return DataResult.success(PATTERN.matcher(statement)).flatMap(matcher -> {
            if (matcher.matches()) {
                String property = matcher.group(1);
                String symbol = matcher.group(2);
                String value = matcher.group(3);
                return Comparator.parse(symbol).map(comparator -> new PropertyPredicate(property, comparator, value));
            }
            return DataResult.error(() -> "Not a statement;");
        }).mapError(error -> "Failed to parse property predicate '" + statement + "': " + error);
    }

    @Override
    public boolean test(State<?, ?> state) {
        return predicate.test(state);
    }

    @Override
    public String toString() {
        return this.predicate.toString();
    }

    private final class Unbaked implements Predicate<State<?, ?>> {
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
            PropertyPredicate.this.predicate = this.bake(state.getProperties());
            return PropertyPredicate.this.predicate.test(state);
        }

        @Override
        public String toString() {
            return this.property + this.comparator.toString() + this.value;
        }

        private <T extends Comparable<T>> Baked<T> bake(Property<T> property) {
            T value = property.parse(this.value).orElseThrow(() -> new IllegalStateException("Unkown value '" + this.value + "' for property '" + this.property + "';"));
            return new Baked<>(property, this.comparator, value);
        }

        private Baked<?> bake(Collection<Property<?>> properties) {
            Property<?> bakedProperty = null;
            for (Property<?> property : properties) {
                if (property.getName().equals(this.property)) {
                    bakedProperty = property;
                    break;
                }
            }
            if (bakedProperty == null) {
                //TODO
                throw new IllegalStateException("Unkown property '" + this.property + "';");
            }
            return bake(bakedProperty);
        }
    }

    private final class Baked<T extends Comparable<T>> implements Predicate<State<?, ?>> {
        private final Property<T> property;
        private final Comparator comparator;
        private final T value;

        private Baked(Property<T> property, Comparator comparator, T value) {
            this.property = property;
            this.comparator = comparator;
            this.value = value;
        }

        @Override
        public boolean test(State<?, ?> state) {
            return comparator.compare(state.get(property), value);
        }

        @Override
        public String toString() {
            return this.property.getName() + this.comparator.toString() + this.property.name(this.value);
        }
    }
}
