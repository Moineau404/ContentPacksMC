package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class MatchingFluidsStatePredicate extends OffsetContextPredicate {
	private final HolderSet<Fluid> fluids;
	public static final MapCodec<MatchingFluidsStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance)
			.and(RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(predicate -> predicate.fluids))
			.apply(instance, MatchingFluidsStatePredicate::new)
	);

	public MatchingFluidsStatePredicate(Vec3i offset, HolderSet<Fluid> fluids) {
		super(offset);
		this.fluids = fluids;
	}

	@Override
	protected boolean test(BlockState state) {
		return state.getFluidState().is(this.fluids);
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.MATCHING_FLUIDS;
	}
}
