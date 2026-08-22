package mod.moineau.contentpacks.client.resource;

import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.render.entity.EntityAsset;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;

public final class EntityAssetReloadListener extends DependentReloadListener<EntityType<?>, EntityAsset> {
    public EntityAssetReloadListener() {
        super("entities", EntityAsset.CODEC);
    }

    @Override
    protected @Nullable EntityType<?> getDependence(Identifier id) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
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

    @Override
    protected void handlePartialError(Identifier id, String pack, String message) {
        ContentPacksClient.LOGGER.error("Partially loaded entity asset for entity type {} from pack {}: {}", id, pack, message);
    }
}