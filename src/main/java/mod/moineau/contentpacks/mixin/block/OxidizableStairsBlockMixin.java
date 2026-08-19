package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperStairBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WeatheringCopperStairBlock.class)
public abstract class OxidizableStairsBlockMixin extends StairBlock {
    @Shadow
    public static final MapCodec<WeatheringCopperStairBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge),
                            propertiesCodec()
                    )
                    .apply(instance, (weatherState, properties) -> new WeatheringCopperStairBlock(weatherState, null, properties))
    );

    public OxidizableStairsBlockMixin(BlockState baseState, Properties properties) {
        super(baseState, properties);
    }
}