package mod.moineau.contentpacks.api.codec;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.dynamic.Codecs;

import java.util.Objects;

@Deprecated
public class Mapper<K, V> implements Codec<V> {
    private final BiMap<K, V> values = HashBiMap.create();
    private final Codec<V> codec;

    public Mapper(Codec<K> keyCodec) {
        BiMap<V, K> inverse = this.values.inverse();
        this.codec = Codecs.idChecked(keyCodec, this.values::get, inverse::get);
    }

    public Mapper<K, V> put(K key, V value) {
        Objects.requireNonNull(value, () -> "Value for " + key + " is null");
        this.values.put(key, value);
        return this;
    }

    public K getKey(V value) {
        return this.values.inverse().get(value);
    }

    public V getValue(K key) {
        return this.values.get(key);
    }

    public void removeByKey(K key) {
        this.values.remove(key);
    }

    public void removeByValue(V value) {
        this.values.inverse().remove(value);
    }

    @Override
    public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
        return codec.decode(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
        return codec.encode(input, ops, prefix);
    }
}
