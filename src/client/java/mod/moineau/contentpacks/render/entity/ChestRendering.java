package mod.moineau.contentpacks.render.entity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mod.moineau.contentpacks.block.CustomTextureProvider;
import net.minecraft.block.Block;
import net.minecraft.block.TrappedChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ChestRendering {
    private static final LoadingCache<Block, SpriteIdentifier> SINGLE_CHEST_TEXTURES = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public @NotNull SpriteIdentifier load(@NotNull Block block) {
            Optional<Identifier> texture = CustomTextureProvider.getCustomTexture(block);
            if (texture.isEmpty()) {
                throw new IllegalArgumentException("Cannot load custom texture for block with entity " + block + ": undefined");
            }
            return block instanceof TrappedChestBlock
                    ? createChestTextureId(texture.get(), "trapped")
                    : createChestTextureId(texture.get(), "normal");
        }
    });
    private static final LoadingCache<Block, SpriteIdentifier> LEFT_CHEST_TEXTURES = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public @NotNull SpriteIdentifier load(@NotNull Block block) {
            Optional<Identifier> texture = CustomTextureProvider.getCustomTexture(block);
            if (texture.isEmpty()) {
                throw new IllegalArgumentException("Cannot load custom texture for block with entity " + block + ": undefined");
            }
            return block instanceof TrappedChestBlock
                    ? createChestTextureId(texture.get(), "trapped_left")
                    : createChestTextureId(texture.get(), "normal_left");
        }
    });
    private static final LoadingCache<Block, SpriteIdentifier> RIGHT_CHEST_TEXTURES = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public @NotNull SpriteIdentifier load(@NotNull Block block) {
            Optional<Identifier> texture = CustomTextureProvider.getCustomTexture(block);
            if (texture.isEmpty()) {
                throw new IllegalArgumentException("Cannot load custom texture for block with entity " + block + ": undefined");
            }
            return block instanceof TrappedChestBlock
                    ? createChestTextureId(texture.get(), "trapped_right")
                    : createChestTextureId(texture.get(), "normal_right");
        }
    });

    public static SpriteIdentifier getChestTextureId(ChestType type, Block block) {
        try {
            return switch (type) {
                case LEFT -> LEFT_CHEST_TEXTURES.get(block);
                case RIGHT -> RIGHT_CHEST_TEXTURES.get(block);
                default -> SINGLE_CHEST_TEXTURES.get(block);
            };
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<SpriteIdentifier> getChestTexture(ChestType type, Block block) {
        if (CustomTextureProvider.hasCustomTexture(block)) {
            return Optional.of(getChestTextureId(type, block));
        }
        return Optional.empty();
    }

    private static SpriteIdentifier createChestTextureId(Identifier id, String variant) {
        return new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, Identifier.of(id.getNamespace(), "entity/chest/" + id.getPath() + "/" + variant));
    }

    private static SpriteIdentifier createSingleChestTextureId(Identifier id) {
        return new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, Identifier.of(id.getNamespace(), "entity/chest/" + id.getPath()));
    }
}
