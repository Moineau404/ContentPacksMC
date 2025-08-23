package mod.moineau.contentpacks.registry;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.Registry;

final class VanillaSaplingGenerators {
    public static void initialize(Registry<SaplingGenerator> registry) {
        Registry.register(registry, "oak", SaplingGenerator.OAK);
        Registry.register(registry, "spruce", SaplingGenerator.SPRUCE);
        Registry.register(registry, "mangrove", SaplingGenerator.MANGROVE);
        Registry.register(registry, "azalea", SaplingGenerator.AZALEA);
        Registry.register(registry, "birch", SaplingGenerator.BIRCH);
        Registry.register(registry, "jungle", SaplingGenerator.JUNGLE);
        Registry.register(registry, "acacia", SaplingGenerator.ACACIA);
        Registry.register(registry, "cherry", SaplingGenerator.CHERRY);
        Registry.register(registry, "dark_oak", SaplingGenerator.DARK_OAK);
        Registry.register(registry, "pale_oak", SaplingGenerator.PALE_OAK);
    }
}
