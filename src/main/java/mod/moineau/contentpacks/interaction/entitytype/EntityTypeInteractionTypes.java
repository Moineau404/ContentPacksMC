package mod.moineau.contentpacks.interaction.entitytype;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class EntityTypeInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<EntityType<?>, ?>> ID_MAPPER = new Codecs.IdMapper<>();
    @Deprecated
    public static final Codec<Interaction<EntityType<?>>> CODEC = ID_MAPPER.getCodec(Identifier.CODEC).dispatch(Interaction::getType, InteractionType::codec);

    //public static final InteractionType<EntityType<?>, ***> *** = () -> ***.CODEC;

    public static void bootstrap() {
        //ID_MAPPER.put(Identifier.of("***", "***"), ***);
    }
}