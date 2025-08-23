package mod.moineau.contentpacks.block;

import mod.moineau.contentpacks.api.util.Workaround;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a more or less temporary solution that allows content-loaded blocks with vanilla block types
 * to automatically be registered in their block entity type supported block list.
 * Use {@link #put(Class, BlockEntityType)} to artificially bind a block type to a block entity type.
 * You can use {@link mod.moineau.contentpacks.interaction.blockentitytype.FabricSupportedBlocksBlockEntityTypeInteraction FabricSupportedBlocks interaction}
 * to add supported blocks directly with content packs.
 */
@Workaround
public final class BlockWithEntityTypes {
    private static final Map<Class<? extends BlockEntityProvider>, BlockEntityType<?>> CLASS2TYPE = new HashMap<>() {{
        put(FurnaceBlock.class, BlockEntityType.FURNACE);
        put(ChestBlock.class, BlockEntityType.CHEST);
        put(TrappedChestBlock.class, BlockEntityType.TRAPPED_CHEST);
        put(EnderChestBlock.class, BlockEntityType.ENDER_CHEST);
        put(JukeboxBlock.class, BlockEntityType.JUKEBOX);
        put(DispenserBlock.class, BlockEntityType.DISPENSER);
        put(DropperBlock.class, BlockEntityType.DROPPER);
        put(SignBlock.class, BlockEntityType.SIGN);
        put(WallSignBlock.class, BlockEntityType.SIGN);
        put(HangingSignBlock.class, BlockEntityType.HANGING_SIGN);
        put(WallHangingSignBlock.class, BlockEntityType.HANGING_SIGN);
        put(SpawnerBlock.class, BlockEntityType.MOB_SPAWNER);
        put(PistonExtensionBlock.class, BlockEntityType.PISTON);
        put(BrewingStandBlock.class, BlockEntityType.BREWING_STAND);
        put(EnchantingTableBlock.class, BlockEntityType.ENCHANTING_TABLE);
        put(EndPortalBlock.class, BlockEntityType.END_PORTAL);
        put(BeaconBlock.class, BlockEntityType.BEACON);
        put(SkullBlock.class, BlockEntityType.SKULL);
        put(WallSkullBlock.class, BlockEntityType.SKULL);
        put(WitherSkullBlock.class, BlockEntityType.SKULL);
        put(WallWitherSkullBlock.class, BlockEntityType.SKULL);
        put(DaylightDetectorBlock.class, BlockEntityType.DAYLIGHT_DETECTOR);
        put(HopperBlock.class, BlockEntityType.HOPPER);
        put(ComparatorBlock.class, BlockEntityType.COMPARATOR);
        put(BannerBlock.class, BlockEntityType.BANNER);
        put(StructureBlock.class, BlockEntityType.STRUCTURE_BLOCK);
        put(EndGatewayBlock.class, BlockEntityType.END_GATEWAY);
        put(CommandBlock.class, BlockEntityType.COMMAND_BLOCK);
        put(ShulkerBoxBlock.class, BlockEntityType.SHULKER_BOX);
        put(BedBlock.class, BlockEntityType.BED);
        put(ConduitBlock.class, BlockEntityType.CONDUIT);
        put(BarrelBlock.class, BlockEntityType.BARREL);
        put(SmokerBlock.class, BlockEntityType.SMOKER);
        put(BlastFurnaceBlock.class, BlockEntityType.BLAST_FURNACE);
        put(LecternBlock.class, BlockEntityType.LECTERN);
        put(BellBlock.class, BlockEntityType.BELL);
        put(JigsawBlock.class, BlockEntityType.JIGSAW);
        put(CampfireBlock.class, BlockEntityType.CAMPFIRE);
        put(BeehiveBlock.class, BlockEntityType.BEEHIVE);
        put(SculkSensorBlock.class, BlockEntityType.SCULK_SENSOR);
        put(CalibratedSculkSensorBlock.class, BlockEntityType.CALIBRATED_SCULK_SENSOR);
        put(SculkCatalystBlock.class, BlockEntityType.SCULK_CATALYST);
        put(SculkShriekerBlock.class, BlockEntityType.SCULK_SHRIEKER);
        put(ChiseledBookshelfBlock.class, BlockEntityType.CHISELED_BOOKSHELF);
        put(BrushableBlock.class, BlockEntityType.BRUSHABLE_BLOCK);
        put(DecoratedPotBlock.class, BlockEntityType.DECORATED_POT);
        put(CrafterBlock.class, BlockEntityType.CRAFTER);
        put(TrialSpawnerBlock.class, BlockEntityType.TRIAL_SPAWNER);
        put(VaultBlock.class, BlockEntityType.VAULT);
        put(TestBlock.class, BlockEntityType.TEST_BLOCK);
        put(TestInstanceBlock.class, BlockEntityType.TEST_INSTANCE_BLOCK);
    }};

    public static void put(Class<? extends BlockEntityProvider> clazz, BlockEntityType<?> type) {
        CLASS2TYPE.put(clazz, type);
    }

    public static boolean register(Block block) {
        BlockEntityType<?> type = CLASS2TYPE.get(block.getClass());
        if (type != null) {
            type.addSupportedBlock(block);
            return true;
        }
        return false;
    }
}