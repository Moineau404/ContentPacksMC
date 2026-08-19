package mod.moineau.contentpacks.client.screen;

import mod.moineau.contentpacks.ContentPacks;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class OutputButton extends SpriteIconButton.CenteredIcon {
    private static final Component TOOLTIP = Component.translatable("options.contentpacks.output.tooltip");
    private static final Component MESSAGE = Component.translatable("options.contentpacks.output");

    public OutputButton(final Button.OnPress onPress) {
        super(
                20,
                20,
                MESSAGE,
                16,
                16,
                0,
                0,
                new WidgetSprites(Identifier.fromNamespaceAndPath(ContentPacks.MOD_ID, "content/output")),
                onPress,
                TOOLTIP,
                null,
                false
        );
    }
}
