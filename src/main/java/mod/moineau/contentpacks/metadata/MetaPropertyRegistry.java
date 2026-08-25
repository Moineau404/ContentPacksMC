package mod.moineau.contentpacks.metadata;

import net.minecraft.server.packs.resources.ResourceMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MetaPropertyRegistry<O> {
    private final Map<String, MetaProperty<? super O, ?>> map = new HashMap<>();

    public void register(MetaProperty<? super O, ?> property) {
        this.map.put(property.getName(), property);
    }

    public List<MetaProperty.Value<? super O, ?>> get(ResourceMetadata metadata) {
        return this.map.values().stream().map(property -> property.getValue(metadata)).<MetaProperty.Value<? super O, ?>>flatMap(Optional::stream).toList();
    }

    public List<MetaProperty.Value<? super O, ?>> get(O object) {
        return this.map.values().stream().map(property -> property.getValue(object)).<MetaProperty.Value<? super O, ?>>flatMap(Optional::stream).toList();
    }
}
