package mod.moineau.contentpacks.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.properties.BlockSetType;

final class VanillaBlockSetTypes {
    public static void initialize(Registry<BlockSetType> registry) {
        Registry.register(registry, "iron", BlockSetType.IRON);
        Registry.register(registry, "copper", BlockSetType.COPPER);
        Registry.register(registry, "gold", BlockSetType.GOLD);
        Registry.register(registry, "stone", BlockSetType.STONE);
        Registry.register(registry, "polished_blackstone", BlockSetType.POLISHED_BLACKSTONE);
        Registry.register(registry, "oak", BlockSetType.OAK);
        Registry.register(registry, "spruce", BlockSetType.SPRUCE);
        Registry.register(registry, "birch", BlockSetType.BIRCH);
        Registry.register(registry, "acacia", BlockSetType.ACACIA);
        Registry.register(registry, "cherry", BlockSetType.CHERRY);
        Registry.register(registry, "jungle", BlockSetType.JUNGLE);
        Registry.register(registry, "dark_oak", BlockSetType.DARK_OAK);
        Registry.register(registry, "pale_oak", BlockSetType.PALE_OAK);
        Registry.register(registry, "crimson", BlockSetType.CRIMSON);
        Registry.register(registry, "warped", BlockSetType.WARPED);
        Registry.register(registry, "mangrove", BlockSetType.MANGROVE);
        Registry.register(registry, "bamboo", BlockSetType.BAMBOO);
    }
}
