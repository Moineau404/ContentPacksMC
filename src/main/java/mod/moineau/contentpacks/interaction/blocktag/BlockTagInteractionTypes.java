package mod.moineau.contentpacks.interaction.blocktag;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;

public final class BlockTagInteractionTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<TagKey<Block>, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final InteractionType<TagKey<Block>, FabricFlammableBlockTagInteraction> FABRIC_FLAMMABLE = () -> FabricFlammableBlockTagInteraction.CODEC;

    public static void bootStrap() {
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "flammable"), FABRIC_FLAMMABLE);
    }
}
