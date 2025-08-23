package mod.moineau.contentpacks.interaction.blockentitytype;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @deprecated Content-loaded blocks with vanilla block types are automatically registered in their block entity type supported block list.
 * It is still useful for non-vanilla block types.
 */
@Deprecated
public record FabricSupportedBlocksBlockEntityTypeInteraction(List<Block> blocks) implements Interaction<BlockEntityType<?>> {
    public static final MapCodec<FabricSupportedBlocksBlockEntityTypeInteraction> CODEC = Registries.BLOCK.getCodec().listOf()
            .xmap(FabricSupportedBlocksBlockEntityTypeInteraction::new, FabricSupportedBlocksBlockEntityTypeInteraction::blocks).fieldOf("values");

    @Override
    public void register(BlockEntityType<?> target) {
        blocks.forEach(target::addSupportedBlock);
    }

    @Override
    public InteractionType<BlockEntityType<?>, ?> getType() {
        return BlockEntityTypeInteractionTypes.FABRIC_SUPPORTED_BLOCKS;
    }

    @Override
    public boolean override() {
        return false;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Optional<FabricSupportedBlocksBlockEntityTypeInteraction> get(BlockEntityType<?> entityType) {
        return Optional.of(new ArrayList<>(((BlockEntityTypeAccessor) entityType).getBlocks()))
                .map(FabricSupportedBlocksBlockEntityTypeInteraction::new);
    }
}
