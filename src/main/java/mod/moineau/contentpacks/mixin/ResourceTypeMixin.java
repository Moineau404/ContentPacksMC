package mod.moineau.contentpacks.mixin;

import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Support content resource type ("content") in addition to client resources ("assets") and server data ("data")
 */
@Mixin(ResourceType.class)
@Unique
public class ResourceTypeMixin {
	@Shadow
	@Final
	@Mutable
	private static ResourceType[] field_14191;

	@Unique
	private static final ResourceType CONTENT = contentpacks$addVariant("CONTENT", "content");

	@Invoker("<init>")
	private static ResourceType contentpacks$invokeInit(String internalName, int internalId, String directory) {
		throw new AssertionError();
	}

	@SuppressWarnings("all")
	@Unique
	private static ResourceType contentpacks$addVariant(String internalName, String directory) {
        ArrayList<ResourceType> variants = new ArrayList<>(Arrays.asList(field_14191));
        ResourceType type = contentpacks$invokeInit(internalName, variants.getLast().ordinal() + 1, directory);
		variants.add(type);
        ResourceTypeMixin.field_14191 = variants.toArray(new ResourceType[0]);
		return type;
	}
}
