package mod.moineau.contentpacks.client.render.entity;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.Map;
import java.util.Optional;

public final class ChestRendering {
    public static final Map<Identifier, MultiblockChestResources<SpriteId>> SPRITES = new Object2ObjectOpenHashMap<>();

    public static void bootStrap() {
        CustomTextureProvider.CHESTS.forEach(chest -> {
            if (chest instanceof AbstractChestBlock<?> block) {
                register(block);
            }
        });
    }

    public static void register(AbstractChestBlock<?> block) {
        Optional<Identifier> customTexture = ((CustomTextureProvider) block).contentpacks$getCustomTexture();
        if (customTexture.isPresent()) {
            Identifier id = customTexture.get();
            if (block instanceof ChestBlock) {
                SPRITES.put(id, createDefaultTextures(id).map(Sheets.CHEST_MAPPER::apply));
            } else {
                SPRITES.put(id, new MultiblockChestResources<>(Sheets.CHEST_MAPPER.apply(id), null, null));
            }
        }
    }

    private static MultiblockChestResources<Identifier> createDefaultTextures(final Identifier id) {
        String namespace = id.getNamespace();
        String path = id.getPath();
        return new MultiblockChestResources<>(
                id,
                Identifier.fromNamespaceAndPath(namespace, path + "_left"),
                Identifier.fromNamespaceAndPath(namespace, path + "_right")
        );
    }

    public static SpriteId chooseCustomSprite(final ChestRenderState state, final ChestRenderState.ChestMaterialType materialType, final ChestType type) {
        Optional<Identifier> customTexture = ((CustomTextureProvider) state).contentpacks$getCustomTexture();
        if (customTexture.isPresent()) {
            Identifier id = customTexture.get();
            MultiblockChestResources<SpriteId> selector = SPRITES.get(id);
            if (selector != null) {
                return selector.select(type);
            }
        }

        return Sheets.chooseSprite(materialType, type);
    }
}