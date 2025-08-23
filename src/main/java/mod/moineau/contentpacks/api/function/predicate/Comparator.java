package mod.moineau.contentpacks.api.function.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.Codecs;

public sealed abstract class Comparator {
    public static final Comparator EQUAL = new Equal();
    public static final Comparator NOT_EQUAL = new NotEqual();
    public static final Comparator MORE = new More();
    public static final Comparator MORE_OR_EQUAL = new MoreOrEqual();
    public static final Comparator LESS = new Less();
    public static final Comparator LESS_OR_EQUAL = new LessOrEqual();
    private static final Codecs.IdMapper<String, Comparator> ID_MAPPER = new Codecs.IdMapper<>();
    public static final Codec<Comparator> CODEC = ID_MAPPER.getCodec(Codec.STRING);

    public abstract <T> boolean compare(Comparable<T> x, T y);

    static {
        ID_MAPPER.put("=", EQUAL);
        ID_MAPPER.put("!=", NOT_EQUAL);
        ID_MAPPER.put(">", MORE);
        ID_MAPPER.put(">=", MORE_OR_EQUAL);
        ID_MAPPER.put("<", LESS);
        ID_MAPPER.put("<=", LESS_OR_EQUAL);
    }

    private static final class Equal extends Comparator {
        @Override
        public <T> boolean compare(Comparable<T> x, T y) {
            return x.compareTo(y) == 0;
        }
    }

    private static final class NotEqual extends Comparator {
        @Override
        public <T> boolean compare(Comparable<T> x, T y) {
            return x.compareTo(y) != 0;
        }
    }

    private static final class More extends Comparator {
        @Override
        public <T> boolean compare(Comparable<T> x, T y) {
            return x.compareTo(y) > 0;
        }
    }

    private static final class MoreOrEqual extends Comparator {
        @Override
        public <T> boolean compare(Comparable<T> x, T y) {
            return x.compareTo(y) >= 0;
        }
    }

    private static final class Less extends Comparator {
        @Override
        public <T> boolean compare(Comparable<T> x, T y) {
            return x.compareTo(y) < 0;
        }
    }

    private static final class LessOrEqual extends Comparator {
        @Override
        public <T> boolean compare(Comparable<T> x, T y) {
            return x.compareTo(y) <= 0;
        }
    }
}