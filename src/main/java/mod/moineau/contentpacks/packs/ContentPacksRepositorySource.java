package mod.moineau.contentpacks.packs;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.packrepos.PackRepos;
import mod.moineau.packrepos.integration.PackRepositoryProvider;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class ContentPacksRepositorySource implements RepositorySource, PackRepositoryProvider {
    private static final PackType PACK_TYPE = PackType.valueOf("CONTENTPACKS_CONTENT");
    private static final PackSelectionConfig SELECTION_CONFIG = new PackSelectionConfig(true, PackRepos.MIDDLE_PACK_POSITION, true);

    @Override
    public void loadPacks(final Consumer<Pack> result) {
        for (Pack pack : ContentPacks.getActivePacks()) {
            Pack requiredPack = Pack.readMetaAndCreate(pack.location(), pack.resources, PACK_TYPE, SELECTION_CONFIG);
            if (requiredPack != null) {
                result.accept(requiredPack);
            }
        }
    }

    @Override
    public Stream<RepositorySource> provideResourcePackRepositorySources() {
        return Stream.of(this);
    }

    @Override
    public Stream<RepositorySource> provideDataPackRepositorySources() {
        return Stream.of(this);
    }
}
