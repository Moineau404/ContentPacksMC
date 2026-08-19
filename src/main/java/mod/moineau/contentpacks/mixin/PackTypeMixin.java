package mod.moineau.contentpacks.mixin;

import org.spongepowered.asm.mixin.*;

import net.minecraft.server.packs.PackType;

/**
 * Support game content pack type ("content") in addition to client resources ("assets") and server data ("data")
 */
@Mixin(PackType.class)
@Unique
public enum PackTypeMixin {
	CONTENTPACKS_CONTENT("content");

	@Shadow
	private PackTypeMixin(String directory) {}
}
