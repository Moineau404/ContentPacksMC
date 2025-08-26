package mod.moineau.contentpacks.block;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;
import mod.moineau.contentpacks.api.util.Workaround;
import net.minecraft.state.StateManager;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Workaround
public interface Bakeable {
    DataResult<?> EMPTY_RESULT = DataResult.success(Unit.INSTANCE);

    default @Nullable DataResult<?> contentpacks$bake(StateManager<?, ?> stateManager) {
        return null;
    }

    static @Nullable DataResult<?> bake(Object object, StateManager<?, ?> stateManager) {
        if (object instanceof Bakeable bakeable) {
            return bakeable.contentpacks$bake(stateManager);
        }
        return null;
    }

    static DataResult<?> bake(DataResult<?> result, StateManager<?, ?> stateManager, Object... objects) {
        DataResult<?> finalResult = Objects.requireNonNullElseGet(result, () -> DataResult.success(Unit.INSTANCE));
        for (Object object : objects) {
            DataResult<?> bakingResult = bake(object, stateManager);
            if (bakingResult != null) {
                finalResult = finalResult.apply2stable((o1, o2) -> o1, bakingResult);
            }
        }
        return finalResult;
    }

    static DataResult<?> bake(StateManager<?, ?> stateManager, Object... objects) {
        return bake(DataResult.success(Unit.INSTANCE), stateManager, objects);
    }
}
