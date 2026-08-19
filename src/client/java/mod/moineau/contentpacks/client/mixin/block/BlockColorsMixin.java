package mod.moineau.contentpacks.client.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.client.render.block.ContentBlockColors;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.*;

//TODO Add compatibility with Sodium !!!
// - Stop using using custom map, use default IdList and make the blockstate -> provider thing directly in block tint source (or make it just normal block -> provider)
@Mixin(BlockColors.class)
public abstract class BlockColorsMixin implements ContentBlockColors {
    @Shadow
    @Final
    private Map<Block, List<BlockTintSource>> sources;

    @Unique
    private final Map<Block, List<BlockTintSource>> contentpacks$sourceOverrides = new IdentityHashMap<>();

    /**
     * @author Moineau
     * @reason Allows block tints to be loaded via resource packs
     */
    @Overwrite
    public List<BlockTintSource> getTintSources(final BlockState state) {
        return Objects.requireNonNullElseGet(
                this.contentpacks$sourceOverrides.get(state.getBlock()),
                () -> this.sources.getOrDefault(state.getBlock(), List.of())
        );
    }

    @ModifyVariable(method = "getColoringProperties", at = @At(value = "STORE"), name = "sources")
    private List<BlockTintSource> inject$getColoringProperties(List<BlockTintSource> sources, @Local(name = "block", argsOnly = true) Block block) {
        return Objects.requireNonNullElse(this.contentpacks$sourceOverrides.get(block), sources);
    }

    @Unique
    @Override
    public void contentpacks$addSourceOverride(Block block, BlockTintSource tintSource) {
        this.contentpacks$sourceOverrides.put(block, List.of(tintSource));
    }

    @Unique
    @Override
    public void contentpacks$addSourceOverrides(Block block, List<BlockTintSource> tintSources) {
        this.contentpacks$sourceOverrides.put(block, tintSources);
    }

    @Unique
    @Override
    public void contentpacks$clearSourceOverrides() {
        this.contentpacks$sourceOverrides.clear();
    }
}
