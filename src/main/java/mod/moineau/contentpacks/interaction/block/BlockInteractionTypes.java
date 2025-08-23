package mod.moineau.contentpacks.interaction.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class BlockInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<Block, ?>> ID_MAPPER = new Codecs.IdMapper<>();
    @Deprecated
    public static final Codec<Interaction<Block>> CODEC = ID_MAPPER.getCodec(Identifier.CODEC).dispatch(Interaction::getType, InteractionType::codec);

    public static final InteractionType<Block, StrippableBlockInteraction> STRIPPABLE = () -> StrippableBlockInteraction.CODEC;
    public static final InteractionType<Block, FlattenableBlockInteraction> FLATTENABLE = () -> FlattenableBlockInteraction.CODEC;
    public static final InteractionType<Block, FabricStrippableBlockInteraction> FABRIC_STRIPPABLE = () -> FabricStrippableBlockInteraction.CODEC;
    public static final InteractionType<Block, FabricFlattenableBlockInteraction> FABRIC_FLATTENABLE = () -> FabricFlattenableBlockInteraction.CODEC;
    public static final InteractionType<Block, FabricFlammableBlockInteraction> FABRIC_FLAMMABLE = () -> FabricFlammableBlockInteraction.CODEC;

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.of(ContentPacks.MOD_ID, "strippable"), STRIPPABLE);
        ID_MAPPER.put(Identifier.of(ContentPacks.MOD_ID, "flattenable"), FLATTENABLE);
        ID_MAPPER.put(Identifier.of("fabric", "strippable"), FABRIC_STRIPPABLE);
        ID_MAPPER.put(Identifier.of("fabric", "flattenable"), FABRIC_FLATTENABLE);
        ID_MAPPER.put(Identifier.of("fabric", "flammable"), FABRIC_FLAMMABLE);
    }
}
