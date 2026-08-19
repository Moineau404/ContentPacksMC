package mod.moineau.contentpacks.util;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.util.WritingLogger;
import mod.moineau.contentpacks.resource.ContentOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorLogger extends WritingLogger {
    public static final ErrorLogger LOAD = new ErrorLogger("errors") {
        @Override
        public void flush() {
            super.flush();
            if (this.count() > 0) ContentPacks.LOGGER.error("{} errors encountered while loading content!", this.count());
        }
    };
    public static final ErrorLogger OUTPUT = new ErrorLogger("output_errors") {
        @Override
        public void flush() {
            super.flush();
            if (this.count() > 0) ContentOutput.LOGGER.warn("{} errors encountered while outputing content!", this.count());
        }
    };

    public ErrorLogger(String fileName) {
        super(FabricLoader.getInstance().getGameDir().resolve("logs").resolve(ContentPacks.MOD_ID).resolve(fileName + ".log").toFile());
    }

    public void write(ResourceKey<?> resourceKey, String error) {
        this.write(resourceKey.identifier(), resourceKey.registry(), error);
    }

    public void write(Identifier id, Identifier registryId, String error) {
        this.error(String.format("{%s} [%s] %s", registryId, id, error));
    }

    public void write(Identifier id, Resource resource, String error) {
        this.error(String.format("(%s) [%s] %s", resource.sourcePackId(), id, error));
    }

    public void write(Identifier id, List<Resource> resources, String error) {
        this.error(String.format("(%s) [%s] %s", resources.stream().map(Resource::sourcePackId).collect(Collectors.joining(" | ")), id, error));
    }

    public int count() {
        return this.lines.size();
    }
}
