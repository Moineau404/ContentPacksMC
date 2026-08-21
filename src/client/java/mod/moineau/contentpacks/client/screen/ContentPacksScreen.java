package mod.moineau.contentpacks.client.screen;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.resource.ContentOutput;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;

import java.util.function.Consumer;

public class ContentPacksScreen extends PackSelectionScreen {
    private static Button outputButton;

    public ContentPacksScreen(Consumer<PackRepository> applier) {
        super(ContentPacksClient.getPackRepository(), applier, ContentPacks.PATH, Component.translatable("options.contentpacks.title"));
    }

    public static void setOutputButtonActive(boolean active) {
        outputButton.active = active;
        if (active) {
            outputButton.setTooltip(Tooltip.create(Component.translatable("options.contentpacks.output.tooltip")));
        } else {
            outputButton.setTooltip(Tooltip.create(Component.translatable("options.contentpacks.output.loading.tooltip")));
        }
    }

    @Override
    protected void init() {
        super.init();
        outputButton = createOutputButton();
        setOutputButtonActive(!ContentOutput.isLoading);
    }

    private Button createOutputButton() {
        Button button = new OutputButton(_ -> ContentOutput.output());
        button.setPosition(5, 5);
        this.addRenderableWidget(button);
        return button;
    }
}
