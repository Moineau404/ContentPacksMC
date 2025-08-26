package mod.moineau.contentpacks.mixin;

import com.google.common.collect.ImmutableSortedMap;
import mod.moineau.contentpacks.state.PropertiesRegistry;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@Mixin(StateManager.class)
public class StateManagerMixin<O, S extends State<O, S>> {
    @Shadow
    @Final
    private ImmutableSortedMap<String, Property<?>> properties;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableSortedMap;copyOf(Ljava/util/Map;)Lcom/google/common/collect/ImmutableSortedMap;"))
    private void inject$init(Function<O, S> defaultStateGetter, O owner, StateManager.Factory<O, S> factory, Map<String, Property<?>> propertiesMap, CallbackInfo ci) {
        PropertiesRegistry.register(owner, this.properties);
    }
}
