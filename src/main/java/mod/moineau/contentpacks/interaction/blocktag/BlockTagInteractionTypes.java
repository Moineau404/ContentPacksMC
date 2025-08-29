package mod.moineau.contentpacks.interaction.blocktag;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class BlockTagInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<TagKey<Block>, ?>> ID_MAPPER = new Codecs.IdMapper<>();
    public static final InteractionType<TagKey<Block>, FabricFlammableBlockTagInteraction> FABRIC_FLAMMABLE = () -> FabricFlammableBlockTagInteraction.CODEC;

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.of("fabric", "flammable"), FABRIC_FLAMMABLE);
    }
}
