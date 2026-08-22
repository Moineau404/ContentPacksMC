package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public record RegistryOutput<T>(Identifier registry, Map<ResourceKey<T>, Entry> results) {
    public RegistryOutput(Registry<T> registry, Codec<T> codec, UnaryOperator<Identifier> locationProvider) {
        this(registry.key().identifier(), new HashMap<>());
        registry.entrySet().forEach(entry -> {
            DataResult<JsonElement> result;
            try {
                result = codec.encodeStart(JsonOps.INSTANCE, entry.getValue());
            } catch (Exception e) {
                result = DataResult.error(e::getMessage);
            }
            results.put(entry.getKey(), new Entry(locationProvider.apply(entry.getKey().identifier()), result));
        });
    }

    public record Entry(Identifier location, DataResult<JsonElement> result) {}
}
