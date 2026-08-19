package mod.moineau.contentpacks.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.grower.TreeGrower;

final class VanillaTreeGrowers {
    public static void initialize(Registry<TreeGrower> registry) {
        Registry.register(registry, "oak", TreeGrower.OAK);
        Registry.register(registry, "spruce", TreeGrower.SPRUCE);
        Registry.register(registry, "mangrove", TreeGrower.MANGROVE);
        Registry.register(registry, "azalea", TreeGrower.AZALEA);
        Registry.register(registry, "birch", TreeGrower.BIRCH);
        Registry.register(registry, "jungle", TreeGrower.JUNGLE);
        Registry.register(registry, "acacia", TreeGrower.ACACIA);
        Registry.register(registry, "cherry", TreeGrower.CHERRY);
        Registry.register(registry, "dark_oak", TreeGrower.DARK_OAK);
        Registry.register(registry, "pale_oak", TreeGrower.PALE_OAK);
    }
}
