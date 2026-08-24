package mod.moineau.contentpacks.fluid;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.fluid.ContentFluid;
import mod.moineau.contentpacks.api.fluid.WaterLikeFluid;
import net.minecraft.core.Registry;

public final class FluidTypes {
    public static void initialize(Registry<MapCodec<? extends ContentFluid>> registry) {
        Registry.register(registry, ContentPacks.id("water_like"), WaterLikeFluid.Still.CODEC);
        Registry.register(registry, ContentPacks.id("flowing_water_like"), WaterLikeFluid.Flowing.CODEC);
    }
}
