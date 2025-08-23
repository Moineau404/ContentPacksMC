package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.CodecUtil;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class ArmorMaterialCodecs {
    public static final Codec<ArmorMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("durability").forGetter(ArmorMaterial::durability),
            CodecUtil.enumMap(EquipmentType.class, Codec.INT, true).fieldOf("defense").forGetter(ArmorMaterial::defense),
            Codec.INT.fieldOf("enchantability").forGetter(ArmorMaterial::enchantmentValue),
            Registries.SOUND_EVENT.getEntryCodec().fieldOf("equip_sound").forGetter(ArmorMaterial::equipSound),
            Codec.FLOAT.fieldOf("toughness").forGetter(ArmorMaterial::toughness),
            Codec.FLOAT.fieldOf("knockback_resistance").forGetter(ArmorMaterial::knockbackResistance),
            TagKey.codec(RegistryKeys.ITEM).fieldOf("repair_items").forGetter(ArmorMaterial::repairIngredient),
            RegistryKey.createCodec(EquipmentAssetKeys.REGISTRY_KEY).fieldOf("asset").forGetter(ArmorMaterial::assetId)
    ).apply(instance, ArmorMaterial::new));
}
