package mod.moineau.contentpacks.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.ContentPacksClient;
import mod.moineau.contentpacks.screen.ContentPacksScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Invoker("createButton")
    public abstract ButtonWidget invoke$createButton(Text message, Supplier<Screen> screenSupplier);

    @SuppressWarnings("DataFlowIssue")
    @Unique
    private void contentpacks$refreshContentPacks(ResourcePackManager resourcePackManager) {
//        if (ContentPacksClient.OPTIONS.refreshPacks(resourcePackManager)) {
//            this.client.setScreen(new PopupScreen.Builder(this, Text.translatable("options.contentpacks.changed.title"))
//                            .message(Text.translatable("options.contentpacks.changed.description"))
//                    .button(Text.translatable("gui.ok"), PopupScreen::close).build());
//        } else {
//            this.client.setScreen(this);
//        }
        if (ContentPacksClient.OPTIONS.refreshPacks(resourcePackManager)) {
            this.client.getToastManager().add(SystemToast.create(this.client, ContentPacksClient.CONTENT_CHANGED,
                    Text.translatable("options.contentpacks.changed.title"), Text.translatable("options.contentpacks.changed.description")));
        }
        this.client.setScreen(this);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 9, shift = At.Shift.AFTER))
    public void inject$init(CallbackInfo callbackInfo, @Local GridWidget.Adder adder){
        adder.add(this.invoke$createButton(Text.translatable("options.contentpacks"), () -> new ContentPacksScreen(this::contentpacks$refreshContentPacks)));
    }
}
