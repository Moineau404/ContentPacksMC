package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;

public final class BlockSoundGroupCodecs {
    public static final Codec<BlockSoundGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(BlockSoundGroup::getVolume),
            Codec.FLOAT.optionalFieldOf("pitch", 1.0F).forGetter(BlockSoundGroup::getPitch),
            Registries.SOUND_EVENT.getCodec().fieldOf("break").forGetter(BlockSoundGroup::getBreakSound),
            Registries.SOUND_EVENT.getCodec().fieldOf("step").forGetter(BlockSoundGroup::getStepSound),
            Registries.SOUND_EVENT.getCodec().fieldOf("place").forGetter(BlockSoundGroup::getPlaceSound),
            Registries.SOUND_EVENT.getCodec().fieldOf("hit").forGetter(BlockSoundGroup::getHitSound),
            Registries.SOUND_EVENT.getCodec().fieldOf("fall").forGetter(BlockSoundGroup::getFallSound)
    ).apply(instance, BlockSoundGroup::new));
}