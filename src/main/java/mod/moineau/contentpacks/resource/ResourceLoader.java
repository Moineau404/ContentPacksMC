package mod.moineau.contentpacks.resource;

import net.minecraft.resource.ResourceManager;

@FunctionalInterface
public interface ResourceLoader {
    void load(ResourceManager resourceManager);
}
