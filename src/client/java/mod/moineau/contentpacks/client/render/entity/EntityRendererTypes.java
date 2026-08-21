package mod.moineau.contentpacks.client.render.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.FunctionUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.monster.illager.SpellcasterIllager;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityRendererTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends EntityRendererProvider<?>>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final MapCodec<EntityRendererProvider<?>> CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatchMap(FunctionUtil::nothing, Function.identity());
    public static final Codec<ModelLayerLocation> MODEL_LAYER_LOCATION_CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(ModelLayerLocation::model),
                    Codec.STRING.fieldOf("layer").forGetter(ModelLayerLocation::layer)
            ).apply(instance, ModelLayerLocation::new)),
            Identifier.CODEC.xmap(id -> new ModelLayerLocation(id, "main"), ModelLayerLocation::model)
    );

    public static void register(Identifier id, MapCodec<? extends EntityRendererProvider<?>> entry) {
        ID_MAPPER.put(id, entry);
    }

    public static void bootStrap() {
//        register(Identifier.withDefaultNamespace("banner"), of(BannerRenderer::new));
//        register(Identifier.withDefaultNamespace("beacon"), of(BeaconRenderer::new));
//        register(Identifier.withDefaultNamespace("bell"), of(BellRenderer::new));
//        register(Identifier.withDefaultNamespace("block_entity_with_bounding_box"), of(BlockEntityWithBoundingBoxRenderer::new));
//        register(Identifier.withDefaultNamespace("brushable_block"), of(BrushableBlockRenderer::new));
//        register(Identifier.withDefaultNamespace("campfire"), of(CampfireRenderer::new));
//        register(Identifier.withDefaultNamespace("chest"), of(ChestRenderer::new));
//        register(Identifier.withDefaultNamespace("conduit"), of(ConduitRenderer::new));
//        register(Identifier.withDefaultNamespace("copper_golem_statue_block"), of(CopperGolemStatueBlockRenderer::new));
//        register(Identifier.withDefaultNamespace("decorated_pot"), of(DecoratedPotRenderer::new));
//        register(Identifier.withDefaultNamespace("enchant_table"), of(EnchantTableRenderer::new));
//        register(Identifier.withDefaultNamespace("hanging_sign"), of(HangingSignRenderer::new));
//        register(Identifier.withDefaultNamespace("lectern"), of(LecternRenderer::new));
//        register(Identifier.withDefaultNamespace("piston_head"), of(PistonHeadRenderer::new));
//        register(Identifier.withDefaultNamespace("shelf"), of(ShelfRenderer::new));
//        register(Identifier.withDefaultNamespace("shulker_box"), of(ShulkerBoxRenderer::new));
//        register(Identifier.withDefaultNamespace("skull_block"), of(SkullBlockRenderer::new));
//        register(Identifier.withDefaultNamespace("spawner"), of(SpawnerRenderer::new));
//        register(Identifier.withDefaultNamespace("standing_sign"), of(StandingSignRenderer::new));
//        register(Identifier.withDefaultNamespace("test_instance"), of(TestInstanceRenderer::new));
//        register(Identifier.withDefaultNamespace("the_end_gateway"), of(TheEndGatewayRenderer::new));
//        register(Identifier.withDefaultNamespace("the_end_portal"), of(TheEndPortalRenderer::new));
//        register(Identifier.withDefaultNamespace("trial_spawner"), of(TrialSpawnerRenderer::new));
//        register(Identifier.withDefaultNamespace("vault"), of(VaultRenderer::new));
        register(Identifier.withDefaultNamespace("allay"), of(AllayRenderer::new));
        register(Identifier.withDefaultNamespace("armadillo"), of(ArmadilloRenderer::new));
        register(Identifier.withDefaultNamespace("armor_stand"), of(ArmorStandRenderer::new));
        register(Identifier.withDefaultNamespace("axolotl"), of(AxolotlRenderer::new));
        register(Identifier.withDefaultNamespace("bat"), of(BatRenderer::new));
        register(Identifier.withDefaultNamespace("bee"), of(BeeRenderer::new));
        register(Identifier.withDefaultNamespace("blaze"), of(BlazeRenderer::new));
        register(Identifier.withDefaultNamespace("boat"), withModel(BoatRenderer::new));
        register(Identifier.withDefaultNamespace("bogged"), of(BoggedRenderer::new));
        register(Identifier.withDefaultNamespace("breeze"), of(BreezeRenderer::new));
        register(Identifier.withDefaultNamespace("camel_husk"), of(CamelHuskRenderer::new));
        register(Identifier.withDefaultNamespace("camel"), of(CamelRenderer::new));
        register(Identifier.withDefaultNamespace("cat"), of(CatRenderer::new));
        register(Identifier.withDefaultNamespace("cave_spider"), of(CaveSpiderRenderer::new));
        register(Identifier.withDefaultNamespace("chicken"), of(ChickenRenderer::new));
        register(Identifier.withDefaultNamespace("cod"), of(CodRenderer::new));
        register(Identifier.withDefaultNamespace("copper_golem"), of(CopperGolemRenderer::new));
        register(Identifier.withDefaultNamespace("cow"), of(CowRenderer::new));
        register(Identifier.withDefaultNamespace("creaking"), of((EntityRendererProvider<Creaking>) CreakingRenderer::new));
        register(Identifier.withDefaultNamespace("creeper"), of(CreeperRenderer::new));
        register(Identifier.withDefaultNamespace("dolphin"), of(DolphinRenderer::new));
//        register(Identifier.withDefaultNamespace("donkey"), of(DonkeyRenderer::new));
        register(Identifier.withDefaultNamespace("dragon_fireball"), of(DragonFireballRenderer::new));
        register(Identifier.withDefaultNamespace("drowned"), of(DrownedRenderer::new));
        register(Identifier.withDefaultNamespace("elder_guardian"), of(ElderGuardianRenderer::new));
        register(Identifier.withDefaultNamespace("end_crystal"), of(EndCrystalRenderer::new));
        register(Identifier.withDefaultNamespace("ender_dragon"), of(EnderDragonRenderer::new));
        register(Identifier.withDefaultNamespace("enderman"), of(EndermanRenderer::new));
        register(Identifier.withDefaultNamespace("endermite"), of(EndermiteRenderer::new));
        register(Identifier.withDefaultNamespace("evoker_fangs"), of(EvokerFangsRenderer::new));
        register(Identifier.withDefaultNamespace("evoker"), of((EntityRendererProvider<SpellcasterIllager>) EvokerRenderer::new));
        register(Identifier.withDefaultNamespace("experience_orb"), of(ExperienceOrbRenderer::new));
        register(Identifier.withDefaultNamespace("falling_block"), of(FallingBlockRenderer::new));
        register(Identifier.withDefaultNamespace("firework_entity"), of(FireworkEntityRenderer::new));
        register(Identifier.withDefaultNamespace("fishing_hook"), of(FishingHookRenderer::new));
        register(Identifier.withDefaultNamespace("fox"), of(FoxRenderer::new));
        register(Identifier.withDefaultNamespace("frog"), of(FrogRenderer::new));
        register(Identifier.withDefaultNamespace("ghast"), of(GhastRenderer::new));
        register(Identifier.withDefaultNamespace("giant_mob"), of(GiantMobRenderer::new, Codec.FLOAT.fieldOf("scale")));
//        register(Identifier.withDefaultNamespace("glow_squid"), of(GlowSquidRenderer::new));
        register(Identifier.withDefaultNamespace("goat"), of(GoatRenderer::new));
        register(Identifier.withDefaultNamespace("guardian"), of(GuardianRenderer::new));
        register(Identifier.withDefaultNamespace("happy_ghast"), of(HappyGhastRenderer::new));
        register(Identifier.withDefaultNamespace("hoglin"), of(HoglinRenderer::new));
        register(Identifier.withDefaultNamespace("horse"), of(HorseRenderer::new));
        register(Identifier.withDefaultNamespace("husk"), of(HuskRenderer::new));
        register(Identifier.withDefaultNamespace("illusioner"), of(IllusionerRenderer::new));
        register(Identifier.withDefaultNamespace("iron_golem"), of(IronGolemRenderer::new));
        register(Identifier.withDefaultNamespace("item_entity"), of(ItemEntityRenderer::new));
        register(Identifier.withDefaultNamespace("item_frame"), of(ItemFrameRenderer::new));
        register(Identifier.withDefaultNamespace("leash_knot"), of(LeashKnotRenderer::new));
        register(Identifier.withDefaultNamespace("lightning_bolt"), of(LightningBoltRenderer::new));
//        register(Identifier.withDefaultNamespace("llama"), of(LlamaRenderer::new));
        register(Identifier.withDefaultNamespace("llama_spit"), of(LlamaSpitRenderer::new));
        register(Identifier.withDefaultNamespace("magma_cube"), of(MagmaCubeRenderer::new));
        register(Identifier.withDefaultNamespace("minecart"), withModel(MinecartRenderer::new));
        register(Identifier.withDefaultNamespace("mushroom_cow"), of(MushroomCowRenderer::new));
        register(Identifier.withDefaultNamespace("nautilus"), of(NautilusRenderer::new));
        register(Identifier.withDefaultNamespace("noop"), of(NoopRenderer::new));
        register(Identifier.withDefaultNamespace("ocelot"), of(OcelotRenderer::new));
//        register(Identifier.withDefaultNamespace("ominous_item_spawner"), of(OminousItemSpawnerRenderer::new));
        register(Identifier.withDefaultNamespace("painting"), of(PaintingRenderer::new));
        register(Identifier.withDefaultNamespace("panda"), of(PandaRenderer::new));
        register(Identifier.withDefaultNamespace("parched"), of(ParchedRenderer::new));
        register(Identifier.withDefaultNamespace("parrot"), of(ParrotRenderer::new));
        register(Identifier.withDefaultNamespace("phantom"), of(PhantomRenderer::new));
//        register(Identifier.withDefaultNamespace("piglin"), of(PiglinRenderer::new));
        register(Identifier.withDefaultNamespace("pig"), of(PigRenderer::new));
        register(Identifier.withDefaultNamespace("pillager"), of(PillagerRenderer::new));
        register(Identifier.withDefaultNamespace("polar_bear"), of(PolarBearRenderer::new));
        register(Identifier.withDefaultNamespace("pufferfish"), of(PufferfishRenderer::new));
        register(Identifier.withDefaultNamespace("rabbit"), of(RabbitRenderer::new));
        register(Identifier.withDefaultNamespace("raft"), withModel(RaftRenderer::new));
        register(Identifier.withDefaultNamespace("ravager"), of(RavagerRenderer::new));
        register(Identifier.withDefaultNamespace("salmon"), of(SalmonRenderer::new));
        register(Identifier.withDefaultNamespace("sheep"), of(SheepRenderer::new));
        register(Identifier.withDefaultNamespace("shulker_bullet"), of(ShulkerBulletRenderer::new));
        register(Identifier.withDefaultNamespace("shulker"), of(ShulkerRenderer::new));
        register(Identifier.withDefaultNamespace("silverfish"), of(SilverfishRenderer::new));
        register(Identifier.withDefaultNamespace("skeleton"), of(SkeletonRenderer::new));
        register(Identifier.withDefaultNamespace("slime"), of(SlimeRenderer::new));
        register(Identifier.withDefaultNamespace("sniffer"), of(SnifferRenderer::new));
        register(Identifier.withDefaultNamespace("snow_golem"), of(SnowGolemRenderer::new));
        register(Identifier.withDefaultNamespace("spectral_arrow"), of(SpectralArrowRenderer::new));
        register(Identifier.withDefaultNamespace("spider"), of(SpiderRenderer::new));
//        register(Identifier.withDefaultNamespace("squid"), of(SquidRenderer::new));
        register(Identifier.withDefaultNamespace("stray"), of(StrayRenderer::new));
        register(Identifier.withDefaultNamespace("strider"), of(StriderRenderer::new));
        register(Identifier.withDefaultNamespace("sulfur_cube"), of(SulfurCubeRenderer::new));
        register(Identifier.withDefaultNamespace("tadpole"), of(TadpoleRenderer::new));
        register(Identifier.withDefaultNamespace("thrown_item"), of(ThrownItemRenderer::new));
        register(Identifier.withDefaultNamespace("thrown_trident"), of(ThrownTridentRenderer::new));
        register(Identifier.withDefaultNamespace("tippable_arrow"), of(TippableArrowRenderer::new));
        register(Identifier.withDefaultNamespace("tnt_minecart"), of(TntMinecartRenderer::new));
        register(Identifier.withDefaultNamespace("tnt"), of(TntRenderer::new));
        register(Identifier.withDefaultNamespace("tropical_fish"), of(TropicalFishRenderer::new));
        register(Identifier.withDefaultNamespace("turtle"), of(TurtleRenderer::new));
//        register(Identifier.withDefaultNamespace("undead_horse"), of(UndeadHorseRenderer::new));
        register(Identifier.withDefaultNamespace("vex"), of(VexRenderer::new));
        register(Identifier.withDefaultNamespace("villager"), of(VillagerRenderer::new));
        register(Identifier.withDefaultNamespace("vindicator"), of(VindicatorRenderer::new));
        register(Identifier.withDefaultNamespace("wandering_trader"), of(WanderingTraderRenderer::new));
        register(Identifier.withDefaultNamespace("warden"), of(WardenRenderer::new));
        register(Identifier.withDefaultNamespace("wind_charge"), of(WindChargeRenderer::new));
        register(Identifier.withDefaultNamespace("witch"), of(WitchRenderer::new));
        register(Identifier.withDefaultNamespace("wither_boss"), of(WitherBossRenderer::new));
        register(Identifier.withDefaultNamespace("wither_skeleton"), of(WitherSkeletonRenderer::new));
        register(Identifier.withDefaultNamespace("wither_skull"), of(WitherSkullRenderer::new));
        register(Identifier.withDefaultNamespace("wolf"), of(WolfRenderer::new));
        register(Identifier.withDefaultNamespace("zoglin"), of(ZoglinRenderer::new));
        register(Identifier.withDefaultNamespace("zombie_nautilus"), of(ZombieNautilusRenderer::new));
        register(Identifier.withDefaultNamespace("zombie"), of(ZombieRenderer::new));
        register(Identifier.withDefaultNamespace("zombie_villager"), of(ZombieVillagerRenderer::new));
//        register(Identifier.withDefaultNamespace("zombified_piglin"), of(ZombifiedPiglinRenderer::new));
//        register(Identifier.withDefaultNamespace("equipment_layer"), withModel(EquipmentLayerRenderer::new));
        register(Identifier.withDefaultNamespace("avatar"), of(AvatarRenderer::new, Codec.BOOL.fieldOf("slim")));
    }

    public static <T extends Entity> MapCodec<? extends EntityRendererProvider<T>> of(EntityRendererProvider<T> provider) {
        return MapCodec.unit(provider);
    }

    public static <T extends Entity, S extends EntityRenderState> MapCodec<? extends EntityRendererProvider<T>> of(Supplier<EntityRenderer<T, S>> supplier) {
        return MapCodec.unit(context -> supplier.get());
    }

    public static <T extends Entity, S extends EntityRenderState, U> MapCodec<? extends EntityRendererProvider<T>> of(BiFunction<EntityRendererProvider.Context, U, EntityRenderer<T, S>> provider, MapDecoder<U> additional) {
        return CodecUtil.unilateralMap(additional.map(value -> (context -> provider.apply(context, value))));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Entity, S extends EntityRenderState> MapCodec<? extends EntityRendererProvider<T>> withModel(BiFunction<EntityRendererProvider.Context, ModelLayerLocation, EntityRenderer<T, S>> provider) {
        return CodecUtil.unilateralMap(MODEL_LAYER_LOCATION_CODEC.fieldOf("model")
                .map(modelId -> (EntityRendererProvider) (context -> provider.apply(context, modelId))));
    }
}
