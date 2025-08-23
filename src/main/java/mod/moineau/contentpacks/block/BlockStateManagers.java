package mod.moineau.contentpacks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Copied from net.minecraft.client.render.model.BlockStateManagers
 */
@ApiStatus.Internal
@Deprecated
public final class BlockStateManagers {
    private static Map<Identifier, StateManager<Block, BlockState>> MANAGERS;

    private static Function<Identifier, StateManager<Block, BlockState>> getIdToManagerMapper() {
        if (MANAGERS == null) {
            MANAGERS = new HashMap<>();

            for (Block block : Registries.BLOCK) {
                MANAGERS.put(block.getRegistryEntry().registryKey().getValue(), block.getStateManager());
            }
        }

        return MANAGERS::get;
    }
}
