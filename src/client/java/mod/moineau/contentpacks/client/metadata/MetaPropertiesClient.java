package mod.moineau.contentpacks.client.metadata;

import mod.moineau.contentpacks.client.render.CustomTextureRegistry;
import mod.moineau.contentpacks.metadata.MetaProperty;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import static mod.moineau.contentpacks.metadata.MetaProperties.register;

public final class MetaPropertiesClient {
    public static final MetaProperty<Object, Identifier> TEXTURE = new MetaProperty<>("texture", Identifier.CODEC, CustomTextureRegistry::register);

    public static void bootStrap() {
        register(Registries.BLOCK, MetaPropertiesClient.TEXTURE);
    }
}
