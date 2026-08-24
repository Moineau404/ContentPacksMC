package mod.moineau.contentpacks.resource;

import net.minecraft.server.packs.resources.ResourceManager;

import java.util.function.Consumer;

public interface ResourceLoader {
    void load(ResourceManager resourceManager, Consumer<String> errorHandler);
}