package mod.moineau.api.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import mod.moineau.api.util.CodecUtil;

import java.util.Map;

public enum Comparator {
    EQUAL_TO("=") {
        @Override
        public <T extends Comparable<T>> boolean compare(T x, T y) {
            return x.compareTo(y) == 0;
        }
    },
    NOT_EQUAL_TO("!=") {
        @Override
        public <T extends Comparable<T>> boolean compare(T x, T y) {
            return x.compareTo(y) != 0;
        }
    },
    GREATER_THAN(">") {
        @Override
        public <T extends Comparable<T>> boolean compare(T x, T y) {
            return x.compareTo(y) > 0;
        }
    },
    GREATER_THAN_OR_EQUAL_TO(">=") {
        @Override
        public <T extends Comparable<T>> boolean compare(T x, T y) {
            return x.compareTo(y) >= 0;
        }
    },
    LESS_THAN("<") {
        @Override
        public <T extends Comparable<T>> boolean compare(T x, T y) {
            return x.compareTo(y) < 0;
        }
    },
    LESS_THAN_OR_EQUAL_TO("<=") {
        @Override
        public <T extends Comparable<T>> boolean compare(T x, T y) {
            return x.compareTo(y) <= 0;
        }
    };

    private static final Map<String, Comparator> SYMBOLS = new Object2ReferenceArrayMap<>() {{
        for (Comparator comparator : Comparator.values()) {
            this.put(comparator.symbol, comparator);
        }
    }};
    public static final Codec<Comparator> SYMBOL_CODEC = Codec.STRING.xmap(SYMBOLS::get, Comparator::toString);
    public static final Codec<Comparator> NAME_CODEC = CodecUtil.enumByName(Comparator.class);
    private final String symbol;

    public static DataResult<Comparator> parse(String symbol) {
        return DataResult.partialGet(SYMBOLS::get, () -> "Unkown symbol \"" + symbol + "\";").apply(symbol);
    }

    Comparator(String symbol) {
        this.symbol = symbol;
    }

    public <T extends Comparable<T>> boolean compare(T x, T y) {
        throw new IllegalStateException();
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    public String getName() {
        return this.name().toLowerCase();
    }
}