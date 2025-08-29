package mod.moineau.contentpacks.resource;

import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ResourceLoader extends Comparable<ResourceLoader> {
    void load(ResourceManager resourceManager);

    default int getPriority() {
        return 0;
    }

    @Override
    default int compareTo(@NotNull ResourceLoader resourceLoader) {
        return this.getPriority() - resourceLoader.getPriority();
    }
}
