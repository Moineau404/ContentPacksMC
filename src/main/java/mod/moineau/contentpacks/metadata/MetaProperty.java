package mod.moineau.contentpacks.metadata;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class MetaProperty<O, T> {
    private final String name;
    private final Codec<T> codec;
    private final Codec<Value<O, T>> valueCodec;
    private final BiConsumer<O, T> listener;
    private final Function<O, Optional<T>> getter;
    private final MetadataSectionType<Value<O, T>> section;

    @ApiStatus.Internal
    public MetaProperty(String name, Codec<T> codec, BiConsumer<O, T> listener, Function<O, Optional<T>> getter) {
        this.name = name;
        this.codec = codec;
        this.valueCodec = codec.xmap(value -> new Value<>(this, value), Value::value);
        this.listener = listener;
        this.getter = getter;
        this.section = new MetadataSectionType<>(this.name, this.valueCodec);
    }

    @ApiStatus.Internal
    public MetaProperty(String name, Codec<T> codec, BiConsumer<O, T> listener) {
        this(name, codec, listener, _ -> Optional.empty());
    }

    public MetaProperty(Identifier id, Codec<T> codec, BiConsumer<O, T> listener, Function<O, Optional<T>> getter) {
        this(id.toString(), codec, listener, getter);
    }

    public MetaProperty(Identifier id, Codec<T> codec, BiConsumer<O, T> listener) {
        this(id.toString(), codec, listener);
    }

    public Optional<Value<O, T>> getValue(ResourceMetadata metadata) {
        return metadata.getSection(this.section);
    }

    public Optional<Value<O, T>> getValue(O owner) {
        return this.getter.apply(owner).map(value -> new Value<>(this, value));
    }

    public String getName() {
        return name;
    }

    public Codec<T> getCodec() {
		return this.codec;
	}

    public Codec<Value<O, T>> getValueCodec() {
        return valueCodec;
    }

    public record Value<O, T>(MetaProperty<O, T> property, T value) {
        public void apply(O owner) {
            this.property.listener.accept(owner, this.value);
        }

        public DataResult<JsonElement> encode() {
            return this.property.codec.encodeStart(JsonOps.INSTANCE, this.value);
        }
    }
}