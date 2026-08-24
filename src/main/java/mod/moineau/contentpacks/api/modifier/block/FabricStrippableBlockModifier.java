package mod.moineau.contentpacks.api.modifier.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.modifier.Modifier;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record FabricStrippableBlockModifier(Block stripped) implements Modifier<Block> {
    public static final Codec<FabricStrippableBlockModifier> CODEC = BuiltInRegistries.BLOCK.byNameCodec()
            .xmap(FabricStrippableBlockModifier::new, FabricStrippableBlockModifier::stripped);

    @Override
    public void apply(Block target) {
        StrippableBlockRegistry.register(target, stripped);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Optional<FabricStrippableBlockModifier> get(Block block) {
        return Optional.ofNullable(AxeItemAccessor.getStrippables().get(block)).map(FabricStrippableBlockModifier::new);
    }
}
