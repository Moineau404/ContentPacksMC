package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.IdentifierUtil;
import mod.moineau.contentpacks.mixin.EntityAttachmentsAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EntityAttachments;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public final class VanillaCodecs {
    public static final RecordCodecBuilder<?, Identifier> INJECT_ID = CodecUtil.unilateral(Identifier.CODEC.fieldOf("$id"));
    public static final RecordCodecBuilder<?, String> INJECT_SLASH_ID = CodecUtil.unilateral(Identifier.CODEC.fieldOf("$id").map(IdentifierUtil::toSlashId));
    public static final Codec<PistonBehavior> PISTON_BEHAVIOR = CodecUtil.enumByName(PistonBehavior.class);
    public static final Codec<AbstractBlock.OffsetType> BLOCK_OFFSET_TYPE = CodecUtil.enumByName(AbstractBlock.OffsetType.class);
    // TODO Content-driven note block instruments ? (by enum injection)
    public static final Codec<NoteBlockInstrument> NOTE_BLOCK_INSTRUMENT = CodecUtil.enumByName(NoteBlockInstrument.class);
    public static final Codec<BlockSetType.ActivationRule> ACTIVATION_RULE = CodecUtil.enumByName(BlockSetType.ActivationRule.class);
    public static final Codec<EntityAttachmentType> ENTITY_ATTACHMENT_TYPE = CodecUtil.enumByName(EntityAttachmentType.class);
    public static final Codec<SpawnGroup> SPAWN_GROUP = CodecUtil.enumByName(SpawnGroup.class);
    public static final Codec<EntityAttachments> ENTITY_ATTACHMENTS = CodecUtil.enumMap(EntityAttachmentType.class, Vec3d.CODEC.listOf())
            .xmap(EntityAttachments::new, attachments -> ((EntityAttachmentsAccessor) attachments).getPoints()).codec();
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

    public static <O, T> RecordCodecBuilder<O, RegistryKey<T>> fillInjectRegistryKey(RegistryKey<? extends Registry<T>> registry) {
        return CodecUtil.unilateral(RegistryKey.createCodec(registry).fieldOf("$id"));
    }

    public static <T> Codec<LazyRegistryEntryReference<T>> createLazyRegistryEntry(RegistryKey<Registry<T>> registryRef, Codec<RegistryEntry<T>> entryCodec) {
        return Codec.either(entryCodec, RegistryKey.createCodec(registryRef)).xmap(LazyRegistryEntryReference::new, LazyRegistryEntryReference::contents);
    }
}