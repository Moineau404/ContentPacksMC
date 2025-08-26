package mod.moineau.contentpacks.block;

import com.google.common.base.Splitter;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
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
@Deprecated
public class BlockPropertiesPredicate implements Predicate<BlockState> {
    private static final Splitter COMMA_SPLITTER = Splitter.on(',');
    private static final Splitter EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
    private static final BlockPropertiesPredicate TRUE = new BlockPropertiesPredicate("") {
        @Override
        public boolean test(@Nullable BlockState state) {
            return true;
        }
    };
    private static final BlockPropertiesPredicate FALSE = new BlockPropertiesPredicate(null) {
        @Override
        public boolean test(@Nullable BlockState state) {
            return false;
        }
    };
    public static final Codec<BlockPropertiesPredicate> CODEC = Codec.STRING
            .xmap(BlockPropertiesPredicate::new, BlockPropertiesPredicate::toString);

    protected final String predicate;

    protected BlockPropertiesPredicate(String predicate) {
        this.predicate = predicate;
    }

    public static BlockPropertiesPredicate of(String predicate) {
        if (predicate == null) {
            return FALSE;
        } else if (predicate.isEmpty()) {
            return TRUE;
        }
        return new BlockPropertiesPredicate(predicate);
    }

    @Override
    public boolean test(@NotNull BlockState state) {
        if (predicate.isEmpty()) {
            return true;
        }
        return parse(state.getBlock().getStateManager(), predicate).test(state);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String string) {
            return predicate.equals(string);
        } else if (obj instanceof BlockPropertiesPredicate predicate2) {
            return this.predicate.equals(predicate2.predicate);
        }
        return false;
    }

    @Override
    public String toString() {
        return predicate;
    }

    /**
     * Copied from net.minecraft.client.render.model.json.BlockPropertiesPredicate
     */
    public static <O, S extends State<O, S>> Predicate<State<O, S>> parse(StateManager<O, S> manager, String predicate) {
        if (predicate.isEmpty()) {
            return (ignore) -> true;
        } else {
            Map<Property<?>, Comparable<?>> map = new HashMap<>();

            for (String statement : COMMA_SPLITTER.split(predicate)) {
                Iterator<String> iterator = EQUAL_SIGN_SPLITTER.split(statement).iterator();
                if (iterator.hasNext()) {
                    String name = iterator.next();
                    Property<?> property = manager.getProperty(name);
                    if (property != null && iterator.hasNext()) {
                        String value = iterator.next();
                        @Nullable Comparable<?> comparable = parse(property, value);
                        if (comparable == null) {
                            throw new RuntimeException("Unknown value: '" + value + "' for blockstate property: '" + name + "' " + property.getValues());
                        }

                        map.put(property, comparable);
                    } else if (!name.isEmpty()) {
                        throw new RuntimeException("Unknown blockstate property: '" + name + "'");
                    }
                }
            }

            return state -> {
                for (Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {
                    if (!Objects.equals(state.get(entry.getKey()), entry.getValue())) {
                        return false;
                    }
                }

                return true;
            };
        }
    }

    /**
     * Copied from net.minecraft.client.render.model.json.BlockPropertiesPredicate
     */
    private static <T extends Comparable<T>> @Nullable T parse(Property<T> property, String value) {
        return property.parse(value).orElse(null);
    }
}

