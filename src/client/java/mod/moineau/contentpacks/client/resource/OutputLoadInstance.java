package mod.moineau.contentpacks.client.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import mod.moineau.api.util.FileUtil;
import mod.moineau.api.util.JsonUtil;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.resource.RegistryOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public final class OutputLoadInstance {
    private static final Logger LOGGER = ContentPacksClient.LOGGER;
    private static final Path DIRECTORY = FabricLoader.getInstance().getGameDir().resolve(".contentpacks.out");
    private static final Path CONTENT_DIRECTORY = DIRECTORY.resolve("content");
    private static final Path REGISTRIES_DIRECTORY = DIRECTORY.resolve("registries");
    private static final Path ERRORS_PATH = DIRECTORY.resolve("errors.log");
    private static OutputLoadInstance instance;
    private final Collection<RegistryOutput<?>> outputs;
    private final Map<Identifier, JsonElement> entries = new HashMap<>();
    private final Map<Identifier, JsonElement> metadatas = new HashMap<>();
    private final Map<Identifier, Set<String>> names = new HashMap<>();
    private final List<String> errors = new LinkedList<>();

    private OutputLoadInstance(Collection<RegistryOutput<?>> outputs) {
        this.outputs = List.copyOf(outputs);
    }

    private void load() {
        this.outputs.forEach(output -> {
            Set<String> set =  new TreeSet<>();

            output.getResults().forEach((key, entry) -> {
                Identifier id = key.identifier();
                set.add(id.toString());

                DataResult<JsonElement> result = entry.result();
                try {
                    if (result.hasResultOrPartial()) {
                        result.ifError(e -> this.errors.add(String.format("[%s / %s] Partially encoded: %s", key.registry(), id, e.message())));
                    }

                    this.entries.put(entry.location(), result.getPartialOrThrow());
                } catch (Exception e) {
                    this.errors.add(String.format("[%s / %s] Partially encoded: %s", key.registry(), id, e.getMessage()));
                }

                DataResult<JsonObject> metadataResult = entry.metadataResult();
                if (metadataResult.result().filter(JsonObject::isEmpty).isEmpty()) {
                    try {
                        if (metadataResult.hasResultOrPartial()) {
                            metadataResult.ifError(e -> this.errors.add(String.format("[%s / %s (metadata)] Partially encoded: %s", key.registry(), id, e.message())));
                        }

                        this.metadatas.put(entry.location().withSuffix(".mcmeta"), metadataResult.getPartialOrThrow());
                    } catch (Exception e) {
                        this.errors.add(String.format("[%s / %s (metadata)] Partially encoded: %s", key.registry(), id, e.getMessage()));
                    }
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
            } catch (Exception e) {
                this.errors.add(String.format("[%s] Failed to write file: %s", location, e.getMessage()));
            }
        });

        this.metadatas.forEach((location, json) -> {
            File file = CONTENT_DIRECTORY.resolve(location.getNamespace()).resolve(location.getPath()).toFile();
            try {
                JsonUtil.writeJson(file, json);
            } catch (Exception e) {
                this.errors.add(String.format("[%s] Failed to write file: %s", location, e.getMessage()));
            }
        });

        this.names.forEach((location, set) -> {
            File file = REGISTRIES_DIRECTORY.resolve(location.getNamespace() + "." + location.getPath() + ".txt").toFile();
            try {
                FileUtil.writeLines(file, set);
            } catch (Exception e) {
                this.errors.add(String.format("[%s] Failed to write file: %s", location, e.getMessage()));
            }
        });

        if (errors.isEmpty()) {
            LOGGER.info("Content outputted successfully!");
        } else {
            LOGGER.warn("Content outputted with {} errors!", errors.size());
        }
        FileUtil.writeLinesSafe(ERRORS_PATH.toFile(), errors, e -> LOGGER.error("Failed to write errors file:", e));
    }

    public static void create(Collection<RegistryOutput<?>> outputs, Runnable onFinished) {
        if (instance != null) {
            LOGGER.warn("Output instance has already been created!");
            return;
        }

        instance = new OutputLoadInstance(outputs);
        Thread.startVirtualThread(() -> {
            Util.backgroundExecutor().forName("contentOutput").execute(() -> {
                instance.load();
                instance.flush();
                instance = null;
                onFinished.run();

                SystemToast.add(
                        Minecraft.getInstance().gui.toastManager(),
                        ContentPacksClient.TOAST_OUTPUT,
                        Component.translatable("options.contentpacks.toast.output.title"),
                        Component.translatable("options.contentpacks.toast.output.description")
                );
                Util.getPlatform().openFile(DIRECTORY.toFile());
            });
        });
    }

    public static void create(Runnable onFinished) {
        create(ContentPacks.getInstance().getRegistryManager().getOutputs(), onFinished);
    }

    public static @Nullable OutputLoadInstance getInstance() {
        return instance;
    }
}
