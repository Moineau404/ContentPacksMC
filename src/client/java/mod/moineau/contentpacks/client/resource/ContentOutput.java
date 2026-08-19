package mod.moineau.contentpacks.client.resource;

import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.screen.ContentPacksScreen;
import mod.moineau.contentpacks.resource.ContentManager;
import mod.moineau.contentpacks.resource.RegistryLoader;
import mod.moineau.contentpacks.resource.ResourceLoader;
import mod.moineau.contentpacks.util.ErrorLogger;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;

public class ContentOutput {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Output");
    private static final Gson GSON = new GsonBuilder().setFormattingStyle(FormattingStyle.PRETTY).disableHtmlEscaping().create();
    public static final File DIRECTORY = FabricLoader.getInstance().getGameDir().resolve(".contentpacks.out").toFile();
    public static boolean isLoading = false;

    public static void output() {
        setLoading(true);
        Thread.startVirtualThread(() -> {
            DIRECTORY.mkdirs();

            if (DIRECTORY.isDirectory()) {
                Util.backgroundExecutor().forName("contentOutput").execute(() -> {
//                    try {
//                        FileUtils.cleanDirectory(DIRECTORY);
//                    } catch (IOException e) {
//                        LOGGER.error("Failed to clean output directory: {}", DIRECTORY.getAbsolutePath());
//                    }

                    for (ResourceLoader loader : ContentManager.getLoaders()) {
                        if (loader instanceof RegistryLoader<?> registryLoader) {
                            registryLoader.serializeAll().forEach(ContentOutput::write);
                        }
                    }

                    Util.getPlatform().openFile(DIRECTORY);
                    ErrorLogger.OUTPUT.flush();
                    SystemToast.add(
                            Minecraft.getInstance().gui.toastManager(),
                            ContentPacksClient.TOAST_OUTPUT,
                            Component.translatable("options.contentpacks.toast.output.title", ErrorLogger.LOAD.count()),
                            Component.translatable("options.contentpacks.toast.output.description")
                    );
                    setLoading(false);
                });
            } else {
                LOGGER.error("Failed to create output directory: {}", DIRECTORY.getAbsolutePath());
                setLoading(false);
            }
        });
    }

    private static void setLoading(boolean value) {
        isLoading = value;
        ContentPacksScreen.setOutputButtonActive(!value);
    }

    private static void write(Identifier path, JsonElement jsonElement) {
        try {
            File file = DIRECTORY.toPath().resolve(path.getNamespace()).resolve(path.getPath()).toFile();
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
