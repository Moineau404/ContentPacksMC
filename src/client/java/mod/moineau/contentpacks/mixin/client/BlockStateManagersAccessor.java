package mod.moineau.contentpacks.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockStateManagers;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(BlockStateManagers.class)
public interface BlockStateManagersAccessor {
    @Invoker("createIdToManagerMapper")
    static Function<Identifier, StateManager<Block, BlockState>> createIdToManagerMapper() {
        throw new AssertionError();
    }
}
