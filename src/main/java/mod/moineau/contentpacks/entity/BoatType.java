package mod.moineau.contentpacks.entity;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.FunctionUtil;
import mod.moineau.contentpacks.codec.VanillaCodecs;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.vehicle.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

// TODO Bring back the Entity Factory Type etc...
/**
 * Workaround for providing a way to create boats.
 * This is more or less temporary, time to find a proper manner to make entity types content-driven
 * (it is not possible now because of entity rendering complexity ;
 * maybe it will be possible with an implementation of other mods like Entity Model Features and Entity Texture Features).
 */
public class BoatType<T extends AbstractBoatEntity> extends EntityType<T> {
    public static final Map<Identifier, BoatType<?>> MAP = new HashMap<>();
    public static final Codec<BoatType<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectRegistryKey(RegistryKeys.ENTITY_TYPE),
            RegistryKey.createCodec(RegistryKeys.ITEM).fieldOf("item").forGetter(entityType -> entityType.getItem().getRegistryEntry().registryKey()),
            Codec.STRING.fieldOf("texture").forGetter(FunctionUtil::nothing),
            Codec.BOOL.optionalFieldOf("chest", false).forGetter(BoatType::isChest),
            Codec.BOOL.optionalFieldOf("raft", false).forGetter(BoatType::isRaft)
    ).apply(instance, BoatType::of));

    private final Supplier<Item> itemSupplier;
    private final String texture;
    private final Identifier textureId;
    private final boolean chest;
    private final boolean raft;

    public BoatType(
            RegistryKey<EntityType<?>> registryKey,
            EntityType.EntityFactory<T> factory,
            Supplier<Item> itemSupplier,
            String texture,
            boolean chest,
            boolean raft
    ) {
        super(
                factory,
                SpawnGroup.MISC,
                true,
                true,
                false,
                false,
                ImmutableSet.of(),
                EntityDimensions.changing(1.375F, 0.5625F).withEyeHeight(0.5625F),
                1.0F,
                10,
                3,
                Util.createTranslationKey("entity", registryKey.getValue()),
                Optional.empty(),
                FeatureFlags.VANILLA_FEATURES
        );
        this.itemSupplier = itemSupplier;
        this.texture = texture;
        this.textureId = Identifier.of(registryKey.getValue().getNamespace(), (!chest ? "boat" : "chest_boat") + "/" + texture);
        this.chest = chest;
        this.raft = raft;
        MAP.put(registryKey.getValue(), this);
    }

    public Item getItem() {
        return itemSupplier.get();
    }

    public String getTexture() {
        return texture;
    }

    public Identifier getTextureId() {
        return textureId;
    }

    public boolean isChest() {
        return chest;
    }

    public boolean isRaft() {
        return raft;
    }

    public static BoatType<? extends AbstractBoatEntity> of(
            RegistryKey<EntityType<?>> registryKey,
            RegistryKey<Item> item,
            String texture,
            boolean chest,
            boolean raft
    ) {
        Supplier<Item> itemSupplier = Suppliers.memoize(() -> Registries.ITEM.get(item));
        EntityFactory<? extends AbstractBoatEntity> factory;
        if (!chest & !raft) {
            factory = getBoatFactory(itemSupplier);
        } else if (!chest & raft) {
            factory = getRaftFactory(itemSupplier);
        } else if (!raft) {
            factory = getChestBoatFactory(itemSupplier);
        } else {
            factory = getChestRaftFactory(itemSupplier);
        }
        return new BoatType<>(registryKey, factory, itemSupplier, texture, chest, raft);
    }

    private static EntityType.EntityFactory<BoatEntity> getBoatFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> new BoatEntity(type, world, itemSupplier);
    }

    private static EntityType.EntityFactory<ChestBoatEntity> getChestBoatFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> new ChestBoatEntity(type, world, itemSupplier);
    }

    private static EntityType.EntityFactory<RaftEntity> getRaftFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> new RaftEntity(type, world, itemSupplier);
    }

    private static EntityType.EntityFactory<ChestRaftEntity> getChestRaftFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> new ChestRaftEntity(type, world, itemSupplier);
    }
}
