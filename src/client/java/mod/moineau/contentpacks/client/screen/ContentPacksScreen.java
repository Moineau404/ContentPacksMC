package mod.moineau.contentpacks.client.screen;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.ContentPacksClient;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;

import java.util.function.Consumer;

public class ContentPacksScreen extends PackSelectionScreen {
    public ContentPacksScreen(Consumer<PackRepository> applier) {
        super(ContentPacksClient.PACK_REPOSITORY, applier, ContentPacks.PATH, Component.translatable("options.contentpacks.title"));
    }

    @Override
    protected void init() {
        super.init();
        Checkbox checkbox = Checkbox.builder(Component.translatable("options.contentpacks.output"), this.font).selected(ContentPacksClient.OPTIONS.isOutputEnabled())
                .pos(5, 5).onValueChange(((_, checked) -> ContentPacksClient.OPTIONS.setOutputEnabled(checked))).build();
        checkbox.setTooltip(Tooltip.create(Component.translatable("options.contentpacks.output.tooltip")));
        checkbox.setAlpha(0.5f);
        this.addRenderableWidget(checkbox);
    }
}
