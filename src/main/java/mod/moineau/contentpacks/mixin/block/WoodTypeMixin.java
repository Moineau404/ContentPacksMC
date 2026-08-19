package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Codec replacement
 */
@Mixin(WoodType.class)
public class WoodTypeMixin {
    @Shadow
    public static final Codec<WoodType> CODEC = ContentRegistries.WOOD_TYPE.byNameCodec();
}
