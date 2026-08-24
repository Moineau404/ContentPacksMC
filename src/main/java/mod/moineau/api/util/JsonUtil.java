package mod.moineau.api.util;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.StrictJsonParser;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class JsonUtil {
    private static final Gson GSON = new GsonBuilder().setFormattingStyle(FormattingStyle.PRETTY).disableHtmlEscaping().create();

    /**
     * Returns {@link Gson} instance.
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * Reads JSON from reader.
     * Throws if failed.
     */
    public static JsonElement readJson(Reader reader) throws JsonParseException {
        return StrictJsonParser.parse(reader);
    }

    /**
     * Reads JSON from file.
     * Throws if failed.
     */
    public static JsonElement readJson(File file) throws IOException, JsonParseException {
        return readJson(new FileReader(file));
    }

    /**
     * Reads JSON from reader.
     * Returns null if failed.
     */
    public static @Nullable JsonElement readJsonSafe(Reader reader, Consumer<Exception> errorHandler) {
        try {
            return readJson(reader);
        } catch (Exception e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Reads JSON from file.
     * Returns null if failed.
     */
    public static @Nullable JsonElement readJsonSafe(File file, Consumer<Exception> errorHandler) {
        try {
            return readJson(file);
        } catch (Exception e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Reads JSON from reader.
     * Returns null if failed.
     */
    public static @Nullable JsonElement readJsonSafe(Reader reader) {
        return readJsonSafe(reader, _ -> {});
    }

    /**
     * Reads JSON from file.
     * Returns null if failed.
     */
    public static @Nullable JsonElement readJsonSafe(File file) {
        return readJsonSafe(file, _ -> {});
    }

    /**
     * Reads JSON {@link DataResult} from reader.
     * Returns {@link DataResult.Error} if failed.
     */
    public static DataResult<JsonElement> readResult(Reader reader) {
        try {
            return DataResult.success(readJson(reader));
        } catch (Exception e) {
            return DataResult.error(e::getMessage);
        }
    }

    /**
     * Reads JSON {@link DataResult} from file.
     * Returns {@link DataResult.Error} if failed.
     */
    public static DataResult<JsonElement> readResult(File file) {
        try {
            return DataResult.success(readJson(file));
        } catch (Exception e) {
            return DataResult.error(e::getMessage);
        }
    }

    /**
     * Parses {@link DataResult} from reader with codec.
     * Throws if failed.
     */
    public static <T> DataResult<T> parseResult(Reader reader, Codec<T> codec) throws IOException, JsonParseException {
        return codec.parse(JsonOps.INSTANCE, readJson(reader));
    }

    /**
     * Parses {@link DataResult} from file with codec.
     * Throws if failed.
     */
    public static <T> DataResult<T> parseResult(File file, Codec<T> codec) throws IOException, JsonParseException {
        return codec.parse(JsonOps.INSTANCE, readJson(file));
    }

    /**
     * Parses {@link DataResult} from reader with codec.
     * Returns {@link DataResult.Error} if failed.
     */
    public static <T> DataResult<T> parseResultSafe(Reader reader, Codec<T> codec) {
        try {
            return parseResult(reader, codec);
        } catch (Exception e) {
            return DataResult.error(e::getMessage);
        }
    }

    /**
     * Parses {@link DataResult} from file with codec.
     * Returns {@link DataResult.Error} if failed.
     */
    public static <T> DataResult<T> parseResultSafe(File file, Codec<T> codec) {
        try {
            return parseResult(file, codec);
        } catch (Exception e) {
            return DataResult.error(e::getMessage);
        }
    }

    /**
     * Parses object from reader with codec.
     * Throws if failed.
     */
    public static <T> T parse(Reader reader, Codec<T> codec) throws IOException, JsonParseException {
        return parseResult(reader, codec).getOrThrow(JsonParseException::new);
    }

    /**
     * Parses object from file with codec.
     * Throws if failed.
     */
    public static <T> T parse(File file, Codec<T> codec) throws IOException, JsonParseException {
        return parseResult(file, codec).getOrThrow(JsonParseException::new);
    }

    /**
     * Parses object from reader with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseSafe(Reader reader, Codec<T> codec, Consumer<Exception> errorHandler) {
        try {
            return parse(reader, codec);
        } catch (Exception e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Parses object from file with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseSafe(File file, Codec<T> codec, Consumer<Exception> errorHandler) {
        try {
            return parse(file, codec);
        } catch (Exception e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Parses object from reader with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseSafe(Reader reader, Codec<T> codec) {
        return parseSafe(reader, codec, _ -> {});
    }

    /**
     * Parses object from file with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseSafe(File file, Codec<T> codec) {
        return parseSafe(file, codec, _ -> {});
    }

    /**
     * Parses object from reader with codec.
     * Throws if failed.
     */
    public static <T> T parseOrPartial(Reader reader, Codec<T> codec) throws IOException, JsonParseException {
        return parseResult(reader, codec).getPartialOrThrow(JsonParseException::new);
    }

    /**
     * Parses object from file with codec.
     * Throws if failed.
     */
    public static <T> T parseOrPartial(File file, Codec<T> codec) throws IOException, JsonParseException {
        return parseResult(file, codec).getPartialOrThrow(JsonParseException::new);
    }

    /**
     * Parses object from reader with codec.
     * Throws if failed.
     */
    public static <T> T parseOrPartial(Reader reader, Codec<T> codec, Consumer<String> partialErrorHandler) throws IOException, JsonParseException {
        return parseResult(reader, codec).promotePartial(partialErrorHandler).getOrThrow(JsonParseException::new);
    }

    /**
     * Parses object from file with codec.
     * Throws if failed.
     */
    public static <T> T parseOrPartial(File file, Codec<T> codec, Consumer<String> partialErrorHandler) throws IOException, JsonParseException {
        return parseResult(file, codec).promotePartial(partialErrorHandler).getOrThrow(JsonParseException::new);
    }

    /**
     * Parses object from reader with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseOrPartialSafe(Reader reader, Codec<T> codec, Consumer<Exception> errorHandler) {
        try {
            return parseResult(reader, codec).getPartialOrThrow(JsonParseException::new);
        } catch (IOException e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Parses object from reader with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseOrPartialSafe(File file, Codec<T> codec, Consumer<Exception> errorHandler) {
        try {
            return parseResult(file, codec).getPartialOrThrow(JsonParseException::new);
        } catch (IOException e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Parses object from reader with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseOrPartialSafe(Reader reader, Codec<T> codec) {
        return parseOrPartialSafe(reader, codec, _ -> {});
    }

    /**
     * Parses object from file with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseOrPartialSafe(File file, Codec<T> codec) {
        return parseOrPartialSafe(file, codec, _ -> {});
    }

    /**
     * Parses object from reader with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseOrPartialSafe(Reader reader, Codec<T> codec, Consumer<String> partialErrorHandler, Consumer<Exception> errorHandler) {
        try {
            return parseResult(reader, codec).promotePartial(partialErrorHandler).getPartialOrThrow(JsonParseException::new);
        } catch (IOException e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Parses object from file with codec.
     * Returns null if failed.
     */
    public static <T> @Nullable T parseOrPartialSafe(File file, Codec<T> codec, Consumer<String> partialErrorHandler, Consumer<Exception> errorHandler) {
        try {
            return parseResult(file, codec).promotePartial(partialErrorHandler).getPartialOrThrow(JsonParseException::new);
        } catch (IOException e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Writes JSON on file with codec. Throws if failed.
     */
    public static void writeJson(File file, JsonElement json) throws IOException {
        file.getParentFile().mkdirs();
        JsonWriter writer = new JsonWriter(new FileWriter(file));
        writer.setFormattingStyle(FormattingStyle.PRETTY);
        GSON.toJson(json, writer);
        writer.flush();
        writer.close();
    }

    /**
     * Writes JSON on file with codec. Returns true if successful, false if failed.
     */
    public static boolean writeJsonSafe(File file, JsonElement json, Consumer<Exception> errorHandler) {
        try {
            writeJson(file, json);
            return true;
        } catch (Exception e) {
            errorHandler.accept(e);
            return false;
        }
    }

    /**
     * Writes JSON on file with codec. Returns true if successful, false if failed.
     */
    public static boolean writeJsonSafe(File file, JsonElement json) {
        return writeJsonSafe(file, json, _ -> {});
    }

    /**
     * Writes object on file with codec. Throws if failed.
     */
    public static <T> void write(File file, T element, Codec<T> codec) throws IOException, JsonParseException {
        writeJson(file, codec.encodeStart(JsonOps.INSTANCE, element).getOrThrow(JsonParseException::new));
    }

    /**
     * Writes object on file with codec. Returns true if successful, false if failed.
     */
    public static <T> boolean writeSafe(File file, T element, Codec<T> codec, Consumer<Exception> errorHandler) {
        try {
            writeJson(file, codec.encodeStart(JsonOps.INSTANCE, element).getOrThrow(JsonParseException::new));
            return true;
        } catch (Exception e) {
            errorHandler.accept(e);
            return false;
        }
    }

    /**
     * Writes object on file with codec. Returns true if successful, false if failed.
     */
    public static <T> boolean writeSafe(File file, T element, Codec<T> codec) {
        return writeSafe(file, element, codec, _ -> {});
    }

    @Deprecated(forRemoval = true)
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
}
