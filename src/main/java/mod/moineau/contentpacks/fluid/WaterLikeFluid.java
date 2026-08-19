package mod.moineau.contentpacks.fluid;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.CodecUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

// TODO Finish
@ApiStatus.Experimental
public abstract class WaterLikeFluid extends WaterFluid implements ContentFluid {
	protected Supplier<Fluid> source;
	protected Supplier<Fluid> flowing;
	final Supplier<Block> block;
	final Supplier<Item> bucket;
	final SoundEvent ambientSound;
	final Optional<SoundEvent> bucketFillSound;
	final ParticleType<?> particle;
	final ParticleType<?> underwaterParticle;
	final boolean infinite;
	final int maxFlowDistance;
	final int levelDecreasePerBlock;
	final int tickRate;
	final TagKey<Fluid> tag;
	final float blastResistance;

	protected WaterLikeFluid(
			Supplier<Block> block,
			Supplier<Item> bucket,
			SoundEvent ambientSound,
			Optional<SoundEvent> bucketFillSound,
			ParticleType<?> particle,
			ParticleType<?> underwaterParticle,
			boolean infinite,
			int maxFlowDistance,
			int levelDecreasePerBlock,
			int tickRate,
			TagKey<Fluid> tag,
			float blastResistance
	) {
		this.block = block;
		this.bucket = bucket;
		this.ambientSound = ambientSound;
		this.bucketFillSound = bucketFillSound;
		this.particle = particle;
		this.underwaterParticle = underwaterParticle;
		this.infinite = infinite;
		this.maxFlowDistance = maxFlowDistance;
		this.levelDecreasePerBlock = levelDecreasePerBlock;
		this.tickRate = tickRate;
		this.tag = tag;
		this.blastResistance = blastResistance;
	}

	protected static <F extends WaterLikeFluid> Products.P13<
			RecordCodecBuilder.Mu<F>,
			Supplier<Block>,
			Supplier<Item>,
			SoundEvent,
			Optional<SoundEvent>,
			ParticleType<?>,
			ParticleType<?>,
			Boolean,
			Integer,
			Integer,
			Integer,
			TagKey<Fluid>,
			Float,
			Supplier<Fluid>
			> fillFields(RecordCodecBuilder.Instance<F> instance, RecordCodecBuilder<F, Supplier<Fluid>> builder) {
		return instance.group(
				CodecUtil.lazy(BuiltInRegistries.BLOCK).fieldOf("block").forGetter(fluid -> fluid.block),
				CodecUtil.lazy(BuiltInRegistries.ITEM).fieldOf("bucket").forGetter(fluid -> fluid.bucket),
				BuiltInRegistries.SOUND_EVENT.byNameCodec().optionalFieldOf("ambient_sound", SoundEvents.WATER_AMBIENT).forGetter(fluid -> fluid.ambientSound),
				CodecUtil.intentionallyOptional(BuiltInRegistries.SOUND_EVENT.byNameCodec()).optionalFieldOf("bucket_fill_sound", Optional.of(SoundEvents.BUCKET_FILL)).forGetter(fluid -> fluid.bucketFillSound),
				BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("particle").forGetter(fluid -> fluid.particle),
				BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("underwater_particle").forGetter(fluid -> fluid.underwaterParticle),
				// TODO Create a predicate for #isInfinite(ServerWorld)
				Codec.BOOL.optionalFieldOf("infinite", true).forGetter(fluid -> fluid.infinite),
				ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_flow_distance", 4).forGetter(fluid -> fluid.maxFlowDistance),
				ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("level_decrease_per_block", 1).forGetter(fluid -> fluid.levelDecreasePerBlock),
				ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("tick_rate", 5).forGetter(fluid -> fluid.tickRate),
				TagKey.codec(Registries.FLUID).fieldOf("tag").forGetter(fluid -> fluid.tag),
				ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("blast_resistance", 100.0F).forGetter(fluid -> fluid.blastResistance),
				builder
		);
	}

	@Override
	public Fluid getFlowing() {
		return flowing.get();
	}

	@Override
	public Fluid getSource() {
		return source.get();
	}

	@Override
	public Item getBucket() {
		return bucket.get();
	}

