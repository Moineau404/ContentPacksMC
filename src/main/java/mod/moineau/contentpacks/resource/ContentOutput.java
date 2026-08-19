package mod.moineau.contentpacks.resource;

import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//TODO : Make output just a button in the content packs screen showing a file popup asking for a directory to extract all content in. Make a loading screen using CompletableFuture etc...
public class ContentOutput {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Output");
    private static final Gson GSON = new GsonBuilder().setFormattingStyle(FormattingStyle.PRETTY).disableHtmlEscaping().create();
    private static final Path PATH = FabricLoader.getInstance().getGameDir().resolve(".contentpacks.out").resolve("content");

    public static void output() {
        try {
            Files.createDirectories(PATH);
        } catch (IOException e) {
            LOGGER.error("Failed to create output directory: {}", PATH);
            return;
        }

        for (ResourceLoader loader : ContentManager.getLoaders()) {
            if (loader instanceof RegistryLoader<?> registryLoader) {
                registryLoader.serializeAll().forEach(ContentOutput::write);
            }
        }
    }

    private static void write(Identifier path, JsonElement jsonElement) {
        try {
            File file = PATH.resolve(path.getNamespace()).resolve(path.getPath()).toFile();
            file.getParentFile().mkdirs();
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setFormattingStyle(FormattingStyle.PRETTY);
            GSON.toJson(jsonElement, writer);
            writer.flush();
            try {
                writer.close();
            } catch (Throwable ignored) {}
        } catch (Exception e) {
            LOGGER.error("Failed to write file {}", path, e);
        }
    }
}
