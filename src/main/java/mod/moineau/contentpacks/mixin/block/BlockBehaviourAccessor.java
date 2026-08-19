package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockBehaviour.class)
public interface BlockBehaviourAccessor {
    @Accessor("properties")
    BlockBehaviour.Properties getProperties();

    @Invoker("propertiesCodec")
    static <B extends Block> RecordCodecBuilder<B, BlockBehaviour.Properties> invoke$propertiesCodec() {
        throw new AssertionError();
    }
}
