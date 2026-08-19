package mod.moineau.contentpacks.entity;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.FunctionUtil;
import mod.moineau.contentpacks.codec.VanillaCodecs;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.boat.ChestRaft;
import net.minecraft.world.entity.vehicle.boat.Raft;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
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
public class BoatType<T extends AbstractBoat> extends EntityType<T> {
    public static final Map<Identifier, BoatType<?>> MAP = new HashMap<>();
    public static final Codec<BoatType<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VanillaCodecs.fillInjectRegistryKey(Registries.ENTITY_TYPE),
            ResourceKey.codec(Registries.ITEM).fieldOf("item").forGetter(entityType -> entityType.getItem().builtInRegistryHolder().key()),
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
            ResourceKey<EntityType<?>> resourceKey,
            EntityType.EntityFactory<T> factory,
            Supplier<Item> itemSupplier,
            String texture,
            boolean chest,
            boolean raft
    ) {
        super(
                factory,
                MobCategory.MISC,
                true,
                true,
                false,
                false,
                BlockTags.DEFAULT_IMMUNE_TO,
                EntityDimensions.scalable(1.375F, 0.5625F).withEyeHeight(0.5625F),
                1.0F,
                10,
                3,
                Util.makeDescriptionId("entity", resourceKey.identifier()),
                Optional.empty(),
                FeatureFlags.VANILLA_SET,
                true
        );
        this.itemSupplier = itemSupplier;
        this.texture = texture;
        this.textureId = Identifier.fromNamespaceAndPath(resourceKey.identifier().getNamespace(), (!chest ? "boat" : "chest_boat") + "/" + texture);
        this.chest = chest;
        this.raft = raft;
        MAP.put(resourceKey.identifier(), this);
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

    public static BoatType<? extends AbstractBoat> of(
            ResourceKey<EntityType<?>> resourceKey,
            ResourceKey<Item> item,
            String texture,
            boolean chest,
            boolean raft
    ) {
        Supplier<Item> itemSupplier = Suppliers.memoize(() -> BuiltInRegistries.ITEM.getValue(item));
        EntityFactory<? extends AbstractBoat> factory;
        if (!chest & !raft) {
            factory = boatFactory(itemSupplier);
        } else if (!chest & raft) {
            factory = raftFactory(itemSupplier);
        } else if (!raft) {
            factory = chestBoatFactory(itemSupplier);
        } else {
            factory = chestRaftFactory(itemSupplier);
        }
        return new BoatType<>(resourceKey, factory, itemSupplier, texture, chest, raft);
    }

    private static EntityType.EntityFactory<Boat> boatFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> new Boat(type, world, itemSupplier);
    }

    private static EntityType.EntityFactory<ChestBoat> chestBoatFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> new ChestBoat(type, world, itemSupplier);
    }

    private static EntityType.EntityFactory<Raft> raftFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> new Raft(type, world, itemSupplier);
    }

    private static EntityType.EntityFactory<ChestRaft> chestRaftFactory(Supplier<Item> itemSupplier) {
        return (type, world) -> new ChestRaft(type, world, itemSupplier);
    }
}
