package mod.moineau.contentpacks.render.entity;

import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.WoodType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public class SignRendering {
    public static final Map<WoodType, Identifier> HANGING_SIGN_EDIT_SCREEN_TEXTURES = new IdentityHashMap<>();

    public static void bootstrap() {
        for (Map.Entry<RegistryKey<WoodType>, WoodType> entry : ContentRegistries.WOOD_TYPE.getEntrySet()) {
            WoodType signType = entry.getValue();
            Identifier id = entry.getKey().getValue();
            TexturedRenderLayers.SIGN_TYPE_TEXTURES.put(signType, createSignTextureId(id));
            TexturedRenderLayers.HANGING_SIGN_TYPE_TEXTURES.put(signType, createHangingSignTextureId(id));
            HANGING_SIGN_EDIT_SCREEN_TEXTURES.put(signType, createHangingSignEditScreenTexturePath(id));
        }
    }

    public static @Nullable Identifier getHangingSignEditScreenTextureId(WoodType signType) {
        return HANGING_SIGN_EDIT_SCREEN_TEXTURES.get(signType);
    }

    private static SpriteIdentifier createSignTextureId(Identifier id) {
        return new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, Identifier.of(id.getNamespace(), "entity/signs/" + id.getPath()));
    }

    private static SpriteIdentifier createHangingSignTextureId(Identifier id) {
        return new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, Identifier.of(id.getNamespace(), "entity/signs/hanging/" + id.getPath()));
    }

    private static Identifier createHangingSignEditScreenTexturePath(Identifier id) {
        return Identifier.of(id.getNamespace(), "textures/gui/hanging_signs/" + id.getPath() + ".png");
    }
}
