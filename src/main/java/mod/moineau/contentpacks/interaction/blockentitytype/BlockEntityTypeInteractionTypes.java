package mod.moineau.contentpacks.interaction.blockentitytype;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class BlockEntityTypeInteractionTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<BlockEntityType<?>, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final InteractionType<BlockEntityType<?>, FabricValidBlocksBlockEntityTypeInteraction> FABRIC_VALID_BLOCKS = () -> FabricValidBlocksBlockEntityTypeInteraction.CODEC;

    public static void bootStrap() {
        ID_MAPPER.put(Identifier.fromNamespaceAndPath("fabric", "valid_blocks"), FABRIC_VALID_BLOCKS);
    }
}