package mod.moineau.contentpacks.interaction.fluid;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;

public final class FluidInteractionTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<Fluid, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();

    public static void bootStrap() {

    }
}