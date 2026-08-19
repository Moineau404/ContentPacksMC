package mod.moineau.contentpacks.block.statepredicates;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.math.Comparison;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Map;

public class MatchingPropertiesStatePredicate extends OffsetContextPredicate {
    public static final MapCodec<MatchingPropertiesStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> registerOffsetField(instance).and(instance.group(
                    Codec.<Property<?>, Comparison<?>>dispatchedMap(
                            ContentRegistries.PROPERTIES.byNameCodec(), property -> Comparison.createSimplifiedCodec(property.codec())
                    ).fieldOf("properties").forGetter(predicate -> predicate.propertyMap),
                    Codec.BOOL.optionalFieldOf("lenient", false).forGetter(predicate -> predicate.lenient))
            ).apply(instance, MatchingPropertiesStatePredicate::new)
    );

    private final ImmutableMap<Property<?>, Comparison<?>> propertyMap;
    private final boolean lenient;

    public MatchingPropertiesStatePredicate(Vec3i vec3i, Map<Property<?>, Comparison<?>> propertyMap, boolean lenient) {
        super(vec3i);
        this.propertyMap = ImmutableMap.copyOf(propertyMap);
        this.lenient = lenient;
    }

    public MatchingPropertiesStatePredicate(Vec3i vec3i, Map<Property<?>, Comparison<?>> propertyMap) {
        this(vec3i, propertyMap, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean test(BlockState state) {
        for (var entry : propertyMap.entrySet()) {
            var value = state.getValue(entry.getKey());
            if (value == null ? !lenient : !((Comparison) entry.getValue()).test(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public StatePredicateType<?> getType() {
        return StatePredicateType.MATCHING_PROPERTIES;
    }
}
