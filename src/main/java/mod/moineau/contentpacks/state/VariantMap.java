package mod.moineau.contentpacks.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO Create a new format of block model definition ("assets/blocks" like "assets/items") like item model definition
public sealed abstract class VariantMap<T> {
    public static <T> Codec<VariantMap<T>> createCodec(Codec<T> elementCodec) {
        return Codec.unboundedMap(PropertiesPredicate.CODEC, elementCodec)
                .xmap(Unbaked::new, VariantMap::getPredicates);
    }

    public static <T> Baked<T> of(Map<PropertiesPredicate.Baked, T> predicates) {
        return new Baked<>(predicates);
    }

    public static <T> Baked<T> singleton(T value) {
        return new Baked<>(Map.of(PropertiesPredicate.empty(), value));
    }

    public abstract @Nullable T get(State<?, ?> state);

    protected abstract Map<PropertiesPredicate, T> getPredicates();

    public static final class Unbaked<T> extends VariantMap<T> {
        private final Map<PropertiesPredicate, T> predicates;

        private Unbaked(Map<PropertiesPredicate, T> predicates) {
            this.predicates = predicates;
        }

        @Override
        public @Nullable T get(State<?, ?> state) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Map<PropertiesPredicate, T> getPredicates() {
            return predicates;
        }

        public DataResult<Baked<T>> bake(Collection<Property<?>> properties) {
            DataResult<Map<PropertiesPredicate.Baked, T>> bakedPredicatesResult = DataResult.success(new HashMap<>());
            for (Map.Entry<PropertiesPredicate, T> value : this.predicates.entrySet()) {
                PropertiesPredicate predicate = value.getKey();
                DataResult<PropertiesPredicate.Baked> result = predicate instanceof PropertiesPredicate.Unbaked unbaked ? unbaked.bake(properties) : DataResult.success((PropertiesPredicate.Baked) predicate);
                bakedPredicatesResult = bakedPredicatesResult.apply2stable((map, baked) -> {
                    map.put(baked, value.getValue());
                    return map;
                }, result);
            }
            return bakedPredicatesResult.map(Baked::new);
        }
    }

    public static final class Baked<T> extends VariantMap<@Nullable T> {
        private final Map<PropertiesPredicate.Baked, T> predicates;

        private Baked(Map<PropertiesPredicate.Baked, T> predicates) {
            this.predicates = predicates;
        }

        @Override
        public @Nullable T get(State<?, ?> state) {
            for (Map.Entry<PropertiesPredicate.Baked, T> value : this.predicates.entrySet()) {
                if (value.getKey().test(state)) {
                    return value.getValue();
                }
            }
            return null;
        }

        @Override
        protected Map<PropertiesPredicate, @Nullable T> getPredicates() {
            return Map.copyOf(predicates);
        }
    }
}