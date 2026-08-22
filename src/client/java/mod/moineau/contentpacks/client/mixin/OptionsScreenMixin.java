package mod.moineau.contentpacks.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.screen.ContentPacksScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Invoker("openScreenButton")
    public abstract Button invoke$openScreenButton(final Component message, final Supplier<Screen> screenToScreen);

    @Unique
    private void contentpacks$applyContentPacks(PackRepository packRepository) {
        if (ContentPacksClient.getOptions().refreshPacks(packRepository)) {
            SystemToast.add(
                    this.minecraft.gui.toastManager(),
                    ContentPacksClient.TOAST_CHANGED,
                    Component.translatable("options.contentpacks.toast.changed.title"),
                    Component.translatable("options.contentpacks.toast.changed.description")
            );
        }
        this.minecraft.setScreenAndShow(this);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 9, shift = At.Shift.AFTER))
    public void inject$init(CallbackInfo ci, @Local(name = "helper") GridLayout.RowHelper helper){
        helper.addChild(this.invoke$openScreenButton(Component.translatable("options.contentpacks"), () -> new ContentPacksScreen(this::contentpacks$applyContentPacks)));
    }
}
