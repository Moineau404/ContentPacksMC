package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.CodecUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class ArmorMaterialCodecs {
    public static final Codec<ArmorMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("durability").forGetter(ArmorMaterial::durability),
            CodecUtil.enumMap(ArmorType.class, Codec.INT, true).fieldOf("defense").forGetter(ArmorMaterial::defense),
            Codec.INT.fieldOf("enchantability").forGetter(ArmorMaterial::enchantmentValue),
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("equip_sound").forGetter(ArmorMaterial::equipSound),
            Codec.FLOAT.fieldOf("toughness").forGetter(ArmorMaterial::toughness),
            Codec.FLOAT.fieldOf("knockback_resistance").forGetter(ArmorMaterial::knockbackResistance),
            TagKey.hashedCodec(Registries.ITEM).fieldOf("repair_items").forGetter(ArmorMaterial::repairIngredient),
            ResourceKey.codec(EquipmentAssets.ROOT_ID).fieldOf("asset").forGetter(ArmorMaterial::assetId)
    ).apply(instance, ArmorMaterial::new));
}
