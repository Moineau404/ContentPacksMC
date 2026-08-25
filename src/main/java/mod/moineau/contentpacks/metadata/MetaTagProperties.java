package mod.moineau.contentpacks.metadata;

import mod.moineau.contentpacks.codec.FabricCodecs;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MetaTagProperties {
    private static final Map<ResourceKey<? extends Registry<?>>, MetaPropertyRegistry<TagKey<?>>> REGISTRIES = new HashMap<>();
    public static final MetaProperty<TagKey<Block>, FlammableBlockRegistry.Entry> FLAMMABLE = new MetaProperty<>("flammable", FabricCodecs.FLAMMABLE_BLOCK_REGISTRY_ENTRY, FlammableBlockRegistry.getDefaultInstance()::add);
    public static final MetaProperty<TagKey<Item>, Float> COMPOSTING_CHANCE = new MetaProperty<>("composting_chance", ExtraCodecs.POSITIVE_FLOAT, CompostableRegistry.INSTANCE::add);
    public static final MetaProperty<TagKey<Item>, Float> FUEL = new MetaProperty<>("fuel", ExtraCodecs.POSITIVE_FLOAT, (tag, value) -> {
        FuelValueEvents.BUILD.register((builder, context) -> builder.add(tag, (int) (context.baseSmeltTime() * value)));
    });

    @SuppressWarnings({"rawtypes","unchecked"})
    public static <O, T> MetaProperty<TagKey<O>, T> register(ResourceKey<? extends Registry<O>> registryName, MetaProperty<TagKey<O>, T> property) {
        MetaPropertyRegistry<TagKey<O>> registry = (MetaPropertyRegistry) REGISTRIES.computeIfAbsent(registryName, _ -> new MetaPropertyRegistry<>());
        registry.register(property);
        return property;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public static <O> List<MetaProperty.Value<TagKey<? super O>, ?>> get(ResourceKey<? extends Registry<O>> registryName, ResourceMetadata metadata) {
        return (List) REGISTRIES.get(registryName).get(metadata);
    }

    static {
        register(Registries.BLOCK, FLAMMABLE);
        register(Registries.ITEM, COMPOSTING_CHANCE);
        register(Registries.ITEM, FUEL);
    }
}
