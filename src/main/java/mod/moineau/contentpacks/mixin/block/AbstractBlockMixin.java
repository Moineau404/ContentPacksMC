package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.codec.BlockSettingsCodecs;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Codec replacement
 */
@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    /**
     * @author Moineau
     * @reason Replace block settings codec
     */
    @Overwrite
    public static <B extends Block> RecordCodecBuilder<B, AbstractBlock.Settings> createSettingsCodec() {
        return BlockSettingsCodecs.CODEC.forGetter(AbstractBlock::getSettings);
    }
}
