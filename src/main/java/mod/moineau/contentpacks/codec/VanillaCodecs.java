package mod.moineau.contentpacks.codec;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.api.util.CodecUtil;
import mod.moineau.api.util.IdentifierUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;
import java.util.stream.Stream;

public final class VanillaCodecs {
    public static final MapDecoder<Identifier> INJECT_ID = Identifier.CODEC.fieldOf(CodecUtil.INJECT_ID_KEY);
    public static final MapDecoder<String> INJECT_SLASH_ID = Identifier.CODEC.fieldOf(CodecUtil.INJECT_ID_KEY).map(IdentifierUtil::toSlashId);
    public static final RecordCodecBuilder<?, Identifier> INJECT_ID_UNILATERAL = CodecUtil.unilateral(INJECT_ID);
    public static final RecordCodecBuilder<?, String> INJECT_SLASH_ID_UNILATERAL = CodecUtil.unilateral(INJECT_SLASH_ID);
    public static final Codec<PushReaction> PISTON_BEHAVIOR = CodecUtil.enumByName(PushReaction.class);
    public static final Codec<BlockBehaviour.OffsetType> BLOCK_OFFSET_TYPE = CodecUtil.enumByName(BlockBehaviour.OffsetType.class);
    public static final Codec<NoteBlockInstrument> NOTE_BLOCK_INSTRUMENT = CodecUtil.enumByName(NoteBlockInstrument.class);
    public static final Codec<BlockSetType.PressurePlateSensitivity> ACTIVATION_RULE = CodecUtil.enumByName(BlockSetType.PressurePlateSensitivity.class);
    public static final Codec<Optional<ResourceKey<LootTable>>> LOOT_TABLE = CodecUtil.intentionallyOptional(LootTable.KEY_CODEC);

    public static <T> MapDecoder<ResourceKey<T>> injectRegistryKey(ResourceKey<? extends Registry<T>> registry) {
        return ResourceKey.codec(registry).fieldOf(CodecUtil.INJECT_ID_KEY);
    }

    @SuppressWarnings("unchecked")
    public static <O> RecordCodecBuilder<O, Identifier> fillInjectId() {
        return (RecordCodecBuilder<O, Identifier>) INJECT_ID_UNILATERAL;
    }

    @SuppressWarnings("unchecked")
    public static <O> RecordCodecBuilder<O, String> fillInjectSlashId() {
        return (RecordCodecBuilder<O, String>) INJECT_SLASH_ID_UNILATERAL;
    }

    public static <O, T> RecordCodecBuilder<O, ResourceKey<T>> fillInjectRegistryKey(ResourceKey<? extends Registry<T>> registry) {
        return CodecUtil.unilateral(injectRegistryKey(registry));
    }

    public static <T> MapCodec<String> description(ResourceKey<? extends Registry<T>> registry, String prefix) {
        return dependantName(
                Codec.STRING.fieldOf("description"),
                registry,
                id -> Util.makeDescriptionId(prefix, id.identifier())
        );
    }

    public static MapCodec<Optional<ResourceKey<LootTable>>> lootTable(String prefix) {
        return dependantName(
                LOOT_TABLE.fieldOf("loot_table"),
                Registries.LOOT_TABLE,
                id -> Optional.of(
                        ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix(prefix + "/"))
                )
        );
    }

    public static <T, V> MapCodec<V> dependantName(MapCodec<V> codec, ResourceKey<? extends Registry<T>> registry, DependantName<T, V> defaultValue) {
        return new MapCodec<>() {
            private final MapDecoder<ResourceKey<T>> keyCodec = injectRegistryKey(registry);

            @Override
            public <O> DataResult<V> decode(DynamicOps<O> ops, MapLike<O> input) {
                DataResult<DependantName<T, V>> result = codec.decode(ops, input).<DependantName<T, V>>map(DependantName::fixed).setPartial(defaultValue);
                return result.apply2(DependantName::get, keyCodec.decode(ops, input)).promotePartial(_ -> {});
            }

            @Override
            public <O> RecordBuilder<O> encode(V input, DynamicOps<O> ops, RecordBuilder<O> prefix) {
                return codec.encode(input, ops, prefix);
            }

            @Override
            public <O> Stream<O> keys(DynamicOps<O> ops) {
                return Stream.empty();
            }
        };
    }
}