	// TODO
	@Override
	public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource random) {
		if (!state.isSource() && !state.getValue(FALLING)) {
			if (random.nextInt(64) == 0) {
				world.playLocalSound(
						pos.getX() + 0.5,
						pos.getY() + 0.5,
						pos.getZ() + 0.5,
						ambientSound,
						SoundSource.AMBIENT,
						random.nextFloat() * 0.25F + 0.75F,
						random.nextFloat() + 0.5F,
						false
				);
			}
		} else if (random.nextInt(10) == 0) {
			world.addParticle(
					(ParticleOptions) underwaterParticle, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0
			);
		}
	}

	@Nullable
	@Override
	public ParticleOptions getDripParticle() {
		return (ParticleOptions) particle;
	}

	@Override
	protected boolean canConvertToSource(ServerLevel world) {
		return infinite;
	}

	// TODO : #onEntityCollision

	@Override
	public int getSlopeFindDistance(LevelReader world) {
		return maxFlowDistance;
	}

	@Override
	public BlockState createLegacyBlock(FluidState state) {
		return block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
	}

	@Override
	public boolean isSame(Fluid fluid) {
		return fluid == getSource() || fluid == getFlowing();
	}

	@Override
	public int getDropOff(LevelReader world) {
		return levelDecreasePerBlock;
	}

	@Override
	public int getTickDelay(LevelReader world) {
		return tickRate;
	}

	@Override
	public boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
		return direction == Direction.DOWN && !fluid.is(tag);
	}

	@Override
	protected float getExplosionResistance() {
		return blastResistance;
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return bucketFillSound;
	}

	public static class Flowing extends WaterLikeFluid {
		public static final MapCodec<WaterLikeFluid.Flowing> CODEC = RecordCodecBuilder.mapCodec(instance -> fillFields(instance,
				CodecUtil.lazy(BuiltInRegistries.FLUID).fieldOf("still").forGetter(flowing -> flowing.source)
		).apply(instance, WaterLikeFluid.Flowing::new));

		protected Flowing(
				Supplier<Block> block,
				Supplier<Item> bucketItem,
				SoundEvent ambientSound,
				Optional<SoundEvent> bucketFillSound,
				ParticleType<?> particle,
				ParticleType<?> underwaterParticle,
				boolean infinite,
				int maxFlowDistance,
				int levelDecreasePerBlock,
				int tickRate,
				TagKey<Fluid> tag,
				float blastResistance,
				Supplier<Fluid> still
		) {
			super(
					block,
					bucketItem,
					ambientSound,
					bucketFillSound,
					particle,
					underwaterParticle,
					infinite,
					maxFlowDistance,
					levelDecreasePerBlock,
					tickRate,
					tag,
					blastResistance
			);
			this.source = still;
			this.flowing = () -> this;
		}

		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		@Override
		public boolean isSource(FluidState state) {
			return false;
		}

		@Override
		public MapCodec<? extends ContentFluid> getCodec() {
			return CODEC;
		}
	}

	public static class Still extends WaterLikeFluid {
		public static final MapCodec<WaterLikeFluid.Still> CODEC = RecordCodecBuilder.mapCodec(instance -> fillFields(instance,
				CodecUtil.lazy(BuiltInRegistries.FLUID).fieldOf("flowing").forGetter(still -> still.flowing)
		).apply(instance, WaterLikeFluid.Still::new));

		protected Still(
				Supplier<Block> block,
				Supplier<Item> bucketItem,
				SoundEvent ambientSound,
				Optional<SoundEvent> bucketFillSound,
				ParticleType<?> particle,
				ParticleType<?> underwaterParticle,
				boolean infinite,
				int maxFlowDistance,
				int levelDecreasePerBlock,
				int tickRate,
				TagKey<Fluid> tag,
				float blastResistance,
				Supplier<Fluid> flowing
		) {
			super(
					block,
					bucketItem,
					ambientSound,
					bucketFillSound,
					particle,
					underwaterParticle,
					infinite,
					maxFlowDistance,
					levelDecreasePerBlock,
					tickRate,
					tag,
					blastResistance
			);
			this.source = () -> this;
			this.flowing = flowing;
		}

		@Override
		public int getAmount(FluidState state) {
			return 8;
		}

		@Override
		public boolean isSource(FluidState state) {
			return true;
		}

		@Override
		public MapCodec<? extends ContentFluid> getCodec() {
			return CODEC;
		}
	}
}
