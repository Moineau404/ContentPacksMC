package mod.moineau.contentpacks.options;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.ContentPacks;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.StrictJsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// TODO Fix disabled list
public final class ContentPacksOptions {
    private static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Options");
    private static final Gson GSON = new GsonBuilder().setFormattingStyle(FormattingStyle.PRETTY).create();
    private static final File FILE = FabricLoader.getInstance().getConfigDir().resolve(ContentPacks.MOD_ID).resolve(ContentPacks.MOD_ID + ".json").toFile();
    private static final Codec<ContentPacksOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("enabled").forGetter(ContentPacksOptions::getEnabledContentPacks),
            Codec.STRING.listOf().fieldOf("disabled").forGetter(ContentPacksOptions::getDisabledContentPacks),
            Codec.BOOL.fieldOf("debug").forGetter(ContentPacksOptions::isDebugEnabled),
            Codec.BOOL.fieldOf("output").forGetter(ContentPacksOptions::isOutputEnabled)
    ).apply(instance, ContentPacksOptions::new));
    private final List<String> enabledContentPacks = new ArrayList<>();
    private final List<String> disabledContentPacks = new ArrayList<>();
    private boolean debug;
    private boolean output;

    private ContentPacksOptions(
            List<String> enabledContentPacks,
            List<String> disabledContentPacks,
            boolean debug,
            boolean output
    ) {
        this.enabledContentPacks.addAll(enabledContentPacks);
        this.disabledContentPacks.addAll(disabledContentPacks);
        this.debug = debug;
        this.output = output;
    }

    /**
     * Return new empty instance
     */
    private static ContentPacksOptions empty() {
        return new ContentPacksOptions(new ArrayList<>(), new ArrayList<>(), false, false);
    }

    /**
     * Read options from config file and return an instance,
     * or write blank options file
     */
    public static ContentPacksOptions read() {
        if (FILE.exists()) {
            try {
                Reader reader = new FileReader(FILE);
                JsonElement jsonElement = StrictJsonParser.parse(reader);
                DataResult<ContentPacksOptions> result = CODEC.parse(JsonOps.INSTANCE, jsonElement);
                ContentPacksOptions options = result.getPartialOrThrow();
                result.ifError(error -> LOGGER.error("Partially loaded options: {}", error.message()));
                return options;
            } catch (FileNotFoundException | JsonParseException | IllegalStateException e) {
                LOGGER.error("Failed to load options:", e);
            }
        } else {
            try {
                FILE.getParentFile().mkdirs();
                FILE.createNewFile();
                ContentPacksOptions empty = empty();
                empty.write();
                return empty;
            } catch (IOException e) {
                LOGGER.error("Failed to create options file:", e);
            }
        }
        return empty();
    }

    /**
     * Write options to config file
     */
    public void write() {
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(FILE));
            writer.setFormattingStyle(FormattingStyle.PRETTY);
            JsonElement jsonElement = CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow();
            GSON.toJson(jsonElement, writer);
            writer.flush();
            writer.close();
        } catch (IOException | JsonParseException e) {
            LOGGER.error("Failed to write options:", e);
        }
    }

    /**
     * Update enabled content packs in given pack manager
     */
    public void updatePackManager(ResourcePackManager manager) {
        Set<String> set = new LinkedHashSet<>();
        for (String pack : this.enabledContentPacks) {
            ResourcePackProfile profile = manager.getProfile(pack);
            if (profile == null && !pack.startsWith("file/")) {
                profile = manager.getProfile("file/" + pack);
            }
            if (profile == null) {
                set.remove(pack);
                ContentPacks.LOGGER.warn("Removed content pack {} from config because it doesn't seem to exist anymore!", pack);
            } else {
                set.add(profile.getId());
            }
        }
        manager.setEnabledProfiles(set);
        this.refreshPacks(manager);
    }

    /**
     * Refresh enabled content packs from given pack manager
     */
    public boolean refreshPacks(ResourcePackManager packManager) {
        List<String> old = ImmutableList.copyOf(this.enabledContentPacks);
        this.enabledContentPacks.clear();
        this.disabledContentPacks.clear();
        for (ResourcePackProfile profile : packManager.getEnabledProfiles()) {
            if (!profile.isPinned()) {
                this.enabledContentPacks.add(profile.getId());
            }
        }
        for (ResourcePackProfile profile : packManager.getProfiles()) {
            if (!this.enabledContentPacks.contains(profile.getId())) {
                this.disabledContentPacks.add(profile.getId());
            }
        }
        this.write();
        return !old.equals(this.enabledContentPacks);
    }

    public List<String> getEnabledContentPacks() {
        return enabledContentPacks;
    }

    public List<String> getDisabledContentPacks() {
        return disabledContentPacks;
    }

    public boolean isDebugEnabled() {
        return debug;
    }

    public boolean isOutputEnabled() {
        return output;
    }
}
