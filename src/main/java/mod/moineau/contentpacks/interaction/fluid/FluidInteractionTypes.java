package mod.moineau.contentpacks.interaction.fluid;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class FluidInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<Fluid, ?>> ID_MAPPER = new Codecs.IdMapper<>();

    public static void bootstrap() {

    }
}