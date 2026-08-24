package mod.moineau.contentpacks.api.client.render;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mod.moineau.contentpacks.api.client.ContentPacksAPIClient;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;

public final class ChestSprites {
    private static final Logger LOGGER = ContentPacksAPIClient.LOGGER;
    private static final LoadingCache<Identifier, MultiblockChestResources<SpriteId>> SPRITES = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public @NotNull MultiblockChestResources<SpriteId> load(@NotNull Identifier texture) {
            return createDefaultTextures(texture).map(Sheets.CHEST_MAPPER::apply);
        }
    });

    private static MultiblockChestResources<Identifier> createDefaultTextures(final Identifier id) {
        String namespace = id.getNamespace();
        String path = id.getPath();
        return new MultiblockChestResources<>(
                id,
                Identifier.fromNamespaceAndPath(namespace, path + "_left"),
                Identifier.fromNamespaceAndPath(namespace, path + "_right")
        );
    }

    public static SpriteId chooseCustomSprite(final ChestRenderState state, final ChestRenderState.ChestMaterialType material, final ChestType type) {
        @Nullable Identifier texture = CustomTextureRegistry.get(state);
        if (texture != null) {
            try {
                return SPRITES.get(texture).select(type);
            } catch (ExecutionException e) {
                LOGGER.error("Failed to load texture {} for chest ({} / {}):", texture, material, type, e);
            }
        }
        return Sheets.chooseSprite(material, type);
    }
}