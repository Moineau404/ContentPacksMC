package mod.moineau.contentpacks.api.util;

import net.minecraft.util.Identifier;

public final class IdentifierUtil {
    public static String toSlashId(Identifier id) {
        return id.getNamespace() + '/' + id.getPath();
    }

    public static Identifier toNormalId(String slashId) {
        return Identifier.of(slashId.replaceFirst("/", ":"));
    }
}
