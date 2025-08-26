package mod.moineau.contentpacks.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mod.moineau.contentpacks.api.function.predicate.Comparator;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public sealed abstract class PropertyPredicate implements Predicate<State<?, ?>> {
    private static final Pattern PATTERN = Pattern.compile("(.+?)(!=|>=|<=|=|>|<)(.+)");
    public static final Codec<PropertyPredicate> CODEC = Codec.STRING.comapFlatMap(PropertyPredicate::parse, PropertyPredicate::toString);
    protected final Comparator comparator;

    protected PropertyPredicate(Comparator comparator) {
        this.comparator = comparator;
    }

    public static <T extends Comparable<T>> PropertyPredicate.Baked<T> of(Property<T> property, Comparator comparator, T value) {
        return new Baked<>(property, comparator, value);
    }

    public static DataResult<PropertyPredicate.Unbaked> parse(String statement) {
        return DataResult.success(PATTERN.matcher(statement)).flatMap(matcher -> {
            if (matcher.matches()) {
                String property = matcher.group(1);
                String symbol = matcher.group(2);
                String value = matcher.group(3);
                return mod.moineau.contentpacks.api.function.predicate.Comparator.parse(symbol).map(comparator -> new Unbaked(property, comparator, value));
            }
            return DataResult.error(() -> "Not a statement;");
        }).mapError(error -> "Failed to parse property predicate \"" + statement + "\": " + error);
    }

    public static final class Unbaked extends PropertyPredicate {
        private final String property;
        private final String value;

        private Unbaked(String property, Comparator comparator, String value) {
            super(comparator);
            this.property = property;
            this.value = value;
        }

        @Override
        public boolean test(State<?, ?> state) {
            return parse(state.getProperties()).flatMap(property -> test(state, property)).orElse(false);
        }

        private Optional<Property<?>> parse(Collection<Property<?>> properties) {
            for (Property<?> property : properties) {
                if (this.property.equals(property.getName())) {
                    return Optional.of(property);
                }
            }
            return Optional.empty();
        }

        private <T extends Comparable<T>> Optional<Boolean> test(State<?, ?> state, Property<T> property) {
            return property.parse(this.value).flatMap(value -> state.getOrEmpty(property)
                    .map(value2 -> this.comparator.compare(value2, value)));
        }

        @Override
        public String toString() {
            return this.property + this.comparator + this.value;
        }

        private DataResult<Property<?>> bakeProperty(Collection<Property<?>> properties) {
            for (Property<?> property : properties) {
                if (this.property.equals(property.getName())) {
                    return DataResult.success(property);
                }
            }
            return DataResult.error(() -> "Failed to parse property \"" + this.property + "\"");
        }

        private <T extends Comparable<T>> DataResult<Property.Value<T>> bakeValue(Property<T> property) {
            return property.parse(this.value).map(property::createValue).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Failed to parse value \"" + this.value + "\", for property " + property));
        }

        public DataResult<Baked<?>> bake(Collection<Property<?>> properties) {
            return bakeProperty(properties).flatMap(this::bakeValue).map(value -> new Baked<>(value, this.comparator));
        }
    }

    public static final class Baked<T extends Comparable<T>> extends PropertyPredicate {
        private final Property<T> property;
        private final T value;

        private Baked(Property<T> property, Comparator comparator, T value) {
            super(comparator);
            this.property = property;
            this.value = value;
        }

        private Baked(Property.Value<T> value, Comparator comparator) {
            this(value.property(), comparator, value.value());
        }

        @Override
        public boolean test(State<?, ?> state) {
            return state.getOrEmpty(this.property).map(value -> this.comparator.compare(value, this.value)).orElse(false);
        }

        @Override
        public String toString() {
            return property.getName() + this.comparator + property.name(this.value);
        }
    }

    @Override
    public abstract String toString();
}
