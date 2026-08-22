package mod.moineau.api.util;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("all")
public final class FunctionUtil {
    /**
     * @deprecated Use {@link Function#identity()}.
     */
    @Deprecated()
    public static <S, T extends S> S self(T t) {
        return t;
    }

    public static <T, R> Function<T, R> constant(R value) {
        return o -> value;
    }

    @Nullable
    public static <T, R> R nothing(T t) {
        return null;
    }

    @Nullable
    public static <T1, T2, R> R nothing(T1 t1, T2 t2) {
        return null;
    }

    @Nullable
    public static <T1, T2, T3, R> R nothing(T1 t1, T2 t2, T3 t3) {
        return null;
    }

    public static <T> void ignore(T t) {}

    public static <T1, T2> void ignore(T1 t1, T2 t2) {}

    public static <T1, T2, T3> void ignore(T1 t1, T2 t2, T3 t3) {}

    public static boolean always() {
        return true;
    }

    public static <T1> boolean always(T1 t1) {
        return true;
    }

    public static <T1, T2> boolean always(T1 t1, T2 t2) {
        return true;
    }

    public static <T1, T2, T3> boolean always(T1 t1, T2 t2, T3 t3) {
        return true;
    }

    public static <T1, T2, T3, T4> boolean always(T1 t1, T2 t2, T3 t3, T4 t4) {
        return true;
    }

    public static boolean never() {
        return false;
    }

    public static <T1> boolean never(T1 t1) {
        return false;
    }

    public static <T1, T2> boolean never(T1 t1, T2 t2) {
        return false;
    }

    public static <T1, T2, T3> boolean never(T1 t1, T2 t2, T3 t3) {
        return false;
    }

    public static <T1, T2, T3, T4> boolean never(T1 t1, T2 t2, T3 t3, T4 t4) {
        return false;
    }

    public static <T, R> Optional<R> empty(T t) {
        return Optional.empty();
    }

    public static <O, T> Function<O, Optional<T>> optional(Function<O, T> function) {
        return function.andThen(Optional::of);
    }

    @SuppressWarnings("unchecked")
    public static <O, T, R> Function<O, R> cast(Function<T, R> function) {
        return o -> function.apply((T) o);
    }

    @SuppressWarnings("unchecked")
    public static <O, T> O cast(T t) {
        return (O) t;
    }
}
