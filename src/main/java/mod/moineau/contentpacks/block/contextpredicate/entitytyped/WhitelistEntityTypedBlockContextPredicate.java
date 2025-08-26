package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;

public record WhitelistEntityTypedBlockContextPredicate(List<EntityType<?>> list) implements EntityTypedBlockContextPredicate {
    public static final MapCodec<WhitelistEntityTypedBlockContextPredicate> CODEC = Registries.ENTITY_TYPE.getCodec().listOf()
            .xmap(WhitelistEntityTypedBlockContextPredicate::new, predicate -> predicate.list).fieldOf("values");

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return this.list.contains(type);
    }

    @Override
    public EntityTypedBlockContextPredicateType<?> getType() {
        return EntityTypedBlockContextPredicateType.WHITELIST;
    }
}