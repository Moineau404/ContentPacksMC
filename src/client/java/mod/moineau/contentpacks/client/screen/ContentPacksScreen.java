package mod.moineau.contentpacks.client.screen;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.resource.OutputLoadInstance;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Content packs selection screen.
 */
public final class ContentPacksScreen extends PackSelectionScreen {
    private static ContentPacksScreen instance;
    private OutputButton outputButton;

    public ContentPacksScreen(Consumer<PackRepository> output) {
        super(ContentPacksClient.getPackRepository(), output, ContentPacks.PATH, Component.translatable("options.contentpacks.title"));
        instance = this;
    }

    @Override
    protected void init() {
        super.init();
        outputButton = createOutputButton(OutputLoadInstance.getInstance() == null);
        this.addRenderableWidget(outputButton);
    }

    @Override
    public void onClose() {
        super.onClose();
        instance = null;
    }

    private OutputButton createOutputButton(boolean active) {
        OutputButton button = new OutputButton(b -> {
            b.setActive(false);
            OutputLoadInstance.create(() -> instance.outputButton.setActive(true));
        });
        button.setPosition(5, 5);
        button.setActive(active);
        return button;
    }

    public static @Nullable ContentPacksScreen getInstance() {
        return instance;
    }
}
