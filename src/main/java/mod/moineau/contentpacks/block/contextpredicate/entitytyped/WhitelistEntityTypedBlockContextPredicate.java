package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public sealed class WhitelistEntityTypedBlockContextPredicate implements EntityTypedBlockContextPredicate {
    public static final MapCodec<WhitelistEntityTypedBlockContextPredicate> CODEC = Registries.ENTITY_TYPE.getCodec().listOf()
            .xmap(WhitelistEntityTypedBlockContextPredicate::new, WhitelistEntityTypedBlockContextPredicate::list).fieldOf("values");

    private final List<EntityType<?>> list;

    public WhitelistEntityTypedBlockContextPredicate(List<EntityType<?>> list) {
        this.list = list;
    }

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return list().contains(type);
    }

    @Override
    public EntityTypedBlockContextPredicateType<?> getType() {
        return EntityTypedBlockContextPredicateType.WHITELIST;
    }

    public List<EntityType<?>> list() {
        return list;
    }

    @ApiStatus.Internal
    public static final class Lazy extends WhitelistEntityTypedBlockContextPredicate {
        private final Supplier<List<EntityType<?>>> lazyList;
        private boolean loaded;

        public Lazy(Supplier<List<EntityType<?>>> list) {
            super(new ArrayList<>());
            this.lazyList = list;
        }

        @Override
        public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
            return super.test(state, world, pos, type);
        }

        @Override
        public List<EntityType<?>> list() {
            if (!loaded) {
                super.list.addAll(lazyList.get());
                loaded = true;
            }
            return super.list;
        }
    }
}
