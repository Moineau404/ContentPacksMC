package mod.moineau.contentpacks.interaction.entitytype;

import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;

public final class EntityTypeInteractionTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, InteractionType<EntityType<?>, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();

    public static void bootStrap() {

    }
}