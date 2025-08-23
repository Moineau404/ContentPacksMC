package mod.moineau.contentpacks.mixin;

import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EntityAttachments;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(EntityAttachments.class)
public interface EntityAttachmentsAccessor {
    @Accessor("points")
    Map<EntityAttachmentType, List<Vec3d>> getPoints();
}
