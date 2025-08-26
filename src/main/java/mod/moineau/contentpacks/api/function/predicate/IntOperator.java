package mod.moineau.contentpacks.api.function.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import mod.moineau.contentpacks.api.util.CodecUtil;

import java.util.Map;

public enum IntOperator {
    ADD("+") {
        @Override
        public int evaluate(int x, int y) {
            return x + y;
        }
    },
    SUBSTRACT("-") {
        @Override
        public int evaluate(int x, int y) {
            return x - y;
        }
    },
    MULTIPLY("*") {
        @Override
        public int evaluate(int x, int y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        @Override
        public int evaluate(int x, int y) {
            return x / y;
        }
    },
    POWER("^") {
        @Override
        public int evaluate(int x, int y) {
            return x ^ y;
        }
    };

    private static final Map<String, IntOperator> SYMBOLS = new Object2ReferenceArrayMap<>() {{
        for (IntOperator comparator : IntOperator.values()) {
            this.put(comparator.symbol, comparator);
        }
    }};
    public static final Codec<IntOperator> SYMBOL_CODEC = Codec.STRING.xmap(SYMBOLS::get, IntOperator::toString);
    public static final Codec<IntOperator> NAME_CODEC = CodecUtil.enumByName(IntOperator.class);
    private final String symbol;

    public static DataResult<IntOperator> parse(String symbol) {
        return DataResult.partialGet(SYMBOLS::get, () -> "Unkown symbol \"" + symbol + "\";").apply(symbol);
    }

    IntOperator(String symbol) {
        this.symbol = symbol;
    }

    public int evaluate(int x, int y) {
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