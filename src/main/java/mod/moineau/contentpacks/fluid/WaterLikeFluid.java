package mod.moineau.contentpacks.fluid;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.CollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

// TODO Finish
@ApiStatus.Experimental
public abstract class WaterLikeFluid extends FlowableFluid {
	final Block block;
	final Item bucketItem;
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
			Block block,
			Item bucketItem,
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

	private static <F extends WaterLikeFluid> Products.P13<
			RecordCodecBuilder.Mu<F>,
			Block,
			Item,
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
			Fluid
			> fillFields(RecordCodecBuilder.Instance<F> instance, RecordCodecBuilder<F, Fluid> builder) {
		return instance.group(
				Registries.BLOCK.getCodec().fieldOf("block").forGetter(fluid -> fluid.block),
				Registries.ITEM.getCodec().fieldOf("bucket_item").forGetter(fluid -> fluid.bucketItem),
				Registries.SOUND_EVENT.getCodec().fieldOf("ambient_sound").forGetter(fluid -> fluid.ambientSound),
				Registries.SOUND_EVENT.getCodec().optionalFieldOf("bucket_fill_sound").forGetter(fluid -> fluid.bucketFillSound),
				Registries.PARTICLE_TYPE.getCodec().fieldOf("particle").forGetter(fluid -> fluid.particle),
				Registries.PARTICLE_TYPE.getCodec().fieldOf("underwater_particle").forGetter(fluid -> fluid.underwaterParticle),
				Codec.BOOL.fieldOf("infinite").forGetter(fluid -> fluid.infinite),
				Codec.INT.fieldOf("max_flow_distance").forGetter(fluid -> fluid.maxFlowDistance),
				Codec.INT.fieldOf("level_decrease_per_block").forGetter(fluid -> fluid.levelDecreasePerBlock),
				Codec.INT.fieldOf("tick_rate").forGetter(fluid -> fluid.tickRate),
				TagKey.codec(RegistryKeys.FLUID).fieldOf("tag").forGetter(fluid -> fluid.tag),
				Codec.FLOAT.fieldOf("blast_resistance").forGetter(fluid -> fluid.blastResistance),
				builder
		);
	}

	@Override
	public Item getBucketItem() {
		return bucketItem;
	}

	// TODO
	@Override
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		if (!state.isStill() && !(Boolean)state.get(FALLING)) {
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

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}

	// TODO ?
	@Override
	protected void onEntityCollision(World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		handler.addEvent(CollisionEvent.EXTINGUISH);
	}

	@Override
	public int getMaxFlowDistance(WorldView world) {
		return maxFlowDistance;
	}

	@Override
	public BlockState toBlockState(FluidState state) {
		return block.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
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
		private final Fluid still;

		public static final Codec<Flowing> CODEC = RecordCodecBuilder.create(instance -> WaterLikeFluid.fillFields(instance,
				Registries.FLUID.getCodec().fieldOf("still").forGetter(flowing -> flowing.still)
		).apply(instance, Flowing::new));

		protected Flowing(
				Block block,
				Item bucketItem,
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
				Fluid still
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
		}

		@Override
		public Fluid getFlowing() {
			return this;
		}

		@Override
		public Fluid getStill() {
			return still;
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
		public boolean matchesType(Fluid fluid) {
			return fluid == still || fluid == this;
		}
	}

	public static class Still extends WaterLikeFluid {
		private final Fluid flowing;

		public static final Codec<Still> CODEC = RecordCodecBuilder.create(instance -> WaterLikeFluid.fillFields(instance,
				Registries.FLUID.getCodec().fieldOf("flowing").forGetter(still -> still.flowing)
		).apply(instance, Still::new));

		protected Still(
				Block block,
				Item bucketItem,
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
				Fluid flowing
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
			this.flowing = flowing;
		}

		@Override
		public Fluid getFlowing() {
			return flowing;
		}

		@Override
		public Fluid getStill() {
			return this;
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
		public boolean matchesType(Fluid fluid) {
			return fluid == this || fluid == flowing;
		}
	}
}
