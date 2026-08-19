package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class BlockSetTypeCodecs {
    public static final Codec<BlockSetType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            Codec.BOOL.fieldOf("open_by_hand").forGetter(BlockSetType::canOpenByHand),
            Codec.BOOL.fieldOf("open_by_wind_charge").forGetter(BlockSetType::canOpenByWindCharge),
            Codec.BOOL.fieldOf("button_activated_by_arrows").forGetter(BlockSetType::canButtonBeActivatedByArrows),
            VanillaCodecs.ACTIVATION_RULE.fieldOf("pressure_plate_sensivity").forGetter(BlockSetType::pressurePlateSensitivity),
            ContentRegistries.SOUND_TYPE.byNameCodec().fieldOf("sound_type").forGetter(BlockSetType::soundType),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("door_close").forGetter(BlockSetType::doorClose),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("door_open").forGetter(BlockSetType::doorOpen),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("trapdoor_close").forGetter(BlockSetType::trapdoorClose),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("trapdoor_open").forGetter(BlockSetType::trapdoorOpen),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("pressure_plate_off").forGetter(BlockSetType::pressurePlateClickOff),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("pressure_plate_on").forGetter(BlockSetType::pressurePlateClickOn),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("button_off").forGetter(BlockSetType::buttonClickOff),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("button_on").forGetter(BlockSetType::buttonClickOn)
    ).apply(instance, BlockSetType::new));
    public static final Codec<BlockSetType> OPTIONAL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            Codec.BOOL.optionalFieldOf("open_by_hand", true).forGetter(BlockSetType::canOpenByHand),
            Codec.BOOL.optionalFieldOf("open_by_wind_charge", true).forGetter(BlockSetType::canOpenByWindCharge),
            Codec.BOOL.optionalFieldOf("button_activated_by_arrows", true).forGetter(BlockSetType::canButtonBeActivatedByArrows),
            VanillaCodecs.ACTIVATION_RULE.optionalFieldOf("pressure_plate_sensivity", BlockSetType.PressurePlateSensitivity.EVERYTHING).forGetter(BlockSetType::pressurePlateSensitivity),
            ContentRegistries.SOUND_TYPE.byNameCodec().optionalFieldOf("sound_type", SoundType.WOOD).forGetter(BlockSetType::soundType),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("door_close", SoundEvents.WOODEN_DOOR_CLOSE).forGetter(BlockSetType::doorClose),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("door_open", SoundEvents.WOODEN_DOOR_OPEN).forGetter(BlockSetType::doorOpen),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("trapdoor_close", SoundEvents.WOODEN_TRAPDOOR_CLOSE).forGetter(BlockSetType::trapdoorClose),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("trapdoor_open", SoundEvents.WOODEN_TRAPDOOR_OPEN).forGetter(BlockSetType::trapdoorOpen),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("pressure_plate_off", SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF).forGetter(BlockSetType::pressurePlateClickOff),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("pressure_plate_on", SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON).forGetter(BlockSetType::pressurePlateClickOn),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("button_off", SoundEvents.WOODEN_BUTTON_CLICK_OFF).forGetter(BlockSetType::buttonClickOff),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("button_on", SoundEvents.WOODEN_BUTTON_CLICK_ON).forGetter(BlockSetType::buttonClickOn)
    ).apply(instance, BlockSetType::new));
}
