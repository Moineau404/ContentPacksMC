package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.api.modifier.Modifier;
import mod.moineau.contentpacks.api.modifier.ModifierType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class RegistryOutput<T> {
    private final Identifier registry;
    private final Map<ResourceKey<T>, OutputResult> results;

    @SuppressWarnings("unchecked")
    public RegistryOutput(Registry<T> registry, Codec<T> codec, UnaryOperator<Identifier> locationProvider) {
        this.registry = registry.key().identifier();
        this.results = new HashMap<>();
        registry.entrySet().forEach(entry -> {
            DataResult<JsonElement> result;
            try {
                result = codec.encodeStart(JsonOps.INSTANCE, entry.getValue());
            } catch (Exception e) {
                result = DataResult.error(e::getMessage);
            }

            DataResult<JsonElement> metadataResult;
            ModifierType<T> modifierType = (ModifierType<T>) ModifierType.getInstances().get(registry.key());
            if (modifierType != null) {
                metadataResult = DataResult.success(modifierType.getModifiers(entry.getValue())).flatMap(map -> {
                    if (!map.isEmpty()) {
                        DataResult<JsonObject> jsonResult = DataResult.success(new JsonObject());
                        for (Map.Entry<String, Pair<Modifier<T>, Codec<Modifier<T>>>> mapEntry : map.entrySet()) {
                            DataResult<JsonElement> valueResult = mapEntry.getValue().getSecond().encodeStart(JsonOps.INSTANCE, mapEntry.getValue().getFirst());
                            jsonResult = jsonResult.apply2((jsonObject, value) -> {
                                jsonObject.add(mapEntry.getKey(), value);
                                return jsonObject;
                            }, valueResult);
                        }

                        return jsonResult.map(Function.identity());
                    } else {
                        return DataResult.success(JsonNull.INSTANCE);
                    }
                });
            } else {
                metadataResult = DataResult.success(JsonNull.INSTANCE);
            }

            results.put(entry.getKey(), new OutputResult(locationProvider.apply(entry.getKey().identifier()), result, metadataResult));
        });
    }

    public Identifier getRegistry() {
        return registry;
    }

    public Map<ResourceKey<T>, OutputResult> getResults() {
        return results;
    }

    public record OutputResult(Identifier location, DataResult<JsonElement> result, DataResult<JsonElement> metadataResult) {}
}
