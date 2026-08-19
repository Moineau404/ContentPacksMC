package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.IdentifierUtil;
import mod.moineau.contentpacks.mixin.EntityAttachmentsAccessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;

public final class VanillaCodecs {
    public static final RecordCodecBuilder<?, Identifier> INJECT_ID = CodecUtil.unilateral(Identifier.CODEC.fieldOf("$id"));
    public static final RecordCodecBuilder<?, String> INJECT_SLASH_ID = CodecUtil.unilateral(Identifier.CODEC.fieldOf("$id").map(IdentifierUtil::toSlashId));
    public static final Codec<PushReaction> PISTON_BEHAVIOR = CodecUtil.enumByName(PushReaction.class);
    public static final Codec<BlockBehaviour.OffsetType> BLOCK_OFFSET_TYPE = CodecUtil.enumByName(BlockBehaviour.OffsetType.class);
    public static final Codec<NoteBlockInstrument> NOTE_BLOCK_INSTRUMENT = CodecUtil.enumByName(NoteBlockInstrument.class);
    public static final Codec<BlockSetType.PressurePlateSensitivity> ACTIVATION_RULE = CodecUtil.enumByName(BlockSetType.PressurePlateSensitivity.class);
    public static final Codec<EntityAttachment> ENTITY_ATTACHMENT_TYPE = CodecUtil.enumByName(EntityAttachment.class);
    public static final Codec<MobCategory> SPAWN_GROUP = CodecUtil.enumByName(MobCategory.class);
    public static final Codec<EntityAttachments> ENTITY_ATTACHMENTS = CodecUtil.enumMap(EntityAttachment.class, Vec3.CODEC.listOf())
            .xmap(EntityAttachments::new, attachments -> ((EntityAttachmentsAccessor) attachments).getAttachments()).codec();
    public static final Codec<EntityDimensions> ENTITY_DIMENSIONS = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("width").forGetter(EntityDimensions::width),
            Codec.FLOAT.fieldOf("height").forGetter(EntityDimensions::height),
            Codec.FLOAT.fieldOf("eyeHeight").forGetter(EntityDimensions::eyeHeight),
            ENTITY_ATTACHMENTS.fieldOf("attachments").forGetter(EntityDimensions::attachments),
            Codec.BOOL.fieldOf("fixed").forGetter(EntityDimensions::fixed)
    ).apply(instance, EntityDimensions::new));

    @SuppressWarnings("unchecked")
    public static <O> RecordCodecBuilder<O, Identifier> fillInjectId() {
        return (RecordCodecBuilder<O, Identifier>) INJECT_ID;
    }

    @SuppressWarnings("unchecked")
    public static <O> RecordCodecBuilder<O, String> fillInjectSlashId() {
        return (RecordCodecBuilder<O, String>) INJECT_SLASH_ID;
    }

    public static <O, T> RecordCodecBuilder<O, ResourceKey<T>> fillInjectRegistryKey(ResourceKey<? extends Registry<T>> registry) {
        return CodecUtil.unilateral(ResourceKey.codec(registry).fieldOf("$id"));
    }

//    public static <T> Codec<EitherHolder<T>> createLazyRegistryEntry(ResourceKey<Registry<T>> registryRef, Codec<Holder<T>> entryCodec) {
//        return Codec.either(entryCodec, ResourceKey.codec(registryRef)).xmap(EitherHolder::new, EitherHolder::contents);
//    }
}