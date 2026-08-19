package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public record WhitelistEntityTypedStatePredicate(List<EntityType<?>> list) implements EntityTypedStatePredicate {
    public static final MapCodec<WhitelistEntityTypedStatePredicate> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf()
            .xmap(WhitelistEntityTypedStatePredicate::new, predicate -> predicate.list).fieldOf("values");

    @Override
    public boolean test(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
        return this.list.contains(type);
    }

    @Override
    public EntityTypedStatePredicateType<?> getType() {
        return EntityTypedStatePredicateType.WHITELIST;
    }
}