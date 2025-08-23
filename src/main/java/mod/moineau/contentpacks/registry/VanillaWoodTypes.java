package mod.moineau.contentpacks.registry;

import net.minecraft.block.WoodType;
import net.minecraft.registry.Registry;

final class VanillaWoodTypes {
    public static void initialize(Registry<WoodType> registry) {
        Registry.register(registry, "oak", WoodType.OAK);
        Registry.register(registry, "spruce", WoodType.SPRUCE);
        Registry.register(registry, "birch", WoodType.BIRCH);
        Registry.register(registry, "acacia", WoodType.ACACIA);
        Registry.register(registry, "cherry", WoodType.CHERRY);
        Registry.register(registry, "jungle", WoodType.JUNGLE);
        Registry.register(registry, "dark_oak", WoodType.DARK_OAK);
        Registry.register(registry, "pale_oak", WoodType.PALE_OAK);
        Registry.register(registry, "crimson", WoodType.CRIMSON);
        Registry.register(registry, "warped", WoodType.WARPED);
        Registry.register(registry, "mangrove", WoodType.MANGROVE);
        Registry.register(registry, "bamboo", WoodType.BAMBOO);
    }
}
