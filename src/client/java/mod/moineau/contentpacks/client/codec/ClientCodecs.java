package mod.moineau.contentpacks.client.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.api.util.CodecUtil;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSource;
import mod.moineau.contentpacks.client.render.block.tint.BlockTintSourceTypes;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;

public class ClientCodecs {
    public static final Codec<FluidModel.Unbaked> FLUID_MODEL = RecordCodecBuilder.create(instance -> instance.group(
            Material.CODEC.fieldOf("still").forGetter(FluidModel.Unbaked::stillMaterial),
            Material.CODEC.fieldOf("flowing").forGetter(FluidModel.Unbaked::flowingMaterial),
            CodecUtil.nullable(Material.CODEC, "overlay").forGetter(FluidModel.Unbaked::overlayMaterial),
            CodecUtil.nullable(CodecUtil.<net.minecraft.client.color.block.BlockTintSource,  BlockTintSource>downgrade(BlockTintSourceTypes.CODEC), "tint").forGetter(FluidModel.Unbaked::tintSource)
    ).apply(instance, FluidModel.Unbaked::new));
}
