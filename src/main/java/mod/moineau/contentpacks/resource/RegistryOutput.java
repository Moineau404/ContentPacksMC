package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.metadata.MetaProperties;
import mod.moineau.contentpacks.metadata.MetaProperty;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class RegistryOutput<T> {
    private final Identifier registry;
    private final Map<ResourceKey<T>, OutputResult> results;

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

            DataResult<JsonObject> metadataResult = DataResult.success(new JsonObject());
            List<MetaProperty.Value<T, ?>> metaPropertyValues = MetaProperties.get(registry.key(), entry.getValue());

            if (!metaPropertyValues.isEmpty()) {
                for (MetaProperty.Value<T, ?> propertyValue : metaPropertyValues) {
                    metadataResult = metadataResult.apply2((jsonObject, jsonElement) -> {
                        jsonObject.add(propertyValue.property().getName(), jsonElement);
                        return jsonObject;
                    }, propertyValue.encode());
                }
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

    public record OutputResult(Identifier location, DataResult<JsonElement> result, DataResult<JsonObject> metadataResult) {}
}
