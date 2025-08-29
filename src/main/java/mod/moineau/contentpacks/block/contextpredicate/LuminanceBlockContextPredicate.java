package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.math.Comparison;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Vec3i;

public final class LuminanceBlockContextPredicate extends OffsetContextPredicate {
    public static final MapCodec<LuminanceBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> registerOffsetField(instance).and(
                    Comparison.createCodec(Codec.INT).fieldOf("predicate").forGetter(predicate -> predicate.comparison)
            ).apply(instance, LuminanceBlockContextPredicate::new)
    );

    private final Comparison<Integer> comparison;

    public LuminanceBlockContextPredicate(Vec3i vec3i, Comparison<Integer> comparison) {
        super(vec3i);
        this.comparison = comparison;
    }

    @Override
    public boolean test(BlockState state) {
        return comparison.test(state.getLuminance());
    }

    @Override
    public BlockContextPredicateType<?> getType() {
        return BlockContextPredicateType.LUMINANCE;
    }
}
