package mod.moineau.contentpacks.client.resource;

import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.render.entity.EntityAsset;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;

public class EntityAssetReloadListener extends DependentReloadListener<EntityType<?>, EntityAsset> {
    private static final String DIRECTORY = "entities";

    public EntityAssetReloadListener() {
        super(DIRECTORY, EntityAsset.CODEC);
    }

    @Override
    protected @Nullable EntityType<?> getDependence(Identifier id) {
        return BuiltInRegistries.ENTITY_TYPE.getValue(id);
    }

    @Override
    protected void loadEntry(EntityType<?> entityType, EntityAsset asset, Identifier id) {
        EntityRenderers.register((EntityType) entityType, (EntityRendererProvider) asset.renderer());
    }

    @Override
    protected void handleNullError(Identifier id) {
        ContentPacksClient.LOGGER.debug("Discovered unknown entity asset {}, ignoring", id);
    }

    @Override
    protected void handleReadingError(Identifier id, String pack, String message) {
        ContentPacksClient.LOGGER.error("Failed to load entity asset for entity type {} from pack {}: {}", id, pack, message);
    }
}