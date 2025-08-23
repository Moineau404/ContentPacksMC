package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public final class WoodTypeCodecs {
    public static final Codec<WoodType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            BlockSetType.CODEC.fieldOf("block_set_type").forGetter(WoodType::setType),
            ContentRegistries.BLOCK_SOUND_GROUP.getCodec().fieldOf("sound_type").forGetter(WoodType::soundType),
            ContentRegistries.BLOCK_SOUND_GROUP.getCodec().fieldOf("hanging_sign_sound_type").forGetter(WoodType::hangingSignSoundType),
            Registries.SOUND_EVENT.getCodec().fieldOf("fence_gate_close").forGetter(WoodType::fenceGateClose),
            Registries.SOUND_EVENT.getCodec().fieldOf("fence_gate_open").forGetter(WoodType::fenceGateOpen)
    ).apply(instance, WoodType::new));
    public static final Codec<WoodType> OPTIONAL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            BlockSetType.CODEC.fieldOf("block_set_type").forGetter(WoodType::setType),
            ContentRegistries.BLOCK_SOUND_GROUP.getCodec().optionalFieldOf("sound_type", BlockSoundGroup.WOOD).forGetter(WoodType::soundType),
            ContentRegistries.BLOCK_SOUND_GROUP.getCodec().optionalFieldOf("hanging_sign_sound_type", BlockSoundGroup.HANGING_SIGN).forGetter(WoodType::hangingSignSoundType),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("fence_gate_close", SoundEvents.BLOCK_FENCE_GATE_CLOSE).forGetter(WoodType::fenceGateClose),
            Registries.SOUND_EVENT.getCodec().optionalFieldOf("fence_gate_open", SoundEvents.BLOCK_FENCE_GATE_OPEN).forGetter(WoodType::fenceGateOpen)
    ).apply(instance, WoodType::new));
}
