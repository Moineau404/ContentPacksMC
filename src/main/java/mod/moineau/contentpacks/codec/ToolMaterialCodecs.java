package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;

public final class ToolMaterialCodecs {
    public static final Codec<ToolMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TagKey.hashedCodec(Registries.BLOCK).fieldOf("incorrect_blocks").forGetter(ToolMaterial::incorrectBlocksForDrops),
            Codec.INT.fieldOf("durability").forGetter(ToolMaterial::durability),
            Codec.FLOAT.fieldOf("mining_speed").forGetter(ToolMaterial::speed),
            Codec.FLOAT.fieldOf("attack_damage").forGetter(ToolMaterial::attackDamageBonus),
            Codec.INT.fieldOf("enchantability").forGetter(ToolMaterial::enchantmentValue),
            TagKey.hashedCodec(Registries.ITEM).fieldOf("repair_items").forGetter(ToolMaterial::repairItems)
    ).apply(instance, ToolMaterial::new));
}
