package mod.moineau.contentpacks.api.util;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.StrictJsonParser;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.util.function.Consumer;

public final class FileUtil {
    private static final Gson GSON = new GsonBuilder().setFormattingStyle(FormattingStyle.PRETTY).create();

    /**
     * Returns {@link Gson} instance.
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * Write content to file. Throws if failed.
     */
    public static void write(File file, String content) throws IOException {
        FileUtils.write(file, content, Charset.defaultCharset());
    }

    /**
     * Write content to file. Returns null if failed.
     */
    public static void writeSafe(File file, String content, Consumer<Exception> errorHandler) {
        try {
            write(file, content);
        } catch (Exception e) {
            errorHandler.accept(e);
        }
    }

    /**
     * Write content to file. Returns null if failed.
     */
    public static void writeSafe(File file, String content) throws IOException {
        writeSafe(file, content, _ -> {});
    }

    /**
     * Read JSON from file. Throws if failed.
     */
    public static JsonElement readJson(Reader reader) throws IOException, JsonParseException {
        return StrictJsonParser.parse(reader);
    }

    /**
     * Read JSON from file. Throws if failed.
     */
    public static JsonElement readJson(File file) throws IOException, JsonParseException {
        return readJson(new FileReader(file));
    }

    /**
     * Read JSON from file. Returns null if failed.
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
     * Read JSON from file. Returns null if failed.
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
     * Read JSON from file. Returns null if failed.
     */
    public static @Nullable JsonElement readJsonSafe(Reader reader) {
        return readJsonSafe(reader, _ -> {});
    }

    /**
     * Read JSON from file. Returns null if failed.
     */
    public static @Nullable JsonElement readJsonSafe(File file) {
        return readJsonSafe(file, _ -> {});
    }

    /**
     * Read {@link DataResult} from reader. Throws if failed.
     */
    public static <T> DataResult<T> parseJsonResult(Reader reader, Codec<T> codec) throws IOException, JsonParseException {
        return codec.parse(JsonOps.INSTANCE, readJson(reader));
    }

    /**
     * Read {@link DataResult} from file. Throws if failed.
     */
    public static <T> DataResult<T> parseJsonResult(File file, Codec<T> codec) throws IOException, JsonParseException {
        return codec.parse(JsonOps.INSTANCE, readJson(file));
    }

    /**
     * Read {@link DataResult} from reader. Returns {@link DataResult.Error} if failed.
     */
    public static <T> DataResult<T> parseJsonResultSafe(Reader reader, Codec<T> codec) {
        try {
            return parseJsonResult(reader, codec);
        } catch (Exception e) {
            return DataResult.error(e::getMessage);
        }
    }

    /**
     * Read {@link DataResult} from file. Returns {@link DataResult.Error} if failed.
     */
    public static <T> DataResult<T> parseJsonResultSafe(File file, Codec<T> codec) {
        try {
            return parseJsonResult(file, codec);
        } catch (Exception e) {
            return DataResult.error(e::getMessage);
        }
    }

    /**
     * Read object from reader with codec. Throws if failed.
     */
    public static <T> T parseJson(Reader reader, Codec<T> codec) throws IOException, JsonParseException {
        return parseJsonResult(reader, codec).getOrThrow(JsonParseException::new);
    }

    /**
     * Read object from file with codec. Throws if failed.
     */
    public static <T> T parseJson(File file, Codec<T> codec) throws IOException, JsonParseException {
        return parseJsonResult(file, codec).getOrThrow(JsonParseException::new);
    }

    /**
     * Read object from reader with codec. Returns null if failed.
     */
    public static <T> @Nullable T parseJsonSafe(Reader reader, Codec<T> codec, Consumer<Exception> errorHandler) {
        try {
            return parseJson(reader, codec);
        } catch (Exception e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Read object from file with codec. Returns null if failed.
     */
    public static <T> @Nullable T parseJsonSafe(File file, Codec<T> codec, Consumer<Exception> errorHandler) {
        try {
            return parseJson(file, codec);
        } catch (Exception e) {
            errorHandler.accept(e);
            return null;
        }
    }

    /**
     * Read object from reader with codec. Returns null if failed.
     */
    public static <T> @Nullable T parseJsonSafe(Reader reader, Codec<T> codec) {
        return parseJsonSafe(reader, codec, _ -> {});
    }

    /**
     * Read object from file with codec. Returns null if failed.
     */
    public static <T> @Nullable T parseJsonSafe(File file, Codec<T> codec) {
        return parseJsonSafe(file, codec, _ -> {});
    }
    
    /**
     * Write JSON on file with codec. Throws if failed.
     */
    public static void writeJson(File file, JsonElement json) throws IOException {
        JsonWriter writer = new JsonWriter(new FileWriter(file));
        GSON.toJson(json, writer);
        writer.flush();
        writer.close();
    }

    /**
     * Write JSON on file with codec. Returns true if successful, false if failed.
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
     * Write JSON on file with codec. Returns true if successful, false if failed.
     */
    public static boolean writeJsonSafe(File file, JsonElement json) {
        return writeJsonSafe(file, json, _ -> {});
    }

    /**
     * Write object on file with codec. Throws if failed.
     */
    public static <T> void write(File file, T element, Codec<T> codec) throws IOException, JsonParseException {
        writeJson(file, codec.encodeStart(JsonOps.INSTANCE, element).getOrThrow(JsonParseException::new));
    }

    /**
     * Write object on file with codec. Returns true if successful, false if failed.
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
     * Write object on file with codec. Returns true if successful, false if failed.
     */
    public static <T> boolean writeSafe(File file, T element, Codec<T> codec) {
        return writeSafe(file, element, codec, _ -> {});
    }
}
