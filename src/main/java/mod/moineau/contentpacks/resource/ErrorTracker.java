package mod.moineau.contentpacks.resource;

import mod.moineau.contentpacks.ContentPacks;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class ErrorTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/ErrorTracker");
    private static final File FILE = FabricLoader.getInstance().getConfigDir().resolve(ContentPacks.MOD_ID).resolve("errors.txt").toFile();
    private static final List<String> ERRORS = new LinkedList<>();

    public static void print(String error) {
        ERRORS.add(error);
    }

    public static void print(Identifier id, Resource resource, String error) {
        print("(" + resource.getPackId() + "//" + id + ") " + error);
    }

    public static void print(Identifier id, List<Resource> resources, String error) {
        print("(" + resources.stream().map(Resource::getPackId).collect(Collectors.joining("|")) + "//" + id + ") " + error);
    }

    public static void write() {
        try {
            FILE.getParentFile().mkdirs();
            FILE.createNewFile();
            Writer writer = new FileWriter(FILE);
            String errors = String.join("\n", ERRORS);
            writer.write(errors);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to write error file {}", FILE);
        }
    }
}
