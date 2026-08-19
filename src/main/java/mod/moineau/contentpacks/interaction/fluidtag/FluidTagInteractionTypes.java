package mod.moineau.contentpacks.interaction.fluidtag;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;

public final class FluidTagInteractionTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<TagKey<Fluid>, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();

    public static void bootStrap() {

    }
}
