package mod.moineau.contentpacks.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;

final class VanillaArmorMaterials {
    public static void initialize(Registry<ArmorMaterial> registry) {
        Registry.register(registry, "leather", ArmorMaterials.LEATHER);
        Registry.register(registry, "copper", ArmorMaterials.COPPER);
        Registry.register(registry, "chainmail", ArmorMaterials.CHAINMAIL);
        Registry.register(registry, "iron", ArmorMaterials.IRON);
        Registry.register(registry, "gold", ArmorMaterials.GOLD);
        Registry.register(registry, "diamond", ArmorMaterials.DIAMOND);
        Registry.register(registry, "turtle_scute", ArmorMaterials.TURTLE_SCUTE);
        Registry.register(registry, "netherite", ArmorMaterials.NETHERITE);
        Registry.register(registry, "armadillo_scute", ArmorMaterials.ARMADILLO_SCUTE);
    }
}
