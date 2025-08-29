package mod.moineau.contentpacks.api.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import mod.moineau.contentpacks.api.util.CodecUtil;

import java.util.Map;

public enum FloatOperator {
    ADD("+") {
        @Override
        public float evaluate(float x, float y) {
            return x + y;
        }
    },
    SUBSTRACT("-") {
        @Override
        public float evaluate(float x, float y) {
            return x - y;
        }
    },
    MULTIPLY("*") {
        @Override
        public float evaluate(float x, float y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        @Override
        public float evaluate(float x, float y) {
            return x / y;
        }
    };

    private static final Map<String, FloatOperator> SYMBOLS = new Object2ReferenceArrayMap<>() {{
        for (FloatOperator comparator : FloatOperator.values()) {
            this.put(comparator.symbol, comparator);
        }
    }};
    public static final Codec<FloatOperator> SYMBOL_CODEC = Codec.STRING.xmap(SYMBOLS::get, FloatOperator::toString);
    public static final Codec<FloatOperator> NAME_CODEC = CodecUtil.enumByName(FloatOperator.class);
    private final String symbol;

    public static DataResult<FloatOperator> parse(String symbol) {
        return DataResult.partialGet(SYMBOLS::get, () -> "Unkown symbol \"" + symbol + "\";").apply(symbol);
    }

    FloatOperator(String symbol) {
        this.symbol = symbol;
    }

    public float evaluate(float x, float y) {
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