package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;

public final class TreeGrowerCodecs {
    public static final Codec<TreeGrower> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            Codec.FLOAT.optionalFieldOf("secondary_chance", 0.0F).forGetter(sapling -> sapling.secondaryChance),
            ResourceKey.codec(Registries.CONFIGURED_FEATURE).optionalFieldOf("mega_tree").forGetter(sapling -> sapling.megaTree),
            ResourceKey.codec(Registries.CONFIGURED_FEATURE).optionalFieldOf("secondary_mega_tree").forGetter(sapling -> sapling.secondaryMegaTree),
            ResourceKey.codec(Registries.CONFIGURED_FEATURE).optionalFieldOf("tree").forGetter(sapling -> sapling.tree),
            ResourceKey.codec(Registries.CONFIGURED_FEATURE).optionalFieldOf("secondary_tree").forGetter(sapling -> sapling.secondaryTree),
            ResourceKey.codec(Registries.CONFIGURED_FEATURE).optionalFieldOf("flowers").forGetter(sapling -> sapling.flowers),
            ResourceKey.codec(Registries.CONFIGURED_FEATURE).optionalFieldOf("secondary_flowers").forGetter(sapling -> sapling.secondaryFlowers)
    ).apply(instance, TreeGrower::new));
}
