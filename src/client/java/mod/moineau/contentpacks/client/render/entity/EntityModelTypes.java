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
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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
import traben.entity_model_features.models.EMFModelMappings;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityModelTypes {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends LayerDefinition>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<LayerDefinition> CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(FunctionUtil::nothing, Function.identity());

    public static void register(Identifier id, MapCodec<? extends LayerDefinition> entry) {
        ID_MAPPER.put(id, entry);
    }
    
    public static void bootStrap() {
        register(Identifier.withDefaultNamespace("minecart"), of(MinecartModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("skull"), of(SkullModel::createMobHeadLayer));
        register(Identifier.withDefaultNamespace("equine_saddle"), of(EquineSaddleModel::createSaddleLayer));
        register(Identifier.withDefaultNamespace("adult_axolotl"), of(AdultAxolotlModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("cow"), of(CowModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("cold_chicken"), of(ColdChickenModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("cold_cow"), of(ColdCowModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("elytra"), of(ElytraModel::createLayer));
        register(Identifier.withDefaultNamespace("baby_feline"), of(BabyFelineModel::createBabyLayer));
        register(Identifier.withDefaultNamespace("adult_piglin"), of(AdultPiglinModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("skull"), of(SkullModel::createHumanoidHeadLayer));
        register(Identifier.withDefaultNamespace("adult_strider"), of(AdultStriderModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("baby_strider"), of(BabyStriderModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("hoglin"), of(HoglinModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("baby_hoglin"), of(BabyHoglinModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("skeleton"), of(SkeletonModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("spider"), of(SpiderModel::createSpiderBodyLayer));
        register(Identifier.withDefaultNamespace("adult_camel"), of(AdultCamelModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("baby_camel"), of(BabyCamelModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("camel_saddle"), of(CamelSaddleModel::createSaddleLayer));
        register(Identifier.withDefaultNamespace("adult_chicken"), of(AdultChickenModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("panda"), of(PandaModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("baby_panda"), of(BabyPandaModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("sheep"), of(SheepModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("baby_sheep"), of(BabySheepModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("sheep_fur"), of(SheepFurModel::createFurLayer));
        register(Identifier.withDefaultNamespace("sniffer"), of(SnifferModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("snifflet"), of(SniffletModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("adult_turtle"), of(AdultTurtleModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("warm_cow"), of(WarmCowModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("zombie_villager"), of(ZombieVillagerModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("baby_zombie_villager"), of(BabyZombieVillagerModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("zombie_villager"), of(ZombieVillagerModel::createNoHatLayer));
        register(Identifier.withDefaultNamespace("baby_zombie_villager"), of(BabyZombieVillagerModel::createNoHatLayer));
        register(Identifier.withDefaultNamespace("armor_stand"), of(ArmorStandModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("squid"), of(SquidModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("baby_squid"), of(BabySquidModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("dolphin"), of(DolphinModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("baby_dolphin"), of(BabyDolphinModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("salmon"), of(SalmonModel::createBodyLayer));
        register(Identifier.withDefaultNamespace("nautilus_saddle"), of(NautilusSaddleModel::createSaddleLayer));
        register(Identifier.withDefaultNamespace("boat"), of(BoatModel::createBoatModel));
        register(Identifier.withDefaultNamespace("chest_boat"), of(BoatModel::createChestBoatModel));
        register(Identifier.withDefaultNamespace("raft"), of(RaftModel::createRaftModel));
        register(Identifier.withDefaultNamespace("chest_raft"), of(RaftModel::createChestRaftModel));
        register(Identifier.fromNamespaceAndPath("contentpacks", "dummy"), Codec.STRING.xmap(
                EntityModelTypes::createDummy, model -> ""
        ).fieldOf("mob_name"));
    }

    public static MapCodec<? extends LayerDefinition> of(Supplier<LayerDefinition> supplier){
        return MapCodec.unit(supplier);
    }

    private static LayerDefinition createDummy(String mobName) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        Map<String, String> knownMap = EMFModelMappings.OPTIFINE_MODEL_MAP_CACHE.get(mobName);
        Set<String> partNames = new HashSet<>(knownMap.values());
        partNames.forEach(partName -> root.addOrReplaceChild(partName, CubeListBuilder.create(), PartPose.ZERO));
        return LayerDefinition.create(mesh, 0, 0);
    }
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