package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.math.Comparison;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public final class LightEmissionStatePredicate extends OffsetContextPredicate {
    public static final MapCodec<LightEmissionStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> registerOffsetField(instance).and(
                    Comparison.createCodec(Codec.INT).fieldOf("predicate").forGetter(predicate -> predicate.comparison)
            ).apply(instance, LightEmissionStatePredicate::new)
    );

    private final Comparison<Integer> comparison;

    public LightEmissionStatePredicate(Vec3i vec3i, Comparison<Integer> comparison) {
        super(vec3i);
        this.comparison = comparison;
    }

    @Override
    public boolean test(BlockState state) {
        return comparison.test(state.getLightEmission());
    }

    @Override
    public StatePredicateType<?> getType() {
        return StatePredicateType.LIGHT_EMISSION;
    }
}
