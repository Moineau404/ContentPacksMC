package mod.moineau.contentpacks.mixin.block;

import mod.moineau.contentpacks.block.statepredicates.entitytyped.EntityTypedStatePredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_BEDROCK(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 1))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_CHERRY_LEAVES(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.whitelist(() -> List.of(EntityTypes.OCELOT, EntityTypes.PARROT));
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 2))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_PALE_OAK_LEAVES(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.whitelist(() -> List.of(EntityTypes.OCELOT, EntityTypes.PARROT));
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 3))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_GLASS(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 4))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_MOVING_PISTON(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 5))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_ICE(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.whitelist(() -> List.of(EntityTypes.POLAR_BEAR));
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 6))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_SOUL_SAND(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysTrue();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 7))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_CARVED_PUMPKIN(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysTrue();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 8))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_JACK_O_LANTERN(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysTrue();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 9))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_STAINED_GLASS(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 10))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_OAK_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 11))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_SPRUCE_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 12))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_BIRCH_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 13))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_JUNGLE_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 14))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_ACACIA_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 15))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_CHERRY_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 16))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_DARK_OAK_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 17))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_PALE_OAK_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 18))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_MANGROVE_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 19))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_BAMBOO_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 20))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_REDSTONE_LAMP(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysTrue();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 21))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_BARRIER(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 22))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_IRON_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 23))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_CHORUS_FLOWER(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 24))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_FROSTED_ICE(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.whitelist(() -> List.of(EntityTypes.POLAR_BEAR));
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 25))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_MAGMA_BLOCK(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.isFireImmune();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 26))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_SCAFFOLDING(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 27))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_CRIMSON_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 28))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_WARPED_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 29))
    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_TINTED_GLASS(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return EntityTypedStatePredicate.alwaysFalse();
    }

// FIXME : Find why there is an error for ordinals after 29

//    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 30))
//    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_COPPER_TRAPDOOR(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
//        return EntityTypedStatePredicate.alwaysFalse();
//    }
//
//    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 31))
//    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_COPPER_GRATE(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
//        return EntityTypedStatePredicate.alwaysFalse();
//    }
//
//    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;isValidSpawn(Lnet/minecraft/world/level/block/state/BlockBehaviour$StateArgumentPredicate;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 32))
//    private static BlockBehaviour.StateArgumentPredicate<EntityType<?>> inject$isValidSpawn_MUD(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
//        return EntityTypedStatePredicate.alwaysTrue();
//    }
}
