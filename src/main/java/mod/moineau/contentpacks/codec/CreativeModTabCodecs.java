package mod.moineau.contentpacks.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.List;
import java.util.Optional;

public final class CreativeModTabCodecs {
    public static final Codec<CreativeModeTab> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("title").forGetter(CreativeModeTab::getDisplayName),
            ItemStackTemplate.CODEC.optionalFieldOf("icon").forGetter(tab -> {
                ItemStack icon = tab.getIconItem();
                return icon != ItemStack.EMPTY ? Optional.of(ItemStackTemplate.fromStack(icon)) : Optional.empty();
            }),
            ItemStackTemplate.CODEC.listOf().optionalFieldOf("display_items", List.of()).forGetter(tab -> tab.getDisplayItems().stream().map(ItemStackTemplate::fromNonEmptyStack).toList()),
            Codec.BOOL.optionalFieldOf("special", false).forGetter(CreativeModeTab::isAlignedRight),
            Codec.BOOL.optionalFieldOf("show_title", true).forGetter(CreativeModeTab::showTitle),
            Codec.BOOL.optionalFieldOf("scroll_bar", true).forGetter(CreativeModeTab::canScroll),
            Identifier.CODEC.optionalFieldOf("background_texture", CreativeModeTab.createTextureLocation("items")).forGetter(CreativeModeTab::getBackgroundTexture)
    ).apply(instance, CreativeModTabCodecs::create));

    private static CreativeModeTab create(
            Component displayName,
            Optional<ItemStackTemplate> icon,
            List<ItemStackTemplate> entries,
            boolean special,
            boolean renderName,
            boolean scrollbar,
            Identifier texture
    ) {
        CreativeModeTab.Builder builder = FabricCreativeModeTab.builder();
        builder.title(displayName);
        builder.icon(() -> icon.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY));
        builder.displayItems(((displayContext, entries1) -> entries1.acceptAll(entries.stream().map(ItemStackTemplate::create).toList())));
        if (special) builder.alignedRight();
        if (!renderName) builder.hideTitle();
        if (!scrollbar) builder.noScrollBar();
        builder.backgroundTexture(texture);
        return builder.build();
    }
}
