package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OxidizableStairsBlock.class)
public abstract class OxidizableStairsBlockMixin extends StairsBlock {
    @Shadow
    public static final MapCodec<OxidizableStairsBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Oxidizable.OxidationLevel.CODEC.fieldOf("weathering_state").forGetter(Degradable::getDegradationLevel),
                            createSettingsCodec()
                    )
                    .apply(instance, (oxidationLevel, settings) -> new OxidizableStairsBlock(oxidationLevel, null, settings))
    );

    public OxidizableStairsBlockMixin(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
    }
}