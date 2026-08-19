package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.codec.BlockPropertiesCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Codec replacement
 */
@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    /**
     * @author Moineau
     * @reason Replace block properties codec
     */
    @Overwrite
    public static <B extends Block> RecordCodecBuilder<B, BlockBehaviour.Properties> propertiesCodec() {
        return BlockPropertiesCodecs.CODEC.forGetter(BlockBehaviour::properties);
    }
}
