package mod.moineau.contentpacks.mixin.client;

import mod.moineau.contentpacks.render.entity.SignRendering;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.gui.screen.ingame.HangingSignEditScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(HangingSignEditScreen.class)
public abstract class HangingSignEditScreenMixin extends AbstractSignEditScreen {
    @Mutable
    @Shadow
    @Final
    private Identifier texture;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void inject$init(SignBlockEntity signBlockEntity, boolean bl, boolean bl2, CallbackInfo ci) {
        this.texture = Objects.requireNonNullElse(SignRendering.getHangingSignEditScreenTextureId(this.signType), this.texture);
    }

    public HangingSignEditScreenMixin(SignBlockEntity blockEntity, boolean front, boolean filtered) {
        super(blockEntity, front, filtered);
    }
}
