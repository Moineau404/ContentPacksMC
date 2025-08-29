package mod.moineau.contentpacks.interaction.entitytype;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class EntityTypeInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<EntityType<?>, ?>> ID_MAPPER = new Codecs.IdMapper<>();

    public static void bootstrap() {

    }
}