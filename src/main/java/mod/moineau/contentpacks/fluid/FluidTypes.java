package mod.moineau.contentpacks.fluid;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.ContentPacks;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public final class FluidTypes {
    public static void initialize(Registry<MapCodec<? extends ContentFluid>> registry) {
        Registry.register(registry, Identifier.fromNamespaceAndPath(ContentPacks.MOD_ID, "water_like"), WaterLikeFluid.Still.CODEC);
        Registry.register(registry, Identifier.fromNamespaceAndPath(ContentPacks.MOD_ID, "flowing_water_like"), WaterLikeFluid.Flowing.CODEC);
    }
}
