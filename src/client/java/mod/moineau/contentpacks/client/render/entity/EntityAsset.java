package mod.moineau.contentpacks.client.render.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public record EntityAsset(EntityRendererProvider<?> renderer) {
    public static final Codec<EntityAsset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityRendererTypes.CODEC.forGetter(EntityAsset::renderer)
    ).apply(instance, EntityAsset::new));
}