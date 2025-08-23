package mod.moineau.contentpacks.mixin.block;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractBlock.class)
public interface AbstractBlockAccessor {
    @Accessor("settings")
    AbstractBlock.Settings getSettings();

    @Invoker("createSettingsCodec")
    static <B extends Block> RecordCodecBuilder<B, AbstractBlock.Settings> invoke$createSettingsCodec() {
        throw new AssertionError();
    }
}
