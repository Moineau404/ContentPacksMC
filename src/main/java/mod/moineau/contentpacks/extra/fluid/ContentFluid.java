package mod.moineau.contentpacks.extra.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.world.level.material.Fluid;
import java.util.function.Function;

public interface ContentFluid {
    Codec<ContentFluid> CODEC = ContentRegistries.FLUID_TYPE.byNameCodec().dispatch(ContentFluid::getCodec, Function.identity());
    Codec<Fluid> DOWNGRADED_CODEC = CODEC.flatXmap(
            contentFluid -> {
                if (contentFluid instanceof Fluid fluid) {
                    return DataResult.success(fluid);
                } else {
                    return DataResult.error(() -> "Object is not a fluid but implements interface Content Fluid!");
                }
            },
            fluid -> {
                if (fluid instanceof ContentFluid contentFluid) {
                    return DataResult.success(contentFluid);
                } else {
                    return DataResult.error(() -> "Fluid is not a Content Fluid and cannot be serialized!");
                }
            }
    );

    MapCodec<? extends ContentFluid> getCodec();
}
