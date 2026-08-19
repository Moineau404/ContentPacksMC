package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.world.level.block.grower.TreeGrower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(TreeGrower.class)
public class TreeGrowerMixin {
    @Shadow
    public static final Codec<TreeGrower> CODEC = ContentRegistries.TREE_GROWER.byNameCodec();
}
