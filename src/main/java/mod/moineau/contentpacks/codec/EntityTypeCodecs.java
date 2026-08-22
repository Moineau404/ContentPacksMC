package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.api.util.CodecUtil;
import mod.moineau.api.util.FunctionUtil;
import mod.moineau.contentpacks.mixin.EntityAttachmentsAccessor;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class EntityTypeCodecs {
    public static final MapCodec<EntityType.EntityFactory<?>> ENTITY_FACTORY = ContentRegistries.ENTITY_FACTORY.byNameCodec().dispatchMap(FunctionUtil::nothing, Function.identity());
    public static final Codec<EntityAttachments> ENTITY_ATTACHMENTS = CodecUtil.enumMap(EntityAttachment.class, Vec3.CODEC.listOf())
            .xmap(EntityAttachments::new, attachments -> ((EntityAttachmentsAccessor) attachments).getAttachments()).codec();
    public static final Codec<EntityDimensions> ENTITY_DIMENSIONS = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("width").forGetter(EntityDimensions::width),
            Codec.FLOAT.fieldOf("height").forGetter(EntityDimensions::height),
            Codec.FLOAT.fieldOf("eye_height").forGetter(EntityDimensions::eyeHeight),
            ENTITY_ATTACHMENTS.fieldOf("attachments").forGetter(EntityDimensions::attachments),
            Codec.BOOL.fieldOf("fixed").forGetter(EntityDimensions::fixed)
    ).apply(instance, EntityDimensions::new));
    public static final Codec<EntityType<?>> CODEC = RecordCodecBuilder.<EntityType<?>>mapCodec(instance -> instance.group(
            MapCodec.of(
                    Encoder.empty(),
                    ENTITY_FACTORY
            ).forGetter(entityType -> entityType.factory),
            MobCategory.CODEC.fieldOf("category").forGetter(EntityType::getCategory),
            Codec.BOOL.optionalFieldOf("serialize", true).forGetter(EntityType::canSerialize),
            Codec.BOOL.optionalFieldOf("summon", true).forGetter(EntityType::canSummon),
            Codec.BOOL.optionalFieldOf("fire_immune", false).forGetter(EntityType::fireImmune),
            Codec.BOOL.optionalFieldOf("can_spawn_far", false).forGetter(EntityType::canSpawnFarFromPlayer),
            TagKey.hashedCodec(Registries.BLOCK).optionalFieldOf("immune_to", BlockTags.DEFAULT_IMMUNE_TO).forGetter(entityType -> entityType.immuneTo),
            CodecUtil.optional(ENTITY_DIMENSIONS, "dimensions", () -> EntityDimensions.scalable(0.6F, 1.8F)).forGetter(EntityType::getDimensions),
            Codec.FLOAT.optionalFieldOf("scale", 1.0f).forGetter(entityType -> entityType.spawnDimensionsScale),
            Codec.INT.optionalFieldOf("client_tracking_range", 5).forGetter(EntityType::clientTrackingRange),
            Codec.INT.optionalFieldOf("update_interval", 3).forGetter(EntityType::updateInterval),
            VanillaCodecs.description(Registries.ENTITY_TYPE, "entity").forGetter(EntityType::getDescriptionId),
            VanillaCodecs.lootTable("entities").forGetter(EntityType::getDefaultLootTable),
            FeatureFlags.CODEC.optionalFieldOf("required_features", FeatureFlags.VANILLA_SET).forGetter(EntityType::requiredFeatures),
            Codec.BOOL.optionalFieldOf("allowed_in_peaceful", true).forGetter(EntityType::isAllowedInPeaceful)
    ).apply(instance, EntityType::new)).codec();
}