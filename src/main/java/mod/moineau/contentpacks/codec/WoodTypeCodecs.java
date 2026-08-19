package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class WoodTypeCodecs {
    public static final Codec<WoodType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            BlockSetType.CODEC.fieldOf("block_set_type").forGetter(WoodType::setType),
            ContentRegistries.SOUND_TYPE.byNameCodec().fieldOf("sound_type").forGetter(WoodType::soundType),
            ContentRegistries.SOUND_TYPE.byNameCodec().fieldOf("hanging_sign_sound_type").forGetter(WoodType::hangingSignSoundType),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("fence_gate_close").forGetter(WoodType::fenceGateClose),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("fence_gate_open").forGetter(WoodType::fenceGateOpen)
    ).apply(instance, WoodType::new));
    public static final Codec<WoodType> OPTIONAL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectSlashId(),
            BlockSetType.CODEC.fieldOf("block_set_type").forGetter(WoodType::setType),
            ContentRegistries.SOUND_TYPE.byNameCodec().optionalFieldOf("sound_type", SoundType.WOOD).forGetter(WoodType::soundType),
            ContentRegistries.SOUND_TYPE.byNameCodec().optionalFieldOf("hanging_sign_sound_type", SoundType.HANGING_SIGN).forGetter(WoodType::hangingSignSoundType),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("fence_gate_close", SoundEvents.FENCE_GATE_CLOSE).forGetter(WoodType::fenceGateClose),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("fence_gate_open", SoundEvents.FENCE_GATE_OPEN).forGetter(WoodType::fenceGateOpen)
    ).apply(instance, WoodType::new));
}
