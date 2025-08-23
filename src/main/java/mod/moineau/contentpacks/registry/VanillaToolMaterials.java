package mod.moineau.contentpacks.registry;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registry;

final class VanillaToolMaterials {
    public static void initialize(Registry<ToolMaterial> registry) {
        Registry.register(registry, "wood", ToolMaterial.WOOD);
        Registry.register(registry, "stone", ToolMaterial.STONE);
        Registry.register(registry, "iron", ToolMaterial.IRON);
        Registry.register(registry, "diamond", ToolMaterial.DIAMOND);
        Registry.register(registry, "gold", ToolMaterial.GOLD);
        Registry.register(registry, "netherite", ToolMaterial.NETHERITE);
    }
}
