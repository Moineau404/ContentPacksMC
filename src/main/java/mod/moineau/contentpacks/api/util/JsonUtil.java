package mod.moineau.contentpacks.api.util;

import com.google.common.collect.Maps;
import com.google.gson.*;

import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;

public final class JsonUtil {
    private static final Gson GSON = new GsonBuilder().setFormattingStyle(FormattingStyle.PRETTY).create();

    public static JsonElement inverse(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            Map<String, JsonElement> map = new LinkedHashMap<>(jsonElement.getAsJsonObject().asMap()).reversed();
            Map<String, JsonElement> map2 = Maps.transformValues(map, JsonUtil::inverse);
            JsonObject jsonObject = new JsonObject();
            map2.forEach(jsonObject::add);
            return jsonObject;
        }
        return jsonElement;
    }

    public static JsonElement read(Reader reader) throws JsonParseException {
        return GSON.fromJson(reader, JsonElement.class);
    }
}
