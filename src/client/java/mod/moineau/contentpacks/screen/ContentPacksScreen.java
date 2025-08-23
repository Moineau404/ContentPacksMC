package mod.moineau.contentpacks.screen;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.ContentPacksClient;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ContentPacksScreen extends PackScreen {
    public ContentPacksScreen(Consumer<ResourcePackManager> applier) {
        super(ContentPacksClient.PACK_MANAGER, applier, ContentPacks.PATH, Text.translatable("options.contentpacks.title"));
    }

    @Override
    public void updatePackLists() {
        super.updatePackLists();
        this.doneButton.active = true;
    }

//    @Override
//    protected void init() {
//        super.init();
//        CheckboxWidget checkboxWidget = CheckboxWidget.builder(Text.translatable("options.contentpacks.output"), this.textRenderer).checked(ContentPacksClient.OPTIONS.isOutputEnabled())
//                .pos(5, 5).callback(((checkbox, checked) -> ContentPacksClient.OPTIONS.setOutputEnabled(true))).build();
//        this.addDrawableChild(checkboxWidget);
//    }
}
