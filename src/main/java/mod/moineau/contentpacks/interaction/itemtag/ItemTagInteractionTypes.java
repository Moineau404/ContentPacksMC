package mod.moineau.contentpacks.interaction.itemtag;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class ItemTagInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<TagKey<Item>, ?>> ID_MAPPER = new Codecs.IdMapper<>();
    @Deprecated
    public static final Codec<Interaction<TagKey<Item>>> CODEC = ID_MAPPER.getCodec(Identifier.CODEC).dispatch(Interaction::getType, InteractionType::codec);

    public static final InteractionType<TagKey<Item>, FabricFuelItemTagInteraction> FABRIC_FUEL = () -> FabricFuelItemTagInteraction.CODEC;
    public static final InteractionType<TagKey<Item>, FabricCompostingChanceItemTagInteraction> FABRIC_COMPOSTING_CHANCE = () -> FabricCompostingChanceItemTagInteraction.CODEC;

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.of("fabric", "fuel"), FABRIC_FUEL);
        ID_MAPPER.put(Identifier.of("fabric", "composting_chance"), FABRIC_COMPOSTING_CHANCE);
    }
}
