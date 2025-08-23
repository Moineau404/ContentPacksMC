package mod.moineau.contentpacks.api.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
}
