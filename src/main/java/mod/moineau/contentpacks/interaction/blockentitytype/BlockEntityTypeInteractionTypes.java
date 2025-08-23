package mod.moineau.contentpacks.interaction.blockentitytype;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public final class BlockEntityTypeInteractionTypes {
    public static final Codecs.IdMapper<Identifier, InteractionType<BlockEntityType<?>, ?>> ID_MAPPER = new Codecs.IdMapper<>();
    @Deprecated
    public static final Codec<Interaction<BlockEntityType<?>>> CODEC = ID_MAPPER.getCodec(Identifier.CODEC).dispatch(Interaction::getType, InteractionType::codec);

    public static final InteractionType<BlockEntityType<?>, FabricSupportedBlocksBlockEntityTypeInteraction> FABRIC_SUPPORTED_BLOCKS = () -> FabricSupportedBlocksBlockEntityTypeInteraction.CODEC;

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.of("fabric", "supported_blocks"), FABRIC_SUPPORTED_BLOCKS);
    }
}