package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.api.util.FunctionUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeCodecs {
    public static final MapCodec<VoxelShape> BOX_INTS = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Vec3i.CODEC.fieldOf("from").forGetter(FunctionUtil::nothing),
            Vec3i.CODEC.fieldOf("to").forGetter(FunctionUtil::nothing)
    ).apply(instance, (v, u) -> Shapes.box(v.getX()/16.0, v.getY()/16.0, v.getZ()/16.0, u.getX()/16.0, u.getY()/16.0, u.getZ()/16.0)));
    public static final MapCodec<VoxelShape> BOX_FOATS = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Vec3.CODEC.fieldOf("from").forGetter(FunctionUtil::nothing),
            Vec3.CODEC.fieldOf("to").forGetter(FunctionUtil::nothing)
    ).apply(instance, (v, u) -> Shapes.box(v.x(), v.y(), v.z(), u.x(), u.y(), u.z())));
    public static final Codec<VoxelShape> BOX = Codec.withAlternative(BOX_INTS.codec(), BOX_FOATS.codec());
    public static final Codec<VoxelShape> CODEC = Codec.withAlternative(BOX, BOX.listOf(2, Integer.MAX_VALUE).flatXmap(
            list -> list.stream().reduce(Shapes::or).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Voxel shape list to join must contains at least 2 elements")),
            FunctionUtil::nothing
    ));
}
