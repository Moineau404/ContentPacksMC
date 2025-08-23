package mod.moineau.contentpacks.resource;

import net.minecraft.resource.ResourceManager;

// TODO Merge prepare/apply
public abstract class ContentLoaderOld<T> {
    public final void load(ResourceManager resourceManager) {
        T prepared = this.prepare(resourceManager);
        this.apply(prepared, resourceManager);
    }

    protected abstract T prepare(ResourceManager manager);

    protected abstract void apply(T prepared, ResourceManager manager);
}
