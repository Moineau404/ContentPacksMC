package mod.moineau.contentpacks.mixin.block;

import mod.moineau.contentpacks.block.contextpredicate.entitytyped.EntityTypedBlockContextPredicate;
import mod.moineau.contentpacks.event.ContentPacksEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 0))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_BEDROCK(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 1))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_CHERRY_LEAVES(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.whitelist(() -> List.of(EntityType.OCELOT, EntityType.PARROT), ContentPacksEvents.REGISTRIES_LOADED, runnable -> runnable::run);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 2))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_PALE_OAK_LEAVES(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.whitelist(() -> List.of(EntityType.OCELOT, EntityType.PARROT), ContentPacksEvents.REGISTRIES_LOADED, runnable -> runnable::run);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 3))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_GLASS(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 4))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_ICE(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.whitelist(() -> List.of(EntityType.POLAR_BEAR), ContentPacksEvents.REGISTRIES_LOADED, runnable -> runnable::run);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 5))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_SOUL_SAND(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysTrue();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 6))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_CARVED_PUMPKIN(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysTrue();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 7))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_JACK_O_LANTERN(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysTrue();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 8))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_OAK_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 9))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_SPRUCE_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 10))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_BIRCH_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 11))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_JUNGLE_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 12))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_ACACIA_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 13))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_CHERRY_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 14))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_DARK_OAK_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 15))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_PALE_OAK_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 16))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_MANGROVE_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 17))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_BAMBOO_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 18))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_REDSTONE_LAMP(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysTrue();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 19))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_BARRIER(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 20))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_IRON_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 21))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_CHORUS_FLOWER(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 22))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_FROSTED_ICE(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.whitelist(() -> List.of(EntityType.POLAR_BEAR), ContentPacksEvents.REGISTRIES_LOADED, runnable -> runnable::run);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 23))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_MAGMA_BLOCK(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.isFireImmune();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 24))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_SCAFFOLDING(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 25))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_CRIMSON_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 26))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_WARPED_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 27))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_TINTED_GLASS(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 28))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_COPPER_TRAPDOOR(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 29))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_COPPER_GRATE(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysFalse();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;allowsSpawning(Lnet/minecraft/block/AbstractBlock$TypedContextPredicate;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 30))
    private static AbstractBlock.TypedContextPredicate<EntityType<?>> inject$allowsSpawning_MUD(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return EntityTypedBlockContextPredicate.alwaysTrue();
    }




}
