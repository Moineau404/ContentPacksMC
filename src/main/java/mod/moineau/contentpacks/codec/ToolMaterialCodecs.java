package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class ToolMaterialCodecs {
    public static final Codec<ToolMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TagKey.codec(RegistryKeys.BLOCK).fieldOf("incorrect_blocks").forGetter(ToolMaterial::incorrectBlocksForDrops),
            Codec.INT.fieldOf("durability").forGetter(ToolMaterial::durability),
            Codec.FLOAT.fieldOf("mining_speed").forGetter(ToolMaterial::speed),
            Codec.FLOAT.fieldOf("attack_damage").forGetter(ToolMaterial::attackDamageBonus),
            Codec.INT.fieldOf("enchantability").forGetter(ToolMaterial::enchantmentValue),
            TagKey.codec(RegistryKeys.ITEM).fieldOf("repair_items").forGetter(ToolMaterial::repairItems)
    ).apply(instance, ToolMaterial::new));
}
