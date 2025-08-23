package mod.moineau.contentpacks.codec;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import java.util.List;

public final class ItemGroupCodecs {
    public static final Codec<ItemGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TextCodecs.CODEC.fieldOf("title").forGetter(ItemGroup::getDisplayName),
            ItemStack.REGISTRY_ENTRY_CODEC.optionalFieldOf("icon", ItemStack.EMPTY).forGetter(ItemGroup::getIcon),
            ItemStack.UNCOUNTED_CODEC.listOf().optionalFieldOf("display_items", List.of()).forGetter(itemGroup -> List.copyOf(itemGroup.getDisplayStacks())),
            Codec.BOOL.optionalFieldOf("special", false).forGetter(ItemGroup::isSpecial),
            Codec.BOOL.optionalFieldOf("show_title", true).forGetter(ItemGroup::shouldRenderName),
            Codec.BOOL.optionalFieldOf("scroll_bar", true).forGetter(ItemGroup::hasScrollbar),
            Identifier.CODEC.optionalFieldOf("background_texture", ItemGroup.getTabTextureId("items")).forGetter(ItemGroup::getTexture)
    ).apply(instance, ItemGroupCodecs::create));

    private static ItemGroup create(
            Text displayName,
            ItemStack icon,
            List<ItemStack> entries,
            boolean special,
            boolean renderName,
            boolean scrollbar,
            Identifier texture
    ) {
        ItemGroup.Builder builder = FabricItemGroup.builder();
        builder.displayName(displayName);
        builder.icon(Suppliers.ofInstance(icon));
        builder.entries(((displayContext, entries1) -> entries1.addAll(entries)));
        if (special) builder.special();
        if (!renderName) builder.noRenderedName();
        if (!scrollbar) builder.noScrollbar();
        builder.texture(texture);
        return builder.build();
    }
}
