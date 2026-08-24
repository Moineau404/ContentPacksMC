package mod.moineau.contentpacks.block;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.MapColor;

public final class MapColors {
    public static final ExtraCodecs.LateBoundIdMapper<String, MapColor> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<MapColor> CODEC = ID_MAPPER.codec(Codec.STRING);

    public static void bootStrap() {
        ID_MAPPER.put("none", MapColor.NONE);
        ID_MAPPER.put("grass", MapColor.GRASS);
        ID_MAPPER.put("sand", MapColor.SAND);
        ID_MAPPER.put("wool", MapColor.WOOL);
        ID_MAPPER.put("fire", MapColor.FIRE);
        ID_MAPPER.put("ice", MapColor.ICE);
        ID_MAPPER.put("metal", MapColor.METAL);
        ID_MAPPER.put("plant", MapColor.PLANT);
        ID_MAPPER.put("snow", MapColor.SNOW);
        ID_MAPPER.put("clay", MapColor.CLAY);
        ID_MAPPER.put("dirt", MapColor.DIRT);
        ID_MAPPER.put("stone", MapColor.STONE);
        ID_MAPPER.put("water", MapColor.WATER);
        ID_MAPPER.put("wood", MapColor.WOOD);
        ID_MAPPER.put("quartz", MapColor.QUARTZ);
        ID_MAPPER.put("color_orange", MapColor.COLOR_ORANGE);
        ID_MAPPER.put("color_magenta", MapColor.COLOR_MAGENTA);
        ID_MAPPER.put("color_light_blue", MapColor.COLOR_LIGHT_BLUE);
        ID_MAPPER.put("color_yellow", MapColor.COLOR_YELLOW);
        ID_MAPPER.put("color_light_green", MapColor.COLOR_LIGHT_GREEN);
        ID_MAPPER.put("color_pink", MapColor.COLOR_PINK);
        ID_MAPPER.put("color_gray", MapColor.COLOR_GRAY);
        ID_MAPPER.put("color_light_gray", MapColor.COLOR_LIGHT_GRAY);
        ID_MAPPER.put("color_cyan", MapColor.COLOR_CYAN);
        ID_MAPPER.put("color_purple", MapColor.COLOR_PURPLE);
        ID_MAPPER.put("color_blue", MapColor.COLOR_BLUE);
        ID_MAPPER.put("color_brown", MapColor.COLOR_BROWN);
        ID_MAPPER.put("color_green", MapColor.COLOR_GREEN);
        ID_MAPPER.put("color_red", MapColor.COLOR_RED);
        ID_MAPPER.put("color_black", MapColor.COLOR_BLACK);
        ID_MAPPER.put("gold", MapColor.GOLD);
        ID_MAPPER.put("diamond", MapColor.DIAMOND);
        ID_MAPPER.put("lapis", MapColor.LAPIS);
        ID_MAPPER.put("emerald", MapColor.EMERALD);
        ID_MAPPER.put("podzol", MapColor.PODZOL);
        ID_MAPPER.put("nether", MapColor.NETHER);
        ID_MAPPER.put("terracotta_white", MapColor.TERRACOTTA_WHITE);
        ID_MAPPER.put("terracotta_orange", MapColor.TERRACOTTA_ORANGE);
        ID_MAPPER.put("terracotta_magenta", MapColor.TERRACOTTA_MAGENTA);
        ID_MAPPER.put("terracotta_light_blue", MapColor.TERRACOTTA_LIGHT_BLUE);
        ID_MAPPER.put("terracotta_yellow", MapColor.TERRACOTTA_YELLOW);
        ID_MAPPER.put("terracotta_light_green", MapColor.TERRACOTTA_LIGHT_GREEN);
        ID_MAPPER.put("terracotta_pink", MapColor.TERRACOTTA_PINK);
        ID_MAPPER.put("terracotta_gray", MapColor.TERRACOTTA_GRAY);
        ID_MAPPER.put("terracotta_light_gray", MapColor.TERRACOTTA_LIGHT_GRAY);
        ID_MAPPER.put("terracotta_cyan", MapColor.TERRACOTTA_CYAN);
        ID_MAPPER.put("terracotta_purple", MapColor.TERRACOTTA_PURPLE);
        ID_MAPPER.put("terracotta_blue", MapColor.TERRACOTTA_BLUE);
        ID_MAPPER.put("terracotta_brown", MapColor.TERRACOTTA_BROWN);
        ID_MAPPER.put("terracotta_green", MapColor.TERRACOTTA_GREEN);
        ID_MAPPER.put("terracotta_red", MapColor.TERRACOTTA_RED);
        ID_MAPPER.put("terracotta_black", MapColor.TERRACOTTA_BLACK);
        ID_MAPPER.put("crimson_nylium", MapColor.CRIMSON_NYLIUM);
        ID_MAPPER.put("crimson_stem", MapColor.CRIMSON_STEM);
        ID_MAPPER.put("crimson_hyphae", MapColor.CRIMSON_HYPHAE);
        ID_MAPPER.put("warped_nylium", MapColor.WARPED_NYLIUM);
        ID_MAPPER.put("warped_stem", MapColor.WARPED_STEM);
        ID_MAPPER.put("warped_hyphae", MapColor.WARPED_HYPHAE);
        ID_MAPPER.put("warped_wart_block", MapColor.WARPED_WART_BLOCK);
        ID_MAPPER.put("deepslate", MapColor.DEEPSLATE);
        ID_MAPPER.put("raw_iron", MapColor.RAW_IRON);
        ID_MAPPER.put("glow_lichen", MapColor.GLOW_LICHEN);
    }
}
