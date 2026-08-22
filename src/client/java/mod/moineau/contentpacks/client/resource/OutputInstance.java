package mod.moineau.contentpacks.client.resource;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import mod.moineau.api.util.FileUtil;
import mod.moineau.api.util.JsonUtil;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.resource.RegistryLoader;
import mod.moineau.contentpacks.resource.RegistryOutput;
import mod.moineau.contentpacks.util.ErrorLogger;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public final class OutputInstance {
    private static final Logger LOGGER = ContentPacksClient.LOGGER;
    private static final Path DIRECTORY = FabricLoader.getInstance().getGameDir().resolve(".contentpacks.out");
    private static final Path CONTENT_DIRECTORY = DIRECTORY.resolve("content");
    private static final Path REGISTRIES_DIRECTORY = DIRECTORY.resolve("registries");
    private static OutputInstance instance;
    private final ErrorLogger logger = new ErrorLogger("output_errors") {
        @Override
        public void flush() {
            if (this.count() > 0) LOGGER.warn("{} errors encountered while outputing content!", this.count());
            super.flush();
        }
    };
    private final Collection<RegistryOutput<?>> outputs;
    private final Map<Identifier, JsonElement> entries = new HashMap<>();
    private final Map<Identifier, Set<String>> names = new HashMap<>();

    private OutputInstance(Collection<RegistryOutput<?>> outputs) {
        this.outputs = List.copyOf(outputs);
    }

    private void load() {
        outputs.forEach(output -> {
            Set<String> set =  new TreeSet<>();

            output.getResults().forEach((key, entry) -> {
                Identifier id = key.identifier();
                set.add(id.toString());

                try {
                    DataResult<JsonElement> result = entry.result();
                    if (result.hasResultOrPartial()) {
                        result.ifError(e -> this.logger.write(id, key.registry(), String.format("Partially encoded: %s", e.message())));
                    }

                    this.entries.put(entry.location(), result.getPartialOrThrow());
                } catch (Exception e) {
                    this.logger.write(id, key.registry(), String.format("Failed to encode: %s", e.getMessage()));
                }
            });

            this.names.put(output.getRegistry(), set);
        });
    }

    private void flush() {
        this.entries.forEach((location, json) -> {
            File file = CONTENT_DIRECTORY.resolve(location.getNamespace()).resolve(location.getPath()).toFile();
            try {
                JsonUtil.writeJson(file, json);
            } catch (IOException e) {
                logger.write(file, String.format("Failed to write file: %s", e.getMessage()));
            }
        });

        this.names.forEach((location, set) -> {
            File file = REGISTRIES_DIRECTORY.resolve(location.getNamespace() + "." + location.getPath() + ".txt").toFile();
            try {
                FileUtil.writeLines(file, set);
            } catch (IOException e) {
                logger.write(file, String.format("Failed to write file: %s", e.getMessage()));
            }
        });

        this.logger.flush();
    }

    public static void create(Collection<RegistryOutput<?>> outputs, Runnable onFinished) {
        if (instance != null) {
            LOGGER.warn("Output instance has already been created!");
            return;
        }

        instance = new OutputInstance(outputs);
        Thread.startVirtualThread(() -> {
            Util.backgroundExecutor().forName("contentOutput").execute(() -> {
                instance.load();
                instance.flush();
                instance = null;
                onFinished.run();

                SystemToast.add(
                        Minecraft.getInstance().gui.toastManager(),
                        ContentPacksClient.TOAST_OUTPUT,
                        Component.translatable("options.contentpacks.toast.output.title", ErrorLogger.LOAD.count()),
                        Component.translatable("options.contentpacks.toast.output.description")
                );
                Util.getPlatform().openFile(DIRECTORY.toFile());
            });
        });
    }

    public static void create(Runnable onFinished) {
        List<RegistryOutput<?>> outputs = new ArrayList<>();
        RegistryLoader.getLoaders().forEach(loader -> {
            outputs.add(loader.getOutput());
        });
        create(outputs, onFinished);
    }

    public static @Nullable OutputInstance getInstance() {
        return instance;
    }
}
