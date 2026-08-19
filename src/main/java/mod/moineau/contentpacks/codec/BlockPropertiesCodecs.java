package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.datafixers.ContentProducts;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.block.ContentBlockProperties;
import mod.moineau.contentpacks.block.LightEmissionProvider;
import mod.moineau.contentpacks.block.MapColorProvider;
import mod.moineau.contentpacks.block.statepredicates.StatePredicate;
import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicate;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

// TODO : Adjust field names with variable names.
public final class BlockPropertiesCodecs {
        public static final Codec<BlockBehaviour.Properties> BASE_CODEC = RecordCodecBuilder.create(instance -> ContentProducts.P32.group(instance,
                MapColorProvider.DOWNGRADED_CODEC.optionalFieldOf("map_color", ContentBlockProperties.DEFAULT_MAP_COLOR)
                        .forGetter(settings -> settings.mapColor),
                Codec.BOOL.optionalFieldOf("collision", true)
                        .forGetter(settings -> settings.hasCollision),
                ContentRegistries.SOUND_TYPE.byNameCodec().optionalFieldOf("sound_type", SoundType.STONE)
                        .forGetter(settings -> settings.soundType),
                LightEmissionProvider.DOWNGRADED_CODEC.optionalFieldOf("light_emission", ContentBlockProperties.DEFAULT_LIGHT_EMISSION)
                        .forGetter(settings -> settings.lightEmission),
                Codec.FLOAT.optionalFieldOf("explosion_resistance", 0.0F)
                        .forGetter(settings -> settings.explosionResistance),
                Codec.FLOAT.optionalFieldOf("destroy_time", 0.0F)
                        .forGetter(settings -> settings.destroyTime),
                Codec.BOOL.optionalFieldOf("requires_tool", false)
                        .forGetter(settings -> settings.requiresCorrectToolForDrops),
                Codec.BOOL.optionalFieldOf("random_ticks", false)
                        .forGetter(settings -> settings.isRandomlyTicking),
                Codec.FLOAT.optionalFieldOf("friction", 0.6F)
                        .forGetter(settings -> settings.friction),
                Codec.FLOAT.optionalFieldOf("speed_factor", 1.0F)
                        .forGetter(settings -> settings.speedFactor),
                Codec.FLOAT.optionalFieldOf("jump_factor", 1.0F)
                        .forGetter(settings -> settings.jumpFactor),
                Codec.FLOAT.optionalFieldOf("bounce_restitution", 0.0F)
                        .forGetter(settings -> settings.bounceRestitution),
                CodecUtil.intentionallyOptional(ResourceKey.codec(Registries.LOOT_TABLE)).optionalFieldOf("loot_table")
                        .forGetter(settings -> Optional.ofNullable(((ContentBlockProperties) settings).contentpacks$getLootTableOverride())),
                Codec.STRING.optionalFieldOf("description")
                        .forGetter(settings -> Optional.ofNullable(((ContentBlockProperties) settings).contentpacks$getDescriptionIdOverride())),
                Codec.BOOL.optionalFieldOf("occlusion", true)
                        .forGetter(settings -> settings.canOcclude),
                Codec.BOOL.optionalFieldOf("air", false)
                        .forGetter(settings -> settings.isAir),
                Codec.BOOL.optionalFieldOf("ignited_by_lava", false)
                        .forGetter(settings -> settings.ignitedByLava),
                Codec.BOOL.optionalFieldOf("liquid", false)
                        .forGetter(settings -> settings.liquid),
                Codec.BOOL.optionalFieldOf("force_solid", false)
                        .forGetter(settings -> settings.forceSolidOn),
                VanillaCodecs.PISTON_BEHAVIOR.optionalFieldOf("push_reaction", PushReaction.NORMAL)
                        .forGetter(settings -> settings.pushReaction),
                Codec.BOOL.optionalFieldOf("terrain_particles", true)
                        .forGetter(settings -> settings.spawnTerrainParticles),
                VanillaCodecs.NOTE_BLOCK_INSTRUMENT.optionalFieldOf("instrument", NoteBlockInstrument.HARP)
                        .forGetter(settings -> settings.instrument),
                Codec.BOOL.optionalFieldOf("replaceable", false)
                        .forGetter(settings -> settings.replaceable),
                EntityTypedStatePredicate.DOWNGRADED_CODEC.optionalFieldOf("valid_spawn", ContentBlockProperties.DEFAULT_IS_VALID_SPAWN)
                        .forGetter(settings -> settings.isValidSpawn),
                StatePredicate.DOWNGRADED_CODEC.optionalFieldOf("redstone_conductor", ContentBlockProperties.DEFAULT_IS_REDSTONE_CONDUCTOR)
                        .forGetter(settings -> settings.isRedstoneConductor),
                StatePredicate.DOWNGRADED_CODEC.optionalFieldOf("suffocating", ContentBlockProperties.DEFAULT_IS_SUFFOCATING)
                        .forGetter(settings -> settings.isSuffocating),
                StatePredicate.DOWNGRADED_CODEC.optionalFieldOf("view_blocking", ContentBlockProperties.DEFAULT_IS_VIEW_BLOCKING)
                        .forGetter(settings -> settings.isViewBlocking),

//                StatePredicate.DOWNGRADED_CODEC.optionalFieldOf("post_process", ContentBlockProperties.DEFAULT_POST_PROCESS)
//                        .forGetter(settings -> settings.postProcess), // TODO
//                StatePredicate.DOWNGRADED_CODEC.optionalFieldOf("emissive_rendering", ContentBlockProperties.DEFAULT_EMISSIVE_RENDERING)
//                        .forGetter(settings -> settings.emissiveRendering), // TODO : Bring back simpler state predicate, make it delegable by block context predicate (state predicate)

                MapCodec.unit(ContentBlockProperties.DEFAULT_POST_PROCESS)
                        .forGetter(settings -> settings.postProcess),
                MapCodec.unit(ContentBlockProperties.DEFAULT_EMISSIVE_RENDERING)
                        .forGetter(settings -> settings.emissiveRendering),

                Codec.BOOL.optionalFieldOf("dynamic_shape", false)
                        .forGetter(settings -> settings.dynamicShape),
                FeatureFlags.CODEC.optionalFieldOf("required_features", FeatureFlags.VANILLA_SET)
                        .forGetter(settings -> settings.requiredFeatures),
                VanillaCodecs.BLOCK_OFFSET_TYPE.optionalFieldOf("offset", BlockBehaviour.OffsetType.NONE)
                        .forGetter(settings -> ((ContentBlockProperties) settings).contentpacks$getOffsetType())
        ).apply(instance, BlockPropertiesCodecs::create));
        public static final MapCodec<BlockBehaviour.Properties> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BASE_CODEC.optionalFieldOf("properties")
                        .xmap(optional -> optional.orElseGet(BlockBehaviour.Properties::of), Optional::of)
                        .forGetter(Function.identity()),
                VanillaCodecs.fillInjectRegistryKey(Registries.BLOCK)
        ).apply(instance, BlockBehaviour.Properties::setId));

        private static BlockBehaviour.Properties create(
                Function<BlockState, MapColor> mapColor,
                boolean hasCollision,
                SoundType soundType,
                ToIntFunction<BlockState> lightEmission,
                float explosionResistance,
                float destroyTime,
                boolean requiresCorrectToolForDrops,
                boolean isRandomlyTicking,
                float friction,
                float speedFactor,
                float jumpFactor,
                float bounceRestitution,
                Optional<Optional<ResourceKey<LootTable>>> drops,
                Optional<String> descriptionId,
                boolean canOcclude,
                boolean isAir,
                boolean ignitedByLava,
                boolean liquid,
                boolean forceSolidOn,
                PushReaction pushReaction,
                boolean spawnTerrainParticles,
                NoteBlockInstrument instrument,
                boolean replaceable,
                BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn,
                BlockBehaviour.StatePredicate isRedstoneConductor,
                BlockBehaviour.StatePredicate isSuffocating,
                BlockBehaviour.StatePredicate isViewBlocking,
                BlockBehaviour.PostProcess postProcess,
                Predicate<BlockState> emissiveRendering,
                boolean dynamicShape,
                FeatureFlagSet requiredFeatures,
                BlockBehaviour.OffsetType offsetFunction
        ) {
                BlockBehaviour.Properties settings = BlockBehaviour.Properties.of();
                settings.mapColor = mapColor;
                settings.hasCollision = hasCollision;
                settings.soundType = soundType;
                settings.lightEmission = lightEmission;
                settings.explosionResistance = explosionResistance;
                settings.destroyTime = destroyTime;
                settings.requiresCorrectToolForDrops = requiresCorrectToolForDrops;
                settings.isRandomlyTicking = isRandomlyTicking;
                settings.friction = friction;
                settings.speedFactor = speedFactor;
                settings.jumpFactor = jumpFactor;
                settings.bounceRestitution = bounceRestitution;
                drops.ifPresent(settings::overrideLootTable);
                descriptionId.ifPresent(settings::overrideDescription);
                settings.canOcclude = canOcclude;
                settings.isAir = isAir;
                settings.ignitedByLava = ignitedByLava;
                settings.liquid = liquid;
                settings.forceSolidOn = forceSolidOn;
                settings.pushReaction = pushReaction;
                settings.spawnTerrainParticles = spawnTerrainParticles;
                settings.instrument = instrument;
                settings.replaceable = replaceable;
                settings.isValidSpawn = isValidSpawn;
                settings.isRedstoneConductor = isRedstoneConductor;
                settings.isSuffocating = isSuffocating;
                settings.isViewBlocking = isViewBlocking;
                settings.postProcess = postProcess;
                settings.emissiveRendering = emissiveRendering;
                settings.dynamicShape = dynamicShape;
                settings.requiredFeatures = requiredFeatures;
                settings.offsetType(offsetFunction);
                return settings;
        }
}