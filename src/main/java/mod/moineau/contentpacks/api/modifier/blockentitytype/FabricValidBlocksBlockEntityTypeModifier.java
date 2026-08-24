package mod.moineau.contentpacks.api.modifier.blockentitytype;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.modifier.Modifier;
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
public record FabricValidBlocksBlockEntityTypeModifier(List<Block> blocks) implements Modifier<BlockEntityType<?>> {
    public static final Codec<FabricValidBlocksBlockEntityTypeModifier> CODEC = BuiltInRegistries.BLOCK.byNameCodec().listOf()
            .xmap(FabricValidBlocksBlockEntityTypeModifier::new, FabricValidBlocksBlockEntityTypeModifier::blocks);

    @Override
    public void apply(BlockEntityType<?> target) {
        blocks.forEach(target::addValidBlock);
    }

    @Override
    public Modifier<BlockEntityType<?>> accumulate(Modifier<BlockEntityType<?>> other) {
       if (other instanceof FabricValidBlocksBlockEntityTypeModifier(List<Block> otherBlocks)) {
           List<Block> blocks = new ArrayList<>();
           blocks.addAll(this.blocks);
           blocks.addAll(otherBlocks);
           return new FabricValidBlocksBlockEntityTypeModifier(blocks);
       }
       return this;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Optional<FabricValidBlocksBlockEntityTypeModifier> get(BlockEntityType<?> entityType) {
        return Optional.of(new ArrayList<>(((BlockEntityTypeAccessor) entityType).getBlocks()))
                .filter(list -> !list.isEmpty())
                .map(FabricValidBlocksBlockEntityTypeModifier::new);
    }
}
