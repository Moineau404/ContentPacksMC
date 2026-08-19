package mod.moineau.contentpacks.interaction.blockentitytype;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.interaction.Interaction;
import mod.moineau.contentpacks.interaction.InteractionType;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @deprecated Content-loaded blocks with vanilla block types are automatically registered in their block entity type supported block list.
 * It is still useful for non-vanilla block types.
 */
@Deprecated
public record FabricValidBlocksBlockEntityTypeInteraction(List<Block> blocks) implements Interaction<BlockEntityType<?>> {
    public static final MapCodec<FabricValidBlocksBlockEntityTypeInteraction> CODEC = BuiltInRegistries.BLOCK.byNameCodec().listOf()
            .xmap(FabricValidBlocksBlockEntityTypeInteraction::new, FabricValidBlocksBlockEntityTypeInteraction::blocks).fieldOf("values");

    @Override
    public void register(BlockEntityType<?> target) {
        blocks.forEach(target::addValidBlock);
    }

    @Override
    public InteractionType<BlockEntityType<?>, ?> getType() {
        return BlockEntityTypeInteractionTypes.FABRIC_VALID_BLOCKS;
    }

    @Override
    public boolean override() {
        return false;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Optional<FabricValidBlocksBlockEntityTypeInteraction> get(BlockEntityType<?> entityType) {
        return Optional.of(new ArrayList<>(((BlockEntityTypeAccessor) entityType).getBlocks()))
                .map(FabricValidBlocksBlockEntityTypeInteraction::new);
    }
}
