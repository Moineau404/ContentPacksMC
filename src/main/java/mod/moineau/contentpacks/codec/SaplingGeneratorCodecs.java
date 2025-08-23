package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class SaplingGeneratorCodecs {
    public static final Codec<SaplingGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            Codec.FLOAT.optionalFieldOf("secondary_chance", 0.0F).forGetter(sapling -> sapling.rareChance),
            RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).optionalFieldOf("mega_tree").forGetter(sapling -> sapling.megaVariant),
            RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).optionalFieldOf("secondary_mega_tree").forGetter(sapling -> sapling.rareMegaVariant),
            RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).optionalFieldOf("tree").forGetter(sapling -> sapling.regularVariant),
            RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).optionalFieldOf("secondary_tree").forGetter(sapling -> sapling.rareRegularVariant),
            RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).optionalFieldOf("flowers").forGetter(sapling -> sapling.beesVariant),
            RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).optionalFieldOf("secondary_flowers").forGetter(sapling -> sapling.rareBeesVariant)
    ).apply(instance, SaplingGenerator::new));
}
