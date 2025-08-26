package mod.moineau.contentpacks.api.util;

import net.fabricmc.fabric.api.event.Event;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class CollectionUtil {
    public static <K, V> Map<V, Set<K>> flip(Map<K, V> map) {
        Map<V, Set<K>> map1 = new HashMap<>();
        map.forEach(((k, v) -> {
            map1.computeIfAbsent(v, (ignore) -> new HashSet<>()).add(k);
        }));
        return map1;
    }

    public static <K, V> Map<K, V> unflip(Map<V, Set<K>> map) {
        Map<K, V> map1 = new HashMap<>();
        map.forEach(((v, sk) -> {
            sk.forEach(k -> {
                map1.put(k, v);
            });
        }));
        return map1;
    }

    @ApiStatus.Experimental
    public static <T, E> List<T> lazyList(Supplier<Collection<T>> supplier, Event<E> event, Function<Runnable, E> callbackAdapter) {
        List<T> list = new ArrayList<>();
        event.register(callbackAdapter.apply(() -> list.addAll(supplier.get())));
        return list;
    }
}