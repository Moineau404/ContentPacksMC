package mod.moineau.contentpacks.interaction.fluidtag;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class FluidTagInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<TagKey<Fluid>, ?>> ID_MAPPER = new Codecs.IdMapper<>();

    public static void bootstrap() {

    }
}
