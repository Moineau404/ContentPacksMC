package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.SaplingGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(SaplingGenerator.class)
public class SaplingGeneratorMixin {
    @Shadow
    public static final Codec<SaplingGenerator> CODEC = ContentRegistries.SAPLING_GENERATOR.getCodec();
}
