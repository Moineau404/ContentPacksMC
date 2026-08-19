package mod.moineau.contentpacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.phys.Vec3;

@Mixin(EntityAttachments.class)
public interface EntityAttachmentsAccessor {
    @Accessor("attachments")
    Map<EntityAttachment, List<Vec3>> getAttachments();
}
