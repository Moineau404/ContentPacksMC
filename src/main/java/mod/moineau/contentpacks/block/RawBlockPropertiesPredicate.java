package mod.moineau.contentpacks.block;

import com.google.common.base.Splitter;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Practical encapsulation of {@link String string} block properties predicate ("property1=value1,property2=value2,...").
 */
public class RawBlockPropertiesPredicate extends BlockPropertiesPredicate {
    private static final Splitter COMMA_SPLITTER = Splitter.on(',');
    private static final Splitter EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
    public static final Codec<BlockPropertiesPredicate> CODEC = Codec.STRING
            .xmap(RawBlockPropertiesPredicate::new, BlockPropertiesPredicate::toString);

    protected RawBlockPropertiesPredicate(String predicate) {
        super(predicate);
    }

    public static RawBlockPropertiesPredicate of(String predicate) {
        return new RawBlockPropertiesPredicate(predicate);
    }

    @Override
    public boolean test(@NotNull BlockState state) {
        if (predicate.isEmpty()) {
            return true;
        }
        return parse(state, predicate).test(state);
    }

    /**
     * Copied from net.minecraft.client.render.model.json.BlockPropertiesPredicate
     */
    public static <O, S extends State<O, S>> Predicate<State<O, S>> parse(State<O, S> state, String predicate) {
        if (predicate.isEmpty()) {
            return (ignore) -> true;
        } else {
            Map<Property<?>, Comparable<?>> map = new HashMap<>();
            Map<String, Property<?>> names = new HashMap<>();
            state.getProperties().forEach(property -> names.put(property.getName(), property));

            for (String statement : COMMA_SPLITTER.split(predicate)) {
                Iterator<String> iterator = EQUAL_SIGN_SPLITTER.split(statement).iterator();
                if (iterator.hasNext()) {
                    String name = iterator.next();
                    Property<?> property = names.get(name);
                    if (property != null && iterator.hasNext()) {
                        String value = iterator.next();
                        @Nullable Comparable<?> comparable = property.parse(value).orElse(null);
                        if (comparable == null) {
                            throw new RuntimeException("Unknown value: '" + value + "' for blockstate property: '" + name + "' " + property.getValues());
                        }

                        map.put(property, comparable);
                    } else if (!name.isEmpty()) {
                        throw new RuntimeException("Unknown blockstate property: '" + name + "'");
                    }
                }
            }

            return state1 -> {
                for (Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {
                    if (!Objects.equals(state.get(entry.getKey()), entry.getValue())) {
                        return false;
                    }
                }

                return true;
            };
        }
    }
}

