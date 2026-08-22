package mod.moineau.contentpacks.api.client.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mod.moineau.contentpacks.util.CustomTextureProvider;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class ChestRendering {
    private static final Map<Identifier, Provider> SPRITES = new Object2ObjectOpenHashMap<>();
    private static boolean bootStrap;

    public static void bootStrap() {
        if (bootStrap) return;
        bootStrap = true;
        CustomTextureProvider.MULTI_CHEST_BLOCKS.forEach(chest -> CustomTextureProvider.getOptionalTexture(chest).ifPresent(ChestRendering::registerMulti));
        CustomTextureProvider.SINGLE_CHEST_BLOCKS.forEach(chest -> CustomTextureProvider.getOptionalTexture(chest).ifPresent(ChestRendering::registerSingle));
    }

    public static void registerMulti(Identifier texture) {
        SPRITES.put(texture, new MultiProvider(createDefaultTextures(texture).map(Sheets.CHEST_MAPPER::apply)));
    }

    public static void registerSingle(Identifier texture) {
        SPRITES.put(texture, new SingleProvider(Sheets.CHEST_MAPPER.apply(texture)));
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

    public static SpriteId chooseCustomSprite(final ChestRenderState state, final ChestRenderState.ChestMaterialType material, final ChestType type) {
        @Nullable Identifier texture = CustomTextureProvider.getTexture(state);
        if (texture != null) {
            Provider provider = SPRITES.get(texture);
            if (provider != null) {
                return provider.select(type);
            }
        }

        return Sheets.chooseSprite(material, type);
    }

    @FunctionalInterface
    private interface Provider {
        SpriteId select(ChestType type);
    }

    private record MultiProvider(MultiblockChestResources<SpriteId> sprites) implements Provider {
        @Override
        public SpriteId select(ChestType type) {
            return sprites.select(type);
        }
    }

    private record SingleProvider(SpriteId sprite) implements Provider {
        @Override
        public SpriteId select(ChestType type) {
            return sprite;
        }
    }
}