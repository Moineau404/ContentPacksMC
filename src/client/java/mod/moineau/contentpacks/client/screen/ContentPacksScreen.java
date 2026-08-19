package mod.moineau.contentpacks.client.screen;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.client.ContentPacksClient;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;

import java.util.function.Consumer;

public class ContentPacksScreen extends PackSelectionScreen {
    public ContentPacksScreen(Consumer<PackRepository> applier) {
        super(ContentPacksClient.PACK_REPOSITORY, applier, ContentPacks.PATH, Component.translatable("options.contentpacks.title"));
    }

//    @Override
//    public void populateLists(final PackSelectionModel.EntryBase transferredEntry) {
//        super.populateLists(transferredEntry);
//        this.doneButton.active = true;
//    }

//    @Override
//    protected void init() {
//        super.init();
//        CheckboxWidget checkboxWidget = CheckboxWidget.builder(Text.translatable("options.contentpacks.output"), this.textRenderer).checked(ContentPacksClient.OPTIONS.isOutputEnabled())
//                .pos(5, 5).callback(((checkbox, checked) -> ContentPacksClient.OPTIONS.setOutputEnabled(true))).build();
//        this.addDrawableChild(checkboxWidget);
//    }
}
