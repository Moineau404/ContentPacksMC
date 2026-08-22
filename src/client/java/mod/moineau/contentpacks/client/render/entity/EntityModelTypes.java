package mod.moineau.contentpacks.client.render.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.api.util.FunctionUtil;
import net.minecraft.client.model.animal.axolotl.AdultAxolotlModel;
import net.minecraft.client.model.animal.camel.AdultCamelModel;
import net.minecraft.client.model.animal.camel.BabyCamelModel;
import net.minecraft.client.model.animal.camel.CamelSaddleModel;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.animal.cow.WarmCowModel;
import net.minecraft.client.model.animal.dolphin.BabyDolphinModel;
import net.minecraft.client.model.animal.dolphin.DolphinModel;
import net.minecraft.client.model.animal.equine.EquineSaddleModel;
import net.minecraft.client.model.animal.feline.BabyFelineModel;
import net.minecraft.client.model.animal.fish.SalmonModel;
import net.minecraft.client.model.animal.nautilus.NautilusSaddleModel;
import net.minecraft.client.model.animal.panda.BabyPandaModel;
import net.minecraft.client.model.animal.panda.PandaModel;
import net.minecraft.client.model.animal.sheep.BabySheepModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.animal.sniffer.SnifferModel;
import net.minecraft.client.model.animal.sniffer.SniffletModel;
import net.minecraft.client.model.animal.squid.BabySquidModel;
import net.minecraft.client.model.animal.squid.SquidModel;
import net.minecraft.client.model.animal.turtle.AdultTurtleModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.monster.hoglin.BabyHoglinModel;
import net.minecraft.client.model.monster.hoglin.HoglinModel;
import net.minecraft.client.model.monster.piglin.AdultPiglinModel;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.model.monster.strider.AdultStriderModel;
import net.minecraft.client.model.monster.strider.BabyStriderModel;
import net.minecraft.client.model.monster.zombie.BabyZombieVillagerModel;
import net.minecraft.client.model.monster.zombie.ZombieVillagerModel;
import net.minecraft.client.model.object.armorstand.ArmorStandModel;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.model.object.boat.RaftModel;
import net.minecraft.client.model.object.cart.MinecartModel;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;
import java.util.function.Supplier;

//import traben.entity_model_features.models.EMFModelMappings;

