package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.BlockSetType;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public final class BlockSetTypeCodecs {
    public static final Codec<BlockSetType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            Codec.BOOL.fieldOf("open_by_hand").forGetter(BlockSetType::canOpenByHand),
            Codec.BOOL.fieldOf("open_by_wind_charge").forGetter(BlockSetType::canOpenByWindCharge),
            Codec.BOOL.fieldOf("button_activated_by_arrows").forGetter(BlockSetType::canButtonBeActivatedByArrows),
            VanillaCodecs.ACTIVATION_RULE.fieldOf("pressure_plate_sensivity").forGetter(BlockSetType::pressurePlateSensitivity),
            ContentRegistries.BLOCK_SOUND_GROUP.getCodec().fieldOf("sound_type").forGetter(BlockSetType::soundType),
            Registries.SOUND_EVENT.getCodec().fieldOf("door_close").forGetter(BlockSetType::doorClose),
            Registries.SOUND_EVENT.getCodec().fieldOf("door_open").forGetter(BlockSetType::doorOpen),
            Registries.SOUND_EVENT.getCodec().fieldOf("trapdoor_close").forGetter(BlockSetType::trapdoorClose),
            Registries.SOUND_EVENT.getCodec().fieldOf("trapdoor_open").forGetter(BlockSetType::trapdoorOpen),
            Registries.SOUND_EVENT.getCodec().fieldOf("pressure_plate_off").forGetter(BlockSetType::pressurePlateClickOff),
            Registries.SOUND_EVENT.getCodec().fieldOf("pressure_plate_on").forGetter(BlockSetType::pressurePlateClickOn),
            Registries.SOUND_EVENT.getCodec().fieldOf("button_off").forGetter(BlockSetType::buttonClickOff),
            Registries.SOUND_EVENT.getCodec().fieldOf("button_on").forGetter(BlockSetType::buttonClickOn)
    ).apply(instance, BlockSetType::new));
    public static final Codec<BlockSetType> OPTIONAL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            Codec.BOOL.optionalFieldOf("open_by_hand", true).forGetter(BlockSetType::canOpenByHand),
            Codec.BOOL.optionalFieldOf("open_by_wind_charge", true).forGetter(BlockSetType::canOpenByWindCharge),
            Codec.BOOL.optionalFieldOf("button_activated_by_arrows", true).forGetter(BlockSetType::canButtonBeActivatedByArrows),
            VanillaCodecs.ACTIVATION_RULE.optionalFieldOf("pressure_plate_sensivity", BlockSetType.ActivationRule.EVERYTHING).forGetter(BlockSetType::pressurePlateSensitivity),
            ContentRegistries.BLOCK_SOUND_GROUP.getCodec().optionalFieldOf("sound_type", BlockSoundGroup.WOOD).forGetter(BlockSetType::soundType),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("door_close", SoundEvents.BLOCK_WOODEN_DOOR_CLOSE).forGetter(BlockSetType::doorClose),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("door_open", SoundEvents.BLOCK_WOODEN_DOOR_OPEN).forGetter(BlockSetType::doorOpen),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("trapdoor_close", SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE).forGetter(BlockSetType::trapdoorClose),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("trapdoor_open", SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN).forGetter(BlockSetType::trapdoorOpen),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("pressure_plate_off", SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF).forGetter(BlockSetType::pressurePlateClickOff),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("pressure_plate_on", SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON).forGetter(BlockSetType::pressurePlateClickOn),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("button_off", SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF).forGetter(BlockSetType::buttonClickOff),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("button_on", SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON).forGetter(BlockSetType::buttonClickOn)
    ).apply(instance, BlockSetType::new));
}
