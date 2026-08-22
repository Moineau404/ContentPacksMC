package mod.moineau.contentpacks.client.options;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.api.util.JsonUtil;
import mod.moineau.contentpacks.client.ContentPacksClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class ContentPacksOptions {
    private static final Logger LOGGER = ContentPacksClient.LOGGER;
    private static final File FILE = FabricLoader.getInstance().getConfigDir().resolve(ContentPacks.MOD_ID + ".json").toFile();
    private static final Codec<ContentPacksOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("enabled").forGetter(ContentPacksOptions::getEnabledPacks),
            Codec.STRING.listOf().fieldOf("disabled").forGetter(ContentPacksOptions::getDisabledPacks)
    ).apply(instance, ContentPacksOptions::new));
    private final List<String> enabledContentPacks = new ArrayList<>();
    private final List<String> disabledContentPacks = new ArrayList<>();

    private ContentPacksOptions(
            List<String> enabledContentPacks,
            List<String> disabledContentPacks
    ) {
        this.enabledContentPacks.addAll(enabledContentPacks);
        this.disabledContentPacks.addAll(disabledContentPacks);
    }

    private ContentPacksOptions() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Reads options from config file and returns a new instance.
     * Write and returns blank options if failed.
     */
    public static ContentPacksOptions read() {
        ContentPacksOptions options = null;

        if (FILE.exists()) {
            try {
                options = JsonUtil.parseOrPartial(FILE, CODEC, e -> LOGGER.warn("Partially read options: {}", e));
            } catch (Exception e) {
                LOGGER.error("Failed to read options:", e);
            }
        }

        if (options == null) {
            options = new ContentPacksOptions();
        }

        options.write();
        return options;
    }

    /**
     * Write options to config file.
     */
    public void write() {
        try {
            JsonUtil.write(FILE, this, CODEC);
        } catch (Exception e) {
            LOGGER.error("Failed to write options file:", e);
        }
    }

    /**
     * Updates enabled content packs in given pack repositiory.
     */
    public void updateRepository(PackRepository repository) {
        Set<String> set = new LinkedHashSet<>();

        for (String id : this.enabledContentPacks) {
            Pack profile = repository.getPack(id);

            if (profile == null && !id.startsWith("file/")) {
                profile = repository.getPack("file/" + id);
            }

            if (profile == null) {
                set.remove(id);
                LOGGER.warn("Removed content pack {} from config because it doesn't seem to exist anymore!", id);
            } else {
                set.add(profile.getId());
            }
        }

        repository.setSelected(set);
        this.refreshPacks(repository);
    }

    /**
     * Refreshes enabled and disabled pack lists from given pack repository.
     */
    public boolean refreshPacks(PackRepository repository) {
        List<String> old = ImmutableList.copyOf(this.enabledContentPacks);
        this.enabledContentPacks.clear();
        this.disabledContentPacks.clear();

        for (Pack profile : repository.getSelectedPacks()) {
            if (!profile.isFixedPosition()) {
                this.enabledContentPacks.add(profile.getId());
            }
        }

        for (Pack profile : repository.getAvailablePacks()) {
            if (!this.enabledContentPacks.contains(profile.getId())) {
                this.disabledContentPacks.add(profile.getId());
            }
        }

        this.write();
        return !old.equals(this.enabledContentPacks);
    }

    public List<String> getEnabledPacks() {
        return enabledContentPacks;
    }

    public List<String> getDisabledPacks() {
        return disabledContentPacks;
    }
}