public class EntityModelTypes {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends LayerDefinition>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<LayerDefinition> CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(FunctionUtil::nothing, Function.identity());
    public static final MapCodec<? extends LayerDefinition> MINECART = of(MinecartModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> SKULL = Codec.BOOL.fieldOf("hat").xmap(
            hat -> hat ? SkullModel.createHumanoidHeadLayer() : SkullModel.createMobHeadLayer(),
            FunctionUtil::nothing
    );
    public static final MapCodec<? extends LayerDefinition> EQUINE_SADDLE = of(EquineSaddleModel::createSaddleLayer);
    public static final MapCodec<? extends LayerDefinition> ADULT_AXOLOTL = of(AdultAxolotlModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> COW = of(CowModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> COLD_CHICKEN = of(ColdChickenModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> COLD_COW = of(ColdCowModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> ELYTRA = of(ElytraModel::createLayer);
    public static final MapCodec<? extends LayerDefinition> BABY_FELINE = of(BabyFelineModel::createBabyLayer);
    public static final MapCodec<? extends LayerDefinition> ADULT_PIGLIN = of(AdultPiglinModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> ADULT_STRIDER = of(AdultStriderModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> BABY_STRIDER = of(BabyStriderModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> HOGLIN = of(HoglinModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> BABY_HOGLIN = of(BabyHoglinModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> SKELETON = of(SkeletonModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> SPIDER = of(SpiderModel::createSpiderBodyLayer);
    public static final MapCodec<? extends LayerDefinition> ADULT_CAMEL = of(AdultCamelModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> BABY_CAMEL = of(BabyCamelModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> CAMEL_SADDLE = of(CamelSaddleModel::createSaddleLayer);
    public static final MapCodec<? extends LayerDefinition> ADULT_CHICKEN = of(AdultChickenModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> PANDA = of(PandaModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> BABY_PANDA = of(BabyPandaModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> SHEEP = of(SheepModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> BABY_SHEEP = of(BabySheepModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> SHEEP_FUR = of(SheepFurModel::createFurLayer);
    public static final MapCodec<? extends LayerDefinition> SNIFFER = of(SnifferModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> SNIFFLET = of(SniffletModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> ADULT_TURTLE = of(AdultTurtleModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> WARM_COW = of(WarmCowModel::createBodyLayer);

    public static final MapCodec<? extends LayerDefinition> ZOMBIE_VILLAGER = Codec.BOOL.fieldOf("hat").xmap(
            hat -> hat ? ZombieVillagerModel.createBodyLayer() : ZombieVillagerModel.createNoHatLayer(),
            FunctionUtil::nothing
    );
    public static final MapCodec<? extends LayerDefinition> BABY_ZOMBIE_VILLAGER = Codec.BOOL.fieldOf("hat").xmap(
            hat -> hat ? BabyZombieVillagerModel.createBodyLayer() : BabyZombieVillagerModel.createNoHatLayer(),
            FunctionUtil::nothing
    );
    public static final MapCodec<? extends LayerDefinition> ARMOR_STAND = of(ArmorStandModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> SQUID = of(SquidModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> BABY_SQUID = of(BabySquidModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> DOLPHIN = of(DolphinModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> BABY_DOLPHIN = of(BabyDolphinModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> SALMON = of(SalmonModel::createBodyLayer);
    public static final MapCodec<? extends LayerDefinition> NAUTILUS_SADDLE = of(NautilusSaddleModel::createSaddleLayer);
    public static final MapCodec<? extends LayerDefinition> BOAT = of(BoatModel::createBoatModel);
    public static final MapCodec<? extends LayerDefinition> CHEST_BOAT = of(BoatModel::createChestBoatModel);
    public static final MapCodec<? extends LayerDefinition> RAFT = of(RaftModel::createRaftModel);
    public static final MapCodec<? extends LayerDefinition> CHEST_RAFT = of(RaftModel::createChestRaftModel);

    public static void bootStrap() {
        register(Identifier.withDefaultNamespace("minecart"), MINECART);
        register(Identifier.withDefaultNamespace("skull"), SKULL);
        register(Identifier.withDefaultNamespace("equine_saddle"), EQUINE_SADDLE);
        register(Identifier.withDefaultNamespace("adult_axolotl"), ADULT_AXOLOTL);
        register(Identifier.withDefaultNamespace("cow"), COW);
        register(Identifier.withDefaultNamespace("cold_chicken"), COLD_CHICKEN);
        register(Identifier.withDefaultNamespace("cold_cow"), COLD_COW);
        register(Identifier.withDefaultNamespace("elytra"), ELYTRA);
        register(Identifier.withDefaultNamespace("baby_feline"), BABY_FELINE);
        register(Identifier.withDefaultNamespace("adult_piglin"), ADULT_PIGLIN);
        register(Identifier.withDefaultNamespace("skull"), SKULL);
        register(Identifier.withDefaultNamespace("adult_strider"), ADULT_STRIDER);
        register(Identifier.withDefaultNamespace("baby_strider"), BABY_STRIDER);
        register(Identifier.withDefaultNamespace("hoglin"), HOGLIN);
        register(Identifier.withDefaultNamespace("baby_hoglin"), BABY_HOGLIN);
        register(Identifier.withDefaultNamespace("skeleton"), SKELETON);
        register(Identifier.withDefaultNamespace("spider"), SPIDER);
        register(Identifier.withDefaultNamespace("adult_camel"), ADULT_CAMEL);
        register(Identifier.withDefaultNamespace("baby_camel"), BABY_CAMEL);
        register(Identifier.withDefaultNamespace("camel_saddle"), CAMEL_SADDLE);
        register(Identifier.withDefaultNamespace("adult_chicken"), ADULT_CHICKEN);
        register(Identifier.withDefaultNamespace("panda"), PANDA);
        register(Identifier.withDefaultNamespace("baby_panda"), BABY_PANDA);
        register(Identifier.withDefaultNamespace("sheep"), SHEEP);
        register(Identifier.withDefaultNamespace("baby_sheep"), BABY_SHEEP);
        register(Identifier.withDefaultNamespace("sheep_fur"), SHEEP_FUR);
        register(Identifier.withDefaultNamespace("sniffer"), SNIFFER);
        register(Identifier.withDefaultNamespace("snifflet"), SNIFFLET);
        register(Identifier.withDefaultNamespace("adult_turtle"), ADULT_TURTLE);
        register(Identifier.withDefaultNamespace("warm_cow"), WARM_COW);
        register(Identifier.withDefaultNamespace("zombie_villager"), ZOMBIE_VILLAGER);
        register(Identifier.withDefaultNamespace("baby_zombie_villager"), BABY_ZOMBIE_VILLAGER);
        register(Identifier.withDefaultNamespace("zombie_villager"), ZOMBIE_VILLAGER);
        register(Identifier.withDefaultNamespace("baby_zombie_villager"), BABY_ZOMBIE_VILLAGER);
        register(Identifier.withDefaultNamespace("armor_stand"), ARMOR_STAND);
        register(Identifier.withDefaultNamespace("squid"), SQUID);
        register(Identifier.withDefaultNamespace("baby_squid"), BABY_SQUID);
        register(Identifier.withDefaultNamespace("dolphin"), DOLPHIN);
        register(Identifier.withDefaultNamespace("baby_dolphin"), BABY_DOLPHIN);
        register(Identifier.withDefaultNamespace("salmon"), SALMON);
        register(Identifier.withDefaultNamespace("nautilus_saddle"), NAUTILUS_SADDLE);
        register(Identifier.withDefaultNamespace("boat"), BOAT);
        register(Identifier.withDefaultNamespace("chest_boat"), CHEST_BOAT);
        register(Identifier.withDefaultNamespace("raft"), RAFT);
        register(Identifier.withDefaultNamespace("chest_raft"), CHEST_RAFT);
//        register(Identifier.fromNamespaceAndPath("contentpacks", "dummy"), Codec.STRING.xmap(
//                EntityModelTypes::createDummy, model -> ""
//        ).fieldOf("mob_name"));
    }

    public static MapCodec<? extends LayerDefinition> of(Supplier<LayerDefinition> supplier){
        return MapCodec.unit(supplier);
    }

    public static void register(Identifier id, MapCodec<? extends LayerDefinition> entry) {
        ID_MAPPER.put(id, entry);
    }

//    private static LayerDefinition createDummy(String mobName) {
//        MeshDefinition mesh = new MeshDefinition();
//        PartDefinition root = mesh.getRoot();
//        Map<String, String> knownMap = EMFModelMappings.OPTIFINE_MODEL_MAP_CACHE.get(mobName);
//        Set<String> partNames = new HashSet<>(knownMap.values());
//        partNames.forEach(partName -> root.addOrReplaceChild(partName, CubeListBuilder.create(), PartPose.ZERO));
//        return LayerDefinition.create(mesh, 0, 0);
//    }
}

/*
Humanoid
Quadruped
Bat
Allay
AdultArmadillo
Armadillo
BabyArmadillo
AdultAxolotl
BabyAxolotl
AdultBee
BabyBee
Bee
AdultCamel
BabyCamel
Camel
CamelSaddle
AdultChicken
BabyChicken
Chicken
ColdChicken
BabyCow
ColdCow
Cow
WarmCow
BabyDolphin
Dolphin
AbstractEquine
BabyDonkey
BabyHorse
Donkey
EquineSaddle
Horse
AbstractFeline
AdultCat
AdultFeline
AdultOcelot
BabyCat
BabyFeline
BabyOcelot
Cod
PufferfishBig
PufferfishMid
PufferfishSmall
Salmon
TropicalFishLarge
TropicalFishSmall
AdultFox
BabyFox
Fox
Frog
Tadpole
HappyGhastHarness
HappyGhast
BabyGoat
Goat
CopperGolem
IronGolem
SnowGolem
BabyLlama
Llama
LlamaSpit
NautilusArmor
Nautilus
NautilusSaddle
BabyPanda
Panda
Parrot
BabyPig
ColdPig
Pig
BabyPolarBear
PolarBear
AdultRabbit
BabyRabbit
Rabbit
BabySheep
SheepFur
Sheep
Sniffer
Snifflet
BabySquid
Squid
AdultTurtle
BabyTurtle
Turtle
AdultWolf
BabyWolf
Wolf
EvokerFangs
SpinAttackEffect
Blaze
Breeze
Creaking
Creeper
EnderDragon
Enderman
Endermite
Ghast
Guardian
BabyHoglin
Hoglin
Illager
ZombieNautilusCoral
Phantom
AbstractPiglin
AdultPiglin
AdultZombifiedPiglin
BabyPiglin
BabyZombifiedPiglin
Piglin
ZombifiedPiglin
Ravager
Shulker
Silverfish
Bogged
Skeleton
MagmaCube
Slime
SmallSulfurCube
SulfurCube
Spider
AdultStrider
BabyStrider
Strider
Vex
Warden
Witch
WitherBoss
AbstractZombie
BabyDrowned
BabyZombie
BabyZombieVillager
Drowned
GiantZombie
Zombie
ZombieVillager
BabyVillager
Villager
ArmorStandArmor
ArmorStand
AbstractBoat
Boat
Raft
Minecart
EndCrystal
Elytra
LeashKnot
Arrow
ShulkerBullet
WindCharge
PlayerCape
PlayerEars
Player
 */