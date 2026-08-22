package mod.moineau.contentpacks.api.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Consumer;

public final class FileUtil {
    /**
     * Writes content to file. Throws if failed.
     */
    public static void write(File file, String content) throws IOException {
        FileUtils.write(file, content, Charset.defaultCharset());
    }

    /**
     * Writes content to file. Returns null if failed.
     */
    public static void writeSafe(File file, String content, Consumer<Exception> errorHandler) {
        try {
            write(file, content);
        } catch (Exception e) {
            errorHandler.accept(e);
        }
    }

    /**
     * Writes content to file. Returns null if failed.
     */
    public static void writeSafe(File file, String content) throws IOException {
        writeSafe(file, content, _ -> {});
    }
}
