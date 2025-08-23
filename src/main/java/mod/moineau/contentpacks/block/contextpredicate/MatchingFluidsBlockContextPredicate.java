package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.Vec3i;

public class MatchingFluidsBlockContextPredicate extends OffsetContextPredicate {
	private final RegistryEntryList<Fluid> fluids;
	public static final MapCodec<MatchingFluidsBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance)
			.and(RegistryCodecs.entryList(RegistryKeys.FLUID).fieldOf("fluids").forGetter(predicate -> predicate.fluids))
			.apply(instance, MatchingFluidsBlockContextPredicate::new)
	);

	public MatchingFluidsBlockContextPredicate(Vec3i offset, RegistryEntryList<Fluid> fluids) {
		super(offset);
		this.fluids = fluids;
	}

	@Override
	protected boolean test(BlockState state) {
		return state.getFluidState().isIn(this.fluids);
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.MATCHING_FLUIDS;
	}
}
