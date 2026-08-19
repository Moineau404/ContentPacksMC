package mod.moineau.contentpacks.api.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

public final class FileUtil {
    public static void write(File file, String content) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
        Writer writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    public static void writeLines(File file, Collection<String> lines) throws IOException {
        write(file, String.join("\n", lines));
    }
}
