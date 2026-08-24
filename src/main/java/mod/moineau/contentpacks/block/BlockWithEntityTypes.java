package mod.moineau.contentpacks.block;

import mod.moineau.api.util.Workaround;
import mod.moineau.contentpacks.api.modifier.blockentitytype.FabricValidBlocksBlockEntityTypeModifier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.piston.MovingPistonBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a more or less temporary solution that allows content-loaded blocks with vanilla block types
 * to automatically be registered in their block entity type supported block list.
 * Use {@link #register(Class, BlockEntityType)} to artificially bind a block type to a block entity type.
 * You can use {@link FabricValidBlocksBlockEntityTypeModifier FabricSupportedBlocks interaction}
 * to add supported blocks directly with content packs.
 */
@Workaround
public final class BlockWithEntityTypes {
    private static final Map<Class<? extends EntityBlock>, BlockEntityType<?>> CLASS2TYPE = new HashMap<>() {{
        put(FurnaceBlock.class, BlockEntityTypes.FURNACE);
        put(ChestBlock.class, BlockEntityTypes.CHEST);
        put(TrappedChestBlock.class, BlockEntityTypes.TRAPPED_CHEST);
        put(EnderChestBlock.class, BlockEntityTypes.ENDER_CHEST);
        put(JukeboxBlock.class, BlockEntityTypes.JUKEBOX);
        put(DispenserBlock.class, BlockEntityTypes.DISPENSER);
        put(DropperBlock.class, BlockEntityTypes.DROPPER);
        put(StandingSignBlock.class, BlockEntityTypes.SIGN);
        put(WallSignBlock.class, BlockEntityTypes.SIGN);
        put(CeilingHangingSignBlock.class, BlockEntityTypes.HANGING_SIGN);
        put(WallHangingSignBlock.class, BlockEntityTypes.HANGING_SIGN);
        put(SpawnerBlock.class, BlockEntityTypes.MOB_SPAWNER);
        put(MovingPistonBlock.class, BlockEntityTypes.PISTON);
        put(BrewingStandBlock.class, BlockEntityTypes.BREWING_STAND);
        put(EnchantingTableBlock.class, BlockEntityTypes.ENCHANTING_TABLE);
        put(EndPortalBlock.class, BlockEntityTypes.END_PORTAL);
        put(BeaconBlock.class, BlockEntityTypes.BEACON);
        put(SkullBlock.class, BlockEntityTypes.SKULL);
        put(WallSkullBlock.class, BlockEntityTypes.SKULL);
        put(WitherSkullBlock.class, BlockEntityTypes.SKULL);
        put(WitherWallSkullBlock.class, BlockEntityTypes.SKULL);
        put(DaylightDetectorBlock.class, BlockEntityTypes.DAYLIGHT_DETECTOR);
        put(HopperBlock.class, BlockEntityTypes.HOPPER);
        put(ComparatorBlock.class, BlockEntityTypes.COMPARATOR);
        put(BannerBlock.class, BlockEntityTypes.BANNER);
        put(StructureBlock.class, BlockEntityTypes.STRUCTURE_BLOCK);
        put(EndGatewayBlock.class, BlockEntityTypes.END_GATEWAY);
        put(CommandBlock.class, BlockEntityTypes.COMMAND_BLOCK);
        put(ShulkerBoxBlock.class, BlockEntityTypes.SHULKER_BOX);
        put(ConduitBlock.class, BlockEntityTypes.CONDUIT);
        put(BarrelBlock.class, BlockEntityTypes.BARREL);
        put(SmokerBlock.class, BlockEntityTypes.SMOKER);
        put(BlastFurnaceBlock.class, BlockEntityTypes.BLAST_FURNACE);
        put(LecternBlock.class, BlockEntityTypes.LECTERN);
        put(BellBlock.class, BlockEntityTypes.BELL);
        put(JigsawBlock.class, BlockEntityTypes.JIGSAW);
        put(CampfireBlock.class, BlockEntityTypes.CAMPFIRE);
        put(BeehiveBlock.class, BlockEntityTypes.BEEHIVE);
        put(SculkSensorBlock.class, BlockEntityTypes.SCULK_SENSOR);
        put(CalibratedSculkSensorBlock.class, BlockEntityTypes.CALIBRATED_SCULK_SENSOR);
        put(SculkCatalystBlock.class, BlockEntityTypes.SCULK_CATALYST);
        put(SculkShriekerBlock.class, BlockEntityTypes.SCULK_SHRIEKER);
        put(ChiseledBookShelfBlock.class, BlockEntityTypes.CHISELED_BOOKSHELF);
        put(BrushableBlock.class, BlockEntityTypes.BRUSHABLE_BLOCK);
        put(DecoratedPotBlock.class, BlockEntityTypes.DECORATED_POT);
        put(CrafterBlock.class, BlockEntityTypes.CRAFTER);
        put(TrialSpawnerBlock.class, BlockEntityTypes.TRIAL_SPAWNER);
        put(VaultBlock.class, BlockEntityTypes.VAULT);
        put(TestBlock.class, BlockEntityTypes.TEST_BLOCK);
        put(TestInstanceBlock.class, BlockEntityTypes.TEST_INSTANCE_BLOCK);
    }};

    public static void register(Class<? extends EntityBlock> clazz, BlockEntityType<?> type) {
        CLASS2TYPE.put(clazz, type);
    }

    public static boolean register(Block block) {
        BlockEntityType<?> type = CLASS2TYPE.get(block.getClass());
        if (type != null) {
            type.addValidBlock(block);
            return true;
        }
        return false;
    }
}