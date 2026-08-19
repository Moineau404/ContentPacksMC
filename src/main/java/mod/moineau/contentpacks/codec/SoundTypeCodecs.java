package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.SoundType;

public final class SoundTypeCodecs {
    public static final Codec<SoundType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(SoundType::getVolume),
            Codec.FLOAT.optionalFieldOf("pitch", 1.0F).forGetter(SoundType::getPitch),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("break").forGetter(SoundType::getBreakSound),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("step").forGetter(SoundType::getStepSound),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("place").forGetter(SoundType::getPlaceSound),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("hit").forGetter(SoundType::getHitSound),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("fall").forGetter(SoundType::getFallSound)
    ).apply(instance, SoundType::new));
}