package mod.moineau.contentpacks.interaction;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.block.BlockInteractionTypes;
import mod.moineau.contentpacks.interaction.blockentitytype.BlockEntityTypeInteractionTypes;
import mod.moineau.contentpacks.interaction.blocktag.BlockTagInteractionTypes;
import mod.moineau.contentpacks.interaction.entitytype.EntityTypeInteractionTypes;
import mod.moineau.contentpacks.interaction.fluid.FluidInteractionTypes;
import mod.moineau.contentpacks.interaction.fluidtag.FluidTagInteractionTypes;
import mod.moineau.contentpacks.interaction.item.ItemInteractionTypes;
import mod.moineau.contentpacks.interaction.itemtag.ItemTagInteractionTypes;

public interface InteractionType<T, I extends Interaction<T>> {
    MapCodec<I> codec();

    static void bootStrap() {
        BlockInteractionTypes.bootStrap();
        BlockTagInteractionTypes.bootStrap();
        FluidInteractionTypes.bootStrap();
        FluidTagInteractionTypes.bootStrap();
        ItemInteractionTypes.bootStrap();
        ItemTagInteractionTypes.bootStrap();
        EntityTypeInteractionTypes.bootStrap();
        BlockEntityTypeInteractionTypes.bootStrap();
    }
}
