package mod.moineau.contentpacks.mixin.client;

import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IntegratedServerLoader.class)
public class IntegratedServerLoaderMixin {
    /**
     * @author Moineau
     * @reason Disable experimental warning
     */
    @Overwrite
    private void showBackupPromptScreen(LevelStorage.Session session, boolean customized, Runnable callback, Runnable onCancel) {
        callback.run();
    }
}
