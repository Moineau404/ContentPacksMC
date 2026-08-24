package mod.moineau.contentpacks.api.client.render;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public final class CustomTextureRegistry {
    public static final MetadataSectionType<Identifier> METADATA_SECTION_TYPE = new MetadataSectionType<>("texture", Identifier.CODEC);
    private static final Map<Object, Identifier> TEXTURES = new WeakHashMap<>();

    public static void register(Object object, Identifier texture) {
        TEXTURES.put(object, texture);
    }

    public static @Nullable Identifier get(Object object) {
        return TEXTURES.get(object);
    }

    public static void pass(Object donor, Object recipient) {
        Identifier texture = TEXTURES.get(donor);
        if (texture != null) {
            register(recipient, texture);
        }
    }
}
