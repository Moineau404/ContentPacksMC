package mod.moineau.contentpacks.item;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.FunctionUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import mod.moineau.contentpacks.codec.ItemSettingsCodecs;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ItemTypes {
    @Workaround
    private static final Map<Class<? extends Item>, MapCodec<? extends Item>> CLASS2CODEC = new HashMap<>();
    public static final MapCodec<Item> CODEC = ContentRegistries.ITEM_TYPE.getCodec().dispatchMap(ItemTypes::get, Function.identity());
    public static final MapCodec<AirBlockItem> AIR_BLOCK = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.BLOCK.getCodec().optionalFieldOf("block", null).forGetter(item -> null),
            fillSettingsBlock()
    ).apply(instance, AirBlockItem::new)); // AIR
    public static final MapCodec<ArmorStandItem> ARMOR_STAND = create(ArmorStandItem::new);
    public static final MapCodec<ArrowItem> ARROW = create(ArrowItem::new);
    public static final MapCodec<AxeItem> AXE = createTool(AxeItem::new);
    public static final MapCodec<BannerItem> BANNER = createVerticallyAttachableBlock(BannerItem::new);
    public static final MapCodec<BedItem> BED = createBlock(BedItem::new);
    public static final MapCodec<BlockItem> BLOCK = createBlock(BlockItem::new);
    public static final MapCodec<BoatItem> BOAT = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.<EntityType<?>, EntityType<? extends AbstractBoatEntity>>upgrade(Registries.ENTITY_TYPE.getCodec()).fieldOf("entity").forGetter(item -> item.boatEntityType),
            fillSettings()
    ).apply(instance, BoatItem::new));
    public static final MapCodec<BoneMealItem> BONE_MEAL = create(BoneMealItem::new);
    public static final MapCodec<BowItem> BOW = create(BowItem::new);
    public static final MapCodec<BrushItem> BRUSH = create(BrushItem::new);
    public static final MapCodec<BucketItem> BUCKET = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.FLUID.getCodec().fieldOf("fluid").forGetter(item -> item.fluid),
            fillSettings()
    ).apply(instance, BucketItem::new));
    public static final MapCodec<BundleItem> BUNDLE = create(BundleItem::new);
    public static final MapCodec<CompassItem> COMPASS = create(CompassItem::new);
    public static final MapCodec<CrossbowItem> CROSSBOW = create(CrossbowItem::new);
    public static final MapCodec<DebugStickItem> DEBUG_STICK = create(DebugStickItem::new);
    public static final MapCodec<DecorationItem> DECORATION = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.<EntityType<?>, EntityType<? extends AbstractDecorationEntity>>upgrade(Registries.ENTITY_TYPE.getCodec()).fieldOf("entity_type").forGetter(item -> item.entityType),
            fillSettings()
    ).apply(instance, DecorationItem::new)); // HANGING_ENTITY
    public static final MapCodec<DiscFragmentItem> DISC_FRAGMENT = create(DiscFragmentItem::new);
    public static final MapCodec<DyeItem> DYE = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DyeColor.CODEC.fieldOf("color").forGetter(DyeItem::getColor),
            fillSettings()
    ).apply(instance, DyeItem::new));
    public static final MapCodec<EggItem> EGG = create(EggItem::new);
    public static final MapCodec<EmptyMapItem> EMPTY_MAP = create(EmptyMapItem::new);
    public static final MapCodec<EndCrystalItem> END_CRYSTAL = create(EndCrystalItem::new);
    public static final MapCodec<EnderEyeItem> ENDER_EYE = create(EnderEyeItem::new);
    public static final MapCodec<EnderPearlItem> ENDER_PEARL = create(EnderPearlItem::new); // ENDERPEARL
    public static final MapCodec<EntityBucketItem> ENTITY_BUCKET = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.<EntityType<?>, EntityType<? extends MobEntity>>upgrade(Registries.ENTITY_TYPE.getCodec()).fieldOf("entity_type").forGetter(item -> item.entityType),
            Registries.FLUID.getCodec().fieldOf("fluid").forGetter(item -> item.fluid),
            Registries.SOUND_EVENT.getCodec().fieldOf("empty_sound").forGetter(item -> item.emptyingSound),
            fillSettings()
    ).apply(instance, EntityBucketItem::new)); // MOB_BUCKET
    public static final MapCodec<ExperienceBottleItem> EXPERIENCE_BOTTLE = create(ExperienceBottleItem::new);
    public static final MapCodec<FilledMapItem> FILLED_MAP = create(FilledMapItem::new); // MAP
    public static final MapCodec<FireChargeItem> FIRE_CHARGE = create(FireChargeItem::new);
    public static final MapCodec<FireworkRocketItem> FIREWORK_ROCKET = create(FireworkRocketItem::new);
    public static final MapCodec<FishingRodItem> FISHING_ROD = create(FishingRodItem::new);
    public static final MapCodec<FlintAndSteelItem> FLINT_AND_STEEL = create(FlintAndSteelItem::new);
    public static final MapCodec<GlassBottleItem> GLASS_BOTTLE = create(GlassBottleItem::new);
    public static final MapCodec<GlowInkSacItem> GLOW_INK_SAC = create(GlowInkSacItem::new);
    public static final MapCodec<GoatHornItem> GOAT_HORN = create(GoatHornItem::new); // INSTRUMENT
    public static final MapCodec<HangingSignItem> HANGING_SIGN = createVerticallyAttachableBlock(HangingSignItem::new);
    public static final MapCodec<HoeItem> HOE = createTool(HoeItem::new);
    public static final MapCodec<HoneycombItem> HONEYCOMB = create(HoneycombItem::new);
    public static final MapCodec<InkSacItem> INK_SAC = create(InkSacItem::new);
    public static final MapCodec<Item> ITEM = create(Item::new);
    public static final MapCodec<ItemFrameItem> ITEM_FRAME = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.<EntityType<?>, EntityType<? extends AbstractDecorationEntity>>upgrade(Registries.ENTITY_TYPE.getCodec()).fieldOf("entity_type").forGetter(item -> item.entityType),
            fillSettings()
    ).apply(instance, ItemFrameItem::new));
    public static final MapCodec<KnowledgeBookItem> KNOWLEDGE_BOOK = create(KnowledgeBookItem::new);
    public static final MapCodec<LeadItem> LEAD = create(LeadItem::new);
    public static final MapCodec<LingeringPotionItem> LINGERING_POTION = create(LingeringPotionItem::new);
    public static final MapCodec<MaceItem> MACE = create(MaceItem::new);
    public static final MapCodec<MinecartItem> MINECART = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.<EntityType<?>, EntityType<? extends AbstractMinecartEntity>>upgrade(Registries.ENTITY_TYPE.getCodec()).fieldOf("entity_type").forGetter(item -> item.type),
            fillSettings()
    ).apply(instance, MinecartItem::new));
    public static final MapCodec<NameTagItem> NAME_TAG = create(NameTagItem::new);
    public static final MapCodec<OnAStickItem<?>> ON_A_STICK = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.<EntityType<?>, EntityType<? extends ItemSteerable>>upgrade(Registries.ENTITY_TYPE.getCodec()).fieldOf("interact_with").forGetter(item -> item.target),
            Codec.INT.fieldOf("consume_damage").forGetter(item -> item.damagePerUse),
            fillSettings()
    ).apply(instance, OnAStickItem::new)); // FOOD_ON_A_STICK
    public static final MapCodec<OperatorOnlyBlockItem> OPERATOR_ONLY_BLOCK = createBlock(OperatorOnlyBlockItem::new); // GAME_MASTER_BLOCK
    public static final MapCodec<PlaceableOnWaterItem> PLACE_ON_WATER_BLOCK = createBlock(PlaceableOnWaterItem::new); // PLACE_ON_WATER_BLOCK
    public static final MapCodec<PlayerHeadItem> PLAYER_HEAD = createVerticallyAttachableBlock(PlayerHeadItem::new);
    public static final MapCodec<PotionItem> POTION = create(PotionItem::new);
    public static final MapCodec<PowderSnowBucketItem> POWDER_SNOW_BUCKET = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.BLOCK.getCodec().fieldOf("block").forGetter(BlockItem::getBlock),
            Registries.SOUND_EVENT.getCodec().fieldOf("place_sound").forGetter(item -> item.placeSound),
            fillSettings()
    ).apply(instance, PowderSnowBucketItem::new)); // SOLID_BUCKET
    public static final MapCodec<ScaffoldingItem> SCAFFOLDING = createBlock(ScaffoldingItem::new); // SCAFFOLDING_BLOCK
    public static final MapCodec<ShearsItem> SHEARS = create(ShearsItem::new);
    public static final MapCodec<ShieldItem> SHIELD = create(ShieldItem::new);
    public static final MapCodec<ShovelItem> SHOVEL = createTool(ShovelItem::new);
    public static final MapCodec<SignItem> SIGN = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.BLOCK.getCodec().fieldOf("block").forGetter(BlockItem::getBlock),
            Registries.BLOCK.getCodec().fieldOf("wall_block").forGetter(item -> item.wallBlock),
            fillSettingsBlock()
    ).apply(instance, SignItem::new));
    public static final MapCodec<SmithingTemplateItem> SMITHING_TEMPLATE = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TextCodecs.CODEC.optionalFieldOf("applies_to", SmithingTemplateItem.ARMOR_TRIM_APPLIES_TO_TEXT).forGetter(item -> item.appliesToText),
            TextCodecs.CODEC.optionalFieldOf("ingredients", SmithingTemplateItem.ARMOR_TRIM_INGREDIENTS_TEXT).forGetter(item -> item.ingredientsText),
            TextCodecs.CODEC.optionalFieldOf("base_slot_description", SmithingTemplateItem.ARMOR_TRIM_BASE_SLOT_DESCRIPTION_TEXT).forGetter(SmithingTemplateItem::getBaseSlotDescription),
            TextCodecs.CODEC.optionalFieldOf("additional_slot_description", SmithingTemplateItem.ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION_TEXT).forGetter(SmithingTemplateItem::getAdditionsSlotDescription),
            Identifier.CODEC.listOf().optionalFieldOf("base_slot_empty_icon", SmithingTemplateItem.getArmorTrimEmptyBaseSlotTextures()).forGetter(SmithingTemplateItem::getEmptyBaseSlotTextures),
            Identifier.CODEC.listOf().optionalFieldOf("additional_slot_empty_icon", SmithingTemplateItem.getArmorTrimEmptyAdditionsSlotTextures()).forGetter(SmithingTemplateItem::getEmptyAdditionsSlotTextures),
            fillSettings()
    ).apply(instance, SmithingTemplateItem::new));
    public static final MapCodec<SnowballItem> SNOWBALL = create(SnowballItem::new);
    @SuppressWarnings("unchecked")
    public static final MapCodec<SpawnEggItem> SPAWN_EGG = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.<EntityType<?>, EntityType<? extends MobEntity>>upgrade(Registries.ENTITY_TYPE.getCodec()).fieldOf("entity_type").forGetter(item -> (EntityType<? extends MobEntity>) item.type),
            fillSettings()
    ).apply(instance, SpawnEggItem::new));
    public static final MapCodec<SpectralArrowItem> SPECTRAL_ARROW = create(SpectralArrowItem::new);
    public static final MapCodec<SplashPotionItem> SPLASH_POTION = create(SplashPotionItem::new);
    public static final MapCodec<SpyglassItem> SPYGLASS = create(SpyglassItem::new);
    public static final MapCodec<TallBlockItem> TALL_BLOCK = createBlock(TallBlockItem::new); // DOUBLE_HIGH_BLOCK
    public static final MapCodec<TippedArrowItem> TIPPED_ARROW = create(TippedArrowItem::new);
    public static final MapCodec<TridentItem> TRIDENT = create(TridentItem::new);
    public static final MapCodec<VerticallyAttachableBlockItem> VERTICALLY_ATTACHABLE_BLOCK = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.BLOCK.getCodec().fieldOf("block").forGetter(BlockItem::getBlock),
            Registries.BLOCK.getCodec().fieldOf("wall_block").forGetter(item -> item.wallBlock),
            Direction.CODEC.fieldOf("direction").forGetter(item -> item.verticalAttachmentDirection),
            fillSettingsBlock()
    ).apply(instance, VerticallyAttachableBlockItem::new)); // STANDING_AND_WALL_BLOCK
    public static final MapCodec<WindChargeItem> WIND_CHARGE = create(WindChargeItem::new);
    public static final MapCodec<WritableBookItem> WRITABLE_BOOK = create(WritableBookItem::new);
    public static final MapCodec<WrittenBookItem> WRITTEN_BOOK = create(WrittenBookItem::new);

    public static <I extends Item> RecordCodecBuilder<I, Item.Settings> fillSettings() {
        return ItemSettingsCodecs.CODEC.forGetter(item -> ((ContentItemAccessor) item).contentpacks$getSettings());
    }

    public static <I extends Item> RecordCodecBuilder<I, Item.Settings> fillSettingsBlock() {
        return ItemSettingsCodecs.CODEC.xmap(Item.Settings::useBlockPrefixedTranslationKey, Function.identity()).forGetter(item -> ((ContentItemAccessor) item).contentpacks$getSettings());
    }

    public static <I extends Item> MapCodec<I> create(Function<Item.Settings, I> itemFromSettings) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(fillSettings()).apply(instance, itemFromSettings));
    }

    public static <I extends BlockItem> MapCodec<I> createBlock(BiFunction<Block, Item.Settings, I> itemFromBlockAndSettings) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Registries.BLOCK.getCodec().fieldOf("block").forGetter(BlockItem::getBlock),
                fillSettingsBlock()
        ).apply(instance, itemFromBlockAndSettings));
    }

    public static <I extends VerticallyAttachableBlockItem> MapCodec<I> createVerticallyAttachableBlock(Function3<Block, Block, Item.Settings, I> itemFromBlockWallBlockAndSettings) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Registries.BLOCK.getCodec().fieldOf("block").forGetter(BlockItem::getBlock),
                Registries.BLOCK.getCodec().fieldOf("wall_block").forGetter(item -> item.wallBlock),
                fillSettingsBlock()
        ).apply(instance, itemFromBlockWallBlockAndSettings));
    }

    // FIXME Tier etc...
    public static <I extends Item> MapCodec<I> createTool(Function4<ToolMaterial, Float, Float, Item.Settings, I> itemFunction) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ContentRegistries.TOOL_MATERIAL.getCodec().fieldOf("tier").forGetter(FunctionUtil::nothing),
                Codec.FLOAT.fieldOf("base_attack_damage").forGetter(FunctionUtil::nothing),
                Codec.FLOAT.fieldOf("base_attack_speed").forGetter(FunctionUtil::nothing),
                fillSettings()
        ).apply(instance, itemFunction));
    }

    public static void register(Class<? extends Item> itemClass, MapCodec<? extends Item> codec) {
        CLASS2CODEC.put(itemClass, codec);
    }

    public static MapCodec<? extends Item> get(Item item) {
        if (item instanceof ContentItem contentItem) {
            return contentItem.getCodec();
        }
        return CLASS2CODEC.get(item.getClass());
    }

    public static void initialize(Registry<MapCodec<? extends Item>> registry) {
        Registry.register(registry, "air", ItemTypes.AIR_BLOCK);
        Registry.register(registry, "armor_stand", ItemTypes.ARMOR_STAND);
        Registry.register(registry, "arrow", ItemTypes.ARROW);
        Registry.register(registry, "axe", ItemTypes.AXE);
        Registry.register(registry, "banner", ItemTypes.BANNER);
        Registry.register(registry, "bed", ItemTypes.BED);
        Registry.register(registry, "block", ItemTypes.BLOCK);
        Registry.register(registry, "boat", ItemTypes.BOAT);
        Registry.register(registry, "bone_meal", ItemTypes.BONE_MEAL);
        Registry.register(registry, "bow", ItemTypes.BOW);
        Registry.register(registry, "brush", ItemTypes.BRUSH);
        Registry.register(registry, "bucket", ItemTypes.BUCKET);
        Registry.register(registry, "bundle", ItemTypes.BUNDLE);
        Registry.register(registry, "compass", ItemTypes.COMPASS);
        Registry.register(registry, "crossbow", ItemTypes.CROSSBOW);
        Registry.register(registry, "debug_stick", ItemTypes.DEBUG_STICK);
        Registry.register(registry, "hanging_entity", ItemTypes.DECORATION);
        Registry.register(registry, "disc_fragment", ItemTypes.DISC_FRAGMENT);
        Registry.register(registry, "dye", ItemTypes.DYE);
        Registry.register(registry, "egg", ItemTypes.EGG);
        Registry.register(registry, "empty_map", ItemTypes.EMPTY_MAP);
        Registry.register(registry, "end_crystal", ItemTypes.END_CRYSTAL);
        Registry.register(registry, "ender_eye", ItemTypes.ENDER_EYE);
        Registry.register(registry, "enderpearl", ItemTypes.ENDER_PEARL);
        Registry.register(registry, "mob_bucket", ItemTypes.ENTITY_BUCKET);
        Registry.register(registry, "experience_bottle", ItemTypes.EXPERIENCE_BOTTLE);
        Registry.register(registry, "map", ItemTypes.FILLED_MAP);
        Registry.register(registry, "fire_charge", ItemTypes.FIRE_CHARGE);
        Registry.register(registry, "firework_rocket", ItemTypes.FIREWORK_ROCKET);
        Registry.register(registry, "fishing_rod", ItemTypes.FISHING_ROD);
        Registry.register(registry, "flint_and_steel", ItemTypes.FLINT_AND_STEEL);
        Registry.register(registry, "bottle", ItemTypes.GLASS_BOTTLE);
        Registry.register(registry, "glow_ink_sac", ItemTypes.GLOW_INK_SAC);
        Registry.register(registry, "instrument", ItemTypes.GOAT_HORN);
        Registry.register(registry, "hanging_sign", ItemTypes.HANGING_SIGN);
        Registry.register(registry, "hoe", ItemTypes.HOE);
        Registry.register(registry, "honeycomb", ItemTypes.HONEYCOMB);
        Registry.register(registry, "ink_sac", ItemTypes.INK_SAC);
        Registry.register(registry, "item", ItemTypes.ITEM);
        Registry.register(registry, "item_frame", ItemTypes.ITEM_FRAME);
        Registry.register(registry, "knowledge_book", ItemTypes.KNOWLEDGE_BOOK);
        Registry.register(registry, "lead", ItemTypes.LEAD);
        Registry.register(registry, "lingering_potion", ItemTypes.LINGERING_POTION);
        Registry.register(registry, "mace", ItemTypes.MACE);
        Registry.register(registry, "minecart", ItemTypes.MINECART);
        Registry.register(registry, "name_tag", ItemTypes.NAME_TAG);
        Registry.register(registry, "food_on_astick", ItemTypes.ON_A_STICK);
        Registry.register(registry, "game_master_block", ItemTypes.OPERATOR_ONLY_BLOCK);
        Registry.register(registry, "place_on_water_block", ItemTypes.PLACE_ON_WATER_BLOCK);
        Registry.register(registry, "player_head", ItemTypes.PLAYER_HEAD);
        Registry.register(registry, "potion", ItemTypes.POTION);
        Registry.register(registry, "solid_bucket", ItemTypes.POWDER_SNOW_BUCKET);
        Registry.register(registry, "scaffolding_block", ItemTypes.SCAFFOLDING);
        Registry.register(registry, "shears", ItemTypes.SHEARS);
        Registry.register(registry, "shield", ItemTypes.SHIELD);
        Registry.register(registry, "shovel", ItemTypes.SHOVEL);
        Registry.register(registry, "sign", ItemTypes.SIGN);
        Registry.register(registry, "smithing_template", ItemTypes.SMITHING_TEMPLATE);
        Registry.register(registry, "snowball", ItemTypes.SNOWBALL);
        Registry.register(registry, "spawn_egg", ItemTypes.SPAWN_EGG);
        Registry.register(registry, "spectral_arrow", ItemTypes.SPECTRAL_ARROW);
        Registry.register(registry, "splash_potion", ItemTypes.SPLASH_POTION);
        Registry.register(registry, "spyglass", ItemTypes.SPYGLASS);
        Registry.register(registry, "double_high_block", ItemTypes.TALL_BLOCK);
        Registry.register(registry, "tipped_arrow", ItemTypes.TIPPED_ARROW);
        Registry.register(registry, "trident", ItemTypes.TRIDENT);
        Registry.register(registry, "standing_and_wall_block", ItemTypes.VERTICALLY_ATTACHABLE_BLOCK);
        Registry.register(registry, "wind_charge", ItemTypes.WIND_CHARGE);
        Registry.register(registry, "writable_book", ItemTypes.WRITABLE_BOOK);
        Registry.register(registry, "written_book", ItemTypes.WRITTEN_BOOK);
    }

    static {
        CLASS2CODEC.put(Item.class, ITEM);
        CLASS2CODEC.put(AirBlockItem.class, AIR_BLOCK);
        CLASS2CODEC.put(ArmorStandItem.class, ARMOR_STAND);
        CLASS2CODEC.put(ArrowItem.class, ARROW);
        CLASS2CODEC.put(AxeItem.class, AXE);
        CLASS2CODEC.put(BannerItem.class, BANNER);
        CLASS2CODEC.put(BedItem.class, BED);
        CLASS2CODEC.put(BlockItem.class, BLOCK);
        CLASS2CODEC.put(BoatItem.class, BOAT);
        CLASS2CODEC.put(BoneMealItem.class, BONE_MEAL);
        CLASS2CODEC.put(BowItem.class, BOW);
        CLASS2CODEC.put(BrushItem.class, BRUSH);
        CLASS2CODEC.put(BucketItem.class, BUCKET);
        CLASS2CODEC.put(BundleItem.class, BUNDLE);
        CLASS2CODEC.put(CompassItem.class, COMPASS);
        CLASS2CODEC.put(CrossbowItem.class, CROSSBOW);
        CLASS2CODEC.put(DebugStickItem.class, DEBUG_STICK);
        CLASS2CODEC.put(DecorationItem.class, DECORATION);
        CLASS2CODEC.put(DiscFragmentItem.class, DISC_FRAGMENT);
        CLASS2CODEC.put(DyeItem.class, DYE);
        CLASS2CODEC.put(EggItem.class, EGG);
        CLASS2CODEC.put(EmptyMapItem.class, EMPTY_MAP);
        CLASS2CODEC.put(EndCrystalItem.class, END_CRYSTAL);
        CLASS2CODEC.put(EnderEyeItem.class, ENDER_EYE);
        CLASS2CODEC.put(EnderPearlItem.class, ENDER_PEARL);
        CLASS2CODEC.put(EntityBucketItem.class, ENTITY_BUCKET);
        CLASS2CODEC.put(ExperienceBottleItem.class, EXPERIENCE_BOTTLE);
        CLASS2CODEC.put(FilledMapItem.class, FILLED_MAP);
        CLASS2CODEC.put(FireChargeItem.class, FIRE_CHARGE);
        CLASS2CODEC.put(FireworkRocketItem.class, FIREWORK_ROCKET);
        CLASS2CODEC.put(FishingRodItem.class, FISHING_ROD);
        CLASS2CODEC.put(FlintAndSteelItem.class, FLINT_AND_STEEL);
        CLASS2CODEC.put(GlassBottleItem.class, GLASS_BOTTLE);
        CLASS2CODEC.put(GlowInkSacItem.class, GLOW_INK_SAC);
        CLASS2CODEC.put(GoatHornItem.class, GOAT_HORN);
        CLASS2CODEC.put(HangingSignItem.class, HANGING_SIGN);
        CLASS2CODEC.put(HoeItem.class, HOE);
        CLASS2CODEC.put(HoneycombItem.class, HONEYCOMB);
        CLASS2CODEC.put(InkSacItem.class, INK_SAC);
        CLASS2CODEC.put(ItemFrameItem.class, ITEM_FRAME);
        CLASS2CODEC.put(KnowledgeBookItem.class, KNOWLEDGE_BOOK);
        CLASS2CODEC.put(LeadItem.class, LEAD);
        CLASS2CODEC.put(LingeringPotionItem.class, LINGERING_POTION);
        CLASS2CODEC.put(MaceItem.class, MACE);
        CLASS2CODEC.put(MinecartItem.class, MINECART);
        CLASS2CODEC.put(NameTagItem.class, NAME_TAG);
        CLASS2CODEC.put(OnAStickItem.class, ON_A_STICK);
        CLASS2CODEC.put(OperatorOnlyBlockItem.class, OPERATOR_ONLY_BLOCK);
        CLASS2CODEC.put(PlaceableOnWaterItem.class, PLACE_ON_WATER_BLOCK);
        CLASS2CODEC.put(PlayerHeadItem.class, PLAYER_HEAD);
        CLASS2CODEC.put(PotionItem.class, POTION);
        CLASS2CODEC.put(PowderSnowBucketItem.class, POWDER_SNOW_BUCKET);
        CLASS2CODEC.put(ScaffoldingItem.class, SCAFFOLDING);
        CLASS2CODEC.put(ShearsItem.class, SHEARS);
        CLASS2CODEC.put(ShieldItem.class, SHIELD);
        CLASS2CODEC.put(ShovelItem.class, SHOVEL);
        CLASS2CODEC.put(SignItem.class, SIGN);
        CLASS2CODEC.put(SmithingTemplateItem.class, SMITHING_TEMPLATE);
        CLASS2CODEC.put(SnowballItem.class, SNOWBALL);
        CLASS2CODEC.put(SpawnEggItem.class, SPAWN_EGG);
        CLASS2CODEC.put(SpectralArrowItem.class, SPECTRAL_ARROW);
        CLASS2CODEC.put(SplashPotionItem.class, SPLASH_POTION);
        CLASS2CODEC.put(SpyglassItem.class, SPYGLASS);
        CLASS2CODEC.put(TallBlockItem.class, TALL_BLOCK);
        CLASS2CODEC.put(TippedArrowItem.class, TIPPED_ARROW);
        CLASS2CODEC.put(TridentItem.class, TRIDENT);
        CLASS2CODEC.put(VerticallyAttachableBlockItem.class, VERTICALLY_ATTACHABLE_BLOCK);
        CLASS2CODEC.put(WindChargeItem.class, WIND_CHARGE);
        CLASS2CODEC.put(WritableBookItem.class, WRITABLE_BOOK);
        CLASS2CODEC.put(WrittenBookItem.class, WRITTEN_BOOK);
    }
}
