package mod.moineau.contentpacks.render.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public record FluidAsset(Identifier stillTexture, Identifier flowingTexture, Optional<Identifier> overlayTexture, int tint) {
    public static final Identifier WATER_STILL = Identifier.ofVanilla("block/water_still");
    public static final Identifier WATER_FLOW = Identifier.ofVanilla("block/water_flow");

    public static final Codec<FluidAsset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("still", WATER_STILL).forGetter(FluidAsset::stillTexture),
            Identifier.CODEC.optionalFieldOf("flow", WATER_FLOW).forGetter(FluidAsset::flowingTexture),
            Identifier.CODEC.optionalFieldOf("overlay").forGetter(FluidAsset::overlayTexture),
            Codecs.ARGB.optionalFieldOf("tint", -1).forGetter(FluidAsset::tint)
    ).apply(instance, FluidAsset::new));

    public DataResult<FluidAsset> register(Fluid fluid) {
        if (fluid instanceof FlowableFluid flowableFluid) {
            FluidRenderHandlerRegistry.INSTANCE.register(flowableFluid.getStill(), flowableFluid.getFlowing(),
                    new SimpleFluidRenderHandler(stillTexture, flowingTexture, overlayTexture.orElse(null), tint));
            return DataResult.success(this);
        }
        return DataResult.error(() -> "Not a flowing fluid: " + fluid);
    }
}
