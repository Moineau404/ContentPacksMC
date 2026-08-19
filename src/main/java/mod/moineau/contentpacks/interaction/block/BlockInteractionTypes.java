package mod.moineau.contentpacks.interaction.block;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;

public final class BlockInteractionTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<Block, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final InteractionType<Block, StrippableBlockInteraction> STRIPPABLE = () -> StrippableBlockInteraction.CODEC;
    public static final InteractionType<Block, FlattenableBlockInteraction> FLATTENABLE = () -> FlattenableBlockInteraction.CODEC;
    public static final InteractionType<Block, FabricStrippableBlockInteraction> FABRIC_STRIPPABLE = () -> FabricStrippableBlockInteraction.CODEC;
    public static final InteractionType<Block, FabricFlattenableBlockInteraction> FABRIC_FLATTENABLE = () -> FabricFlattenableBlockInteraction.CODEC;
    public static final InteractionType<Block, FabricFlammableBlockInteraction> FABRIC_FLAMMABLE = () -> FabricFlammableBlockInteraction.CODEC;

    public static void bootStrap() {
        ID_MAPPER.put(Identifier.fromNamespaceAndPath(ContentPacks.MOD_ID, "strippable"), STRIPPABLE);
        ID_MAPPER.put(Identifier.fromNamespaceAndPath(ContentPacks.MOD_ID, "flattenable"), FLATTENABLE);
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "strippable"), FABRIC_STRIPPABLE);
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "flattenable"), FABRIC_FLATTENABLE);
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "flammable"), FABRIC_FLAMMABLE);
    }
}
