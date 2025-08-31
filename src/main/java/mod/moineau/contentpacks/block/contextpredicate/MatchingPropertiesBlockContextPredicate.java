package mod.moineau.contentpacks.block.contextpredicate;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.math.Comparison;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Vec3i;

import java.util.Map;

public class MatchingPropertiesBlockContextPredicate extends OffsetContextPredicate {
    public static final MapCodec<MatchingPropertiesBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> registerOffsetField(instance).and(instance.group(
                    Codec.<Property<?>, Comparison<?>>dispatchedMap(
                            ContentRegistries.PROPERTIES.getCodec(), property -> Comparison.createSimplifiedCodec(property.getCodec())
                    ).fieldOf("properties").forGetter(predicate -> predicate.propertyMap),
                    Codec.BOOL.optionalFieldOf("lenient", false).forGetter(predicate -> predicate.lenient))
            ).apply(instance, MatchingPropertiesBlockContextPredicate::new)
    );

    private final ImmutableMap<Property<?>, Comparison<?>> propertyMap;
    private final boolean lenient;

    public MatchingPropertiesBlockContextPredicate(Vec3i vec3i, Map<Property<?>, Comparison<?>> propertyMap, boolean lenient) {
        super(vec3i);
        this.propertyMap = ImmutableMap.copyOf(propertyMap);
        this.lenient = lenient;
    }

    public MatchingPropertiesBlockContextPredicate(Vec3i vec3i, Map<Property<?>, Comparison<?>> propertyMap) {
        this(vec3i, propertyMap, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean test(BlockState state) {
        for (var entry : propertyMap.entrySet()) {
            var value = state.getEntries().get(entry.getKey());
            if (value == null ? !lenient : !((Comparison) entry.getValue()).test(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BlockContextPredicateType<?> getType() {
        return BlockContextPredicateType.MATCHING_PROPERTIES;
    }
}
