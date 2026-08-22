package mod.moineau.api.util;

import net.minecraft.resources.Identifier;

public final class IdentifierUtil {
    public static String toSlashId(Identifier id) {
        return id.getNamespace() + '/' + id.getPath();
    }

    public static Identifier toNormalId(String slashId) {
        return Identifier.parse(slashId.replaceFirst("/", ":"));
    }
}
