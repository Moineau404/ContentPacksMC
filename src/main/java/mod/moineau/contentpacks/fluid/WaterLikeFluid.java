package mod.moineau.contentpacks.fluid;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.CodecUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

// TODO Finish
@ApiStatus.Experimental
public abstract class WaterLikeFluid extends WaterFluid implements ContentFluid {
	protected Supplier<Fluid> still;
	protected Supplier<Fluid> flowing;
	final Supplier<Block> block;
	final Supplier<Item> bucketItem;
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
			float blastResistance
	) {
		this.block = block;
		this.bucketItem = bucketItem;
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
				CodecUtil.lazy(Registries.BLOCK).fieldOf("block").forGetter(fluid -> fluid.block),
				CodecUtil.lazy(Registries.ITEM).fieldOf("bucket_item").forGetter(fluid -> fluid.bucketItem),
				Registries.SOUND_EVENT.getCodec().optionalFieldOf("ambient_sound", SoundEvents.BLOCK_WATER_AMBIENT).forGetter(fluid -> fluid.ambientSound),
				CodecUtil.intentionallyOptional(Registries.SOUND_EVENT.getCodec()).optionalFieldOf("bucket_fill_sound", Optional.of(SoundEvents.ITEM_BUCKET_FILL)).forGetter(fluid -> fluid.bucketFillSound),
				Registries.PARTICLE_TYPE.getCodec().fieldOf("particle").forGetter(fluid -> fluid.particle),
				Registries.PARTICLE_TYPE.getCodec().fieldOf("underwater_particle").forGetter(fluid -> fluid.underwaterParticle),
				// TODO Create a predicate for #isInfinite(ServerWorld)
				Codec.BOOL.optionalFieldOf("infinite", true).forGetter(fluid -> fluid.infinite),
				Codecs.NON_NEGATIVE_INT.optionalFieldOf("max_flow_distance", 4).forGetter(fluid -> fluid.maxFlowDistance),
				Codecs.NON_NEGATIVE_INT.optionalFieldOf("level_decrease_per_block", 1).forGetter(fluid -> fluid.levelDecreasePerBlock),
				Codecs.NON_NEGATIVE_INT.optionalFieldOf("tick_rate", 5).forGetter(fluid -> fluid.tickRate),
				TagKey.unprefixedCodec(RegistryKeys.FLUID).fieldOf("tag").forGetter(fluid -> fluid.tag),
				Codecs.NON_NEGATIVE_FLOAT.optionalFieldOf("blast_resistance", 100.0F).forGetter(fluid -> fluid.blastResistance),
				builder
		);
	}

	@Override
	public Fluid getFlowing() {
		return flowing.get();
	}

	@Override
	public Fluid getStill() {
		return still.get();
	}

	@Override
	public Item getBucketItem() {
		return bucketItem.get();
	}

	// TODO
	@Override
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		if (!state.isStill() && !state.get(FALLING)) {
			if (random.nextInt(64) == 0) {
				world.playSoundClient(
						pos.getX() + 0.5,
						pos.getY() + 0.5,
						pos.getZ() + 0.5,
						ambientSound,
						SoundCategory.AMBIENT,
						random.nextFloat() * 0.25F + 0.75F,
						random.nextFloat() + 0.5F,
						false
				);
			}
		} else if (random.nextInt(10) == 0) {
			world.addParticleClient(
					(ParticleEffect) underwaterParticle, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0
			);
		}
	}

	@Nullable
	@Override
	public ParticleEffect getParticle() {
		return (ParticleEffect) particle;
	}

	@Override
	protected boolean isInfinite(ServerWorld world) {
		return infinite;
	}

	// TODO : #onEntityCollision

	@Override
	public int getMaxFlowDistance(WorldView world) {
		return maxFlowDistance;
	}

	@Override
	public BlockState toBlockState(FluidState state) {
		return block.get().getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == getStill() || fluid == getFlowing();
	}

	@Override
	public int getLevelDecreasePerBlock(WorldView world) {
		return levelDecreasePerBlock;
	}

	@Override
	public int getTickRate(WorldView world) {
		return tickRate;
	}

	@Override
	public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
		return direction == Direction.DOWN && !fluid.isIn(tag);
	}

	@Override
	protected float getBlastResistance() {
		return blastResistance;
	}

	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return bucketFillSound;
	}

	public static class Flowing extends WaterLikeFluid {
		public static final MapCodec<WaterLikeFluid.Flowing> CODEC = RecordCodecBuilder.mapCodec(instance -> fillFields(instance,
				CodecUtil.lazy(Registries.FLUID).fieldOf("still").forGetter(flowing -> flowing.still)
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
			this.still = still;
			this.flowing = () -> this;
		}

		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState state) {
			return state.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState state) {
			return false;
		}

		@Override
		public MapCodec<? extends ContentFluid> getCodec() {
			return CODEC;
		}
	}

	public static class Still extends WaterLikeFluid {
		public static final MapCodec<WaterLikeFluid.Still> CODEC = RecordCodecBuilder.mapCodec(instance -> fillFields(instance,
				CodecUtil.lazy(Registries.FLUID).fieldOf("flowing").forGetter(still -> still.flowing)
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
			this.still = () -> this;
			this.flowing = flowing;
		}

		@Override
		public int getLevel(FluidState state) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState state) {
			return true;
		}

		@Override
		public MapCodec<? extends ContentFluid> getCodec() {
			return CODEC;
		}
	}
}
