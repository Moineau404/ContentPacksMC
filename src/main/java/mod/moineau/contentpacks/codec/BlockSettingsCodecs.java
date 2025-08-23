package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.datafixers.ContentProducts;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.block.ContentBlockSettings;
import mod.moineau.contentpacks.block.MapColors;
import mod.moineau.contentpacks.block.contextpredicate.BlockContextPredicate;
import mod.moineau.contentpacks.block.contextpredicate.entitytyped.EntityTypedBlockContextPredicate;
import mod.moineau.contentpacks.block.statefunction.BlockStateFunction;
import mod.moineau.contentpacks.block.statefunction.BlockStateToIntFunction;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.sound.BlockSoundGroup;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class BlockSettingsCodecs {
        public static final Codec<AbstractBlock.Settings> BASE_CODEC = RecordCodecBuilder.create(instance -> ContentProducts.P31.group(instance,
                BlockStateFunction.createDowngradedCodec(MapColors.CODEC, MapColor.CLEAR).optionalFieldOf("map_color_provider", ContentBlockSettings.DEFAULT_MAP_COLOR_PROVIDER)
                        .forGetter(settings -> settings.mapColorProvider),
                Codec.BOOL.optionalFieldOf("collision", true)
                        .forGetter(settings -> settings.collidable),
                ContentRegistries.BLOCK_SOUND_GROUP.getCodec().optionalFieldOf("sound", BlockSoundGroup.STONE)
                        .forGetter(settings -> settings.soundGroup),
                BlockStateToIntFunction.createDowngradedCodec(0, 15).optionalFieldOf("light_level", ContentBlockSettings.DEFAULT_LUMINANCE_PROVIDER)
                        .forGetter(settings -> settings.luminance),
                Codec.FLOAT.optionalFieldOf("explosion_resistance", 0.0F)
                        .forGetter(settings -> settings.resistance),
                Codec.FLOAT.optionalFieldOf("destroy_time", 0.0F)
                        .forGetter(settings -> settings.hardness),
                Codec.BOOL.optionalFieldOf("requires_tool", false)
                        .forGetter(settings -> settings.toolRequired),
                Codec.BOOL.optionalFieldOf("random_ticks", false)
                        .forGetter(settings -> settings.randomTicks),
                Codec.FLOAT.optionalFieldOf("friction", 0.6F)
                        .forGetter(settings -> settings.slipperiness),
                Codec.FLOAT.optionalFieldOf("speed_factor", 1.0F)
                        .forGetter(settings -> settings.velocityMultiplier),
                Codec.FLOAT.optionalFieldOf("jump_factor", 1.0F)
                        .forGetter(settings -> settings.jumpVelocityMultiplier),
                CodecUtil.intentionallyOptional(RegistryKey.createCodec(RegistryKeys.LOOT_TABLE)).optionalFieldOf("loot_table")
                        .forGetter(settings -> Optional.ofNullable(((ContentBlockSettings) settings).contentpacks$getLootTableOverride())),
                Codec.STRING.optionalFieldOf("description")
                        .forGetter(settings -> Optional.ofNullable(((ContentBlockSettings) settings).contentpacks$getTranslationKeyOverride())),
                Codec.BOOL.optionalFieldOf("occlusion", true)
                        .forGetter(settings -> settings.opaque),
                Codec.BOOL.optionalFieldOf("air", false)
                        .forGetter(settings -> settings.isAir),
                Codec.BOOL.optionalFieldOf("ignited_by_lava", false)
                        .forGetter(settings -> settings.burnable),
                Codec.BOOL.optionalFieldOf("liquid", false)
                        .forGetter(settings -> settings.liquid),
                Codec.BOOL.optionalFieldOf("force_solid", false)
                        .forGetter(settings -> settings.forceSolid),
                VanillaCodecs.PISTON_BEHAVIOR.optionalFieldOf("push_reaction", PistonBehavior.NORMAL)
                        .forGetter(settings -> settings.pistonBehavior),
                Codec.BOOL.optionalFieldOf("terrain_particles", true)
                        .forGetter(settings -> settings.blockBreakParticles),
                VanillaCodecs.NOTE_BLOCK_INSTRUMENT.optionalFieldOf("instrument", NoteBlockInstrument.HARP)
                        .forGetter(settings -> settings.instrument),
                Codec.BOOL.optionalFieldOf("replaceable", false)
                        .forGetter(settings -> settings.replaceable),
                EntityTypedBlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("valid_spawn", ContentBlockSettings.DEFAULT_ALLOWS_SWPANING_PREDICATE)
                        .forGetter(settings -> settings.allowsSpawningPredicate),
                BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("redstone_conductor", ContentBlockSettings.DEFAULT_SOLID_BLOCK_PREDICATE)
                        .forGetter(settings -> settings.solidBlockPredicate),
                BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("suffocating", ContentBlockSettings.DEFAULT_SUFFOCATION_PREDICATE)
                        .forGetter(settings -> settings.suffocationPredicate),
                BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("view_blocking", ContentBlockSettings.DEFAULT_BLOCK_VISION_PREDICATE)
                        .forGetter(settings -> settings.blockVisionPredicate),
                BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("post_process", ContentBlockSettings.DEFAULT_POST_PROCESS_PREDICATE)
                        .forGetter(settings -> settings.postProcessPredicate),
                BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("emissive_rendering", ContentBlockSettings.DEFAULT_EMISIVE_RENDERING_PREDICATE)
                        .forGetter(settings -> settings.emissiveLightingPredicate),
                Codec.BOOL.optionalFieldOf("dynamic_shape", false)
                        .forGetter(settings -> settings.dynamicBounds),
                FeatureFlags.CODEC.optionalFieldOf("required_features", FeatureFlags.VANILLA_FEATURES)
                        .forGetter(settings -> settings.requiredFeatures),
                VanillaCodecs.BLOCK_OFFSET_TYPE.optionalFieldOf("offset", AbstractBlock.OffsetType.NONE)
                        .forGetter(settings -> ((ContentBlockSettings) settings).contentpacks$getOffsetType())
        ).apply(instance, BlockSettingsCodecs::create));
        public static final MapCodec<AbstractBlock.Settings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BASE_CODEC.optionalFieldOf("properties")
                        .xmap(optional -> optional.orElseGet(AbstractBlock.Settings::create), Optional::of)
                        .forGetter(Function.identity()),
                VanillaCodecs.fillInjectRegistryKey(RegistryKeys.BLOCK)
        ).apply(instance, AbstractBlock.Settings::registryKey));

        private static AbstractBlock.Settings create(
                Function<BlockState, MapColor> mapColorProvider,
                boolean collidable,
                BlockSoundGroup soundGroup,
                ToIntFunction<BlockState> luminance,
                float resistance,
                float hardness,
                boolean toolRequired,
                boolean randomTicks,
                float slipperiness,
                float velocityMultiplier,
                float jumpVelocityMultiplier,
                Optional<Optional<RegistryKey<LootTable>>> lootTable,
                Optional<String> translationKey,
                boolean opaque,
                boolean isAir,
                boolean burnable,
                boolean liquid,
                boolean forceSolid,
                PistonBehavior pistonBehavior,
                boolean blockBreakParticles,
                NoteBlockInstrument instrument,
                boolean replaceable,
                AbstractBlock.TypedContextPredicate<EntityType<?>> allowsSpawningPredicate,
                AbstractBlock.ContextPredicate solidBlockPredicate,
                AbstractBlock.ContextPredicate suffocationPredicate,
                AbstractBlock.ContextPredicate blockVisionPredicate,
                AbstractBlock.ContextPredicate postProcessPredicate,
                AbstractBlock.ContextPredicate emissiveLightingPredicate,
                boolean dynamicBounds,
                FeatureSet requiredFeatures,
                AbstractBlock.OffsetType offsetType
        ) {
                AbstractBlock.Settings settings = AbstractBlock.Settings.create();
                settings.mapColorProvider = mapColorProvider;
                settings.collidable = collidable;
                settings.soundGroup = soundGroup;
                settings.luminance = luminance;
                settings.resistance = resistance;
                settings.hardness = hardness;
                settings.toolRequired = toolRequired;
                settings.randomTicks = randomTicks;
                settings.slipperiness = slipperiness;
                settings.velocityMultiplier = velocityMultiplier;
                settings.jumpVelocityMultiplier = jumpVelocityMultiplier;
                lootTable.ifPresent(settings::lootTable);
                translationKey.ifPresent(settings::overrideTranslationKey);
                settings.opaque = opaque;
                settings.isAir = isAir;
                settings.burnable = burnable;
                settings.liquid = liquid;
                settings.forceSolid = forceSolid;
                settings.pistonBehavior = pistonBehavior;
                settings.blockBreakParticles = blockBreakParticles;
                settings.instrument = instrument;
                settings.replaceable = replaceable;
                settings.allowsSpawningPredicate = allowsSpawningPredicate;
                settings.solidBlockPredicate = solidBlockPredicate;
                settings.suffocationPredicate = suffocationPredicate;
                settings.blockVisionPredicate = blockVisionPredicate;
                settings.postProcessPredicate = postProcessPredicate;
                settings.emissiveLightingPredicate = emissiveLightingPredicate;
                settings.dynamicBounds = dynamicBounds;
                settings.requiredFeatures = requiredFeatures;
                settings.offset(offsetType);
                return settings;
        }
}
/*
        public static final Codec<AbstractBlock.Settings> BASE_CODEC = RecordCodecBuilder.create(instance -> ContentProducts.P31.group(instance,
                        BlockStateFunction.createDowngradedCodec(MapColors.CODEC, MapColor.CLEAR).optionalFieldOf("map_color_provider", ContentBlockSettings.DEFAULT_MAP_COLOR_PROVIDER).forGetter(settings -> settings.mapColorProvider),
                        Codec.BOOL.optionalFieldOf("collision", true).forGetter(settings -> settings.collidable),
                        ContentRegistries.BLOCK_SOUND_GROUP.getCodec().optionalFieldOf("sound", BlockSoundGroup.STONE).forGetter(settings -> settings.soundGroup),
                        BlockStateToIntFunction.createDowngradedCodec(0, 15).optionalFieldOf("light_level", ContentBlockSettings.DEFAULT_LUMINANCE_PROVIDER).forGetter(settings -> settings.luminance),
                        Codec.FLOAT.optionalFieldOf("explosion_resistance", 0.0F).forGetter(settings -> settings.resistance),
                        Codec.FLOAT.optionalFieldOf("destroy_time", 0.0F).forGetter(settings -> settings.hardness),
                        Codec.BOOL.optionalFieldOf("requires_tool", false).forGetter(settings -> settings.toolRequired),
                        Codec.BOOL.optionalFieldOf("random_ticks", false).forGetter(settings -> settings.randomTicks),
                        Codec.FLOAT.optionalFieldOf("friction", 0.6F).forGetter(settings -> settings.slipperiness),
                        Codec.FLOAT.optionalFieldOf("speed_factor", 1.0F).forGetter(settings -> settings.velocityMultiplier),
                        Codec.FLOAT.optionalFieldOf("jump_factor", 1.0F).forGetter(settings -> settings.jumpVelocityMultiplier),
                        CodecUtil.intentionallyOptional(RegistryKey.createCodec(RegistryKeys.LOOT_TABLE)).optionalFieldOf("loot_table").forGetter(settings -> Optional.ofNullable(((ContentBlockSettings) settings)
                                        .contentpacks$getLootTableOverride())),
                        Codec.STRING.optionalFieldOf("description").forGetter(settings -> Optional.ofNullable(((ContentBlockSettings) settings)
                                        .contentpacks$getTranslationKeyOverride())),
                        Codec.BOOL.optionalFieldOf("occlusion", true).forGetter(settings -> settings.opaque),
                        Codec.BOOL.optionalFieldOf("air", false).forGetter(settings -> settings.isAir),
                        Codec.BOOL.optionalFieldOf("ignited_by_lava", false).forGetter(settings -> settings.burnable),
                        Codec.BOOL.optionalFieldOf("liquid", false).forGetter(settings -> settings.liquid),
                        Codec.BOOL.optionalFieldOf("force_solid", false).forGetter(settings -> settings.forceSolid),
                        VanillaCodecs.PISTON_BEHAVIOR.optionalFieldOf("push_reaction", PistonBehavior.NORMAL).forGetter(settings -> settings.pistonBehavior),
                        Codec.BOOL.optionalFieldOf("terrain_particles", true).forGetter(settings -> settings.blockBreakParticles),
                        VanillaCodecs.NOTE_BLOCK_INSTRUMENT.optionalFieldOf("instrument", NoteBlockInstrument.HARP).forGetter(settings -> settings.instrument),
                        Codec.BOOL.optionalFieldOf("replaceable", false).forGetter(settings -> settings.replaceable),
                        EntityTypedBlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("valid_spawn", ContentBlockSettings.DEFAULT_ALLOWS_SWPANING_PREDICATE).forGetter(settings -> settings.allowsSpawningPredicate),
                        BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("redstone_conductor", ContentBlockSettings.DEFAULT_SOLID_BLOCK_PREDICATE).forGetter(settings -> settings.solidBlockPredicate),
                        BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("suffocating", ContentBlockSettings.DEFAULT_SUFFOCATION_PREDICATE).forGetter(settings -> settings.suffocationPredicate),
                        BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("view_blocking", ContentBlockSettings.DEFAULT_BLOCK_VISION_PREDICATE).forGetter(settings -> settings.blockVisionPredicate),
                        BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("post_process", ContentBlockSettings.DEFAULT_POST_PROCESS_PREDICATE).forGetter(settings -> settings.postProcessPredicate),
                        BlockContextPredicate.DOWNGRADED_CODEC.optionalFieldOf("emissive_rendering", ContentBlockSettings.DEFAULT_EMISIVE_RENDERING_PREDICATE).forGetter(settings -> settings.emissiveLightingPredicate),
                        Codec.BOOL.optionalFieldOf("dynamic_shape", false).forGetter(settings -> settings.dynamicBounds),
                        FeatureFlags.CODEC.optionalFieldOf("required_features", FeatureFlags.VANILLA_FEATURES).forGetter(settings -> settings.requiredFeatures),
                        VanillaCodecs.BLOCK_OFFSET_TYPE.optionalFieldOf("offset", AbstractBlock.OffsetType.NONE).forGetter(settings -> ((ContentBlockSettings) settings).contentpacks$getOffsetType())

                ).apply(instance, BlockSettingsCodecs::create)
        );
 */