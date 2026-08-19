package mod.moineau.contentpacks.client.mixin;

import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WorldOpenFlows.class)
public class WorldOpenFlowsMixin {
    /**
     * @author Moineau
     * @reason Disable experimental warning
     */
    @Overwrite
    private void askForBackup(LevelStorageSource.LevelStorageAccess levelAccess, boolean oldCustomized, Runnable proceedCallback, Runnable cancelCallback) {
        proceedCallback.run();
    }
}
