package mod.moineau.contentpacks.block;

import com.mojang.serialization.Codec;
import net.minecraft.block.MapColor;
import net.minecraft.util.dynamic.Codecs;

// TODO Content-driven map colors ?
public final class MapColors {
    public static final Codecs.IdMapper<String, MapColor> ID_MAPPER = new Codecs.IdMapper<>();
    public static final Codec<MapColor> CODEC = ID_MAPPER.getCodec(Codec.STRING);

    public static void bootstrap() {
        ID_MAPPER.put("clear", MapColor.CLEAR);
        ID_MAPPER.put("pale_green", MapColor.PALE_GREEN);
        ID_MAPPER.put("pale_yellow", MapColor.PALE_YELLOW);
        ID_MAPPER.put("white_gray", MapColor.WHITE_GRAY);
        ID_MAPPER.put("bright_red", MapColor.BRIGHT_RED);
        ID_MAPPER.put("pale_purple", MapColor.PALE_PURPLE);
        ID_MAPPER.put("iron_gray", MapColor.IRON_GRAY);
        ID_MAPPER.put("dark_green", MapColor.DARK_GREEN);
        ID_MAPPER.put("white", MapColor.WHITE);
        ID_MAPPER.put("light_blue_gray", MapColor.LIGHT_BLUE_GRAY);
        ID_MAPPER.put("dirt_brown", MapColor.DIRT_BROWN);
        ID_MAPPER.put("stone_gray", MapColor.STONE_GRAY);
        ID_MAPPER.put("water_blue", MapColor.WATER_BLUE);
        ID_MAPPER.put("oak_tan", MapColor.OAK_TAN);
        ID_MAPPER.put("off_white", MapColor.OFF_WHITE);
        ID_MAPPER.put("orange", MapColor.ORANGE);
        ID_MAPPER.put("magenta", MapColor.MAGENTA);
        ID_MAPPER.put("light_blue", MapColor.LIGHT_BLUE);
        ID_MAPPER.put("yellow", MapColor.YELLOW);
        ID_MAPPER.put("lime", MapColor.LIME);
        ID_MAPPER.put("pink", MapColor.PINK);
        ID_MAPPER.put("gray", MapColor.GRAY);
        ID_MAPPER.put("light_gray", MapColor.LIGHT_GRAY);
        ID_MAPPER.put("cyan", MapColor.CYAN);
        ID_MAPPER.put("purple", MapColor.PURPLE);
        ID_MAPPER.put("blue", MapColor.BLUE);
        ID_MAPPER.put("brown", MapColor.BROWN);
        ID_MAPPER.put("green", MapColor.GREEN);
        ID_MAPPER.put("red", MapColor.RED);
        ID_MAPPER.put("black", MapColor.BLACK);
        ID_MAPPER.put("gold", MapColor.GOLD);
        ID_MAPPER.put("diamond_blue", MapColor.DIAMOND_BLUE);
        ID_MAPPER.put("lapis_blue", MapColor.LAPIS_BLUE);
        ID_MAPPER.put("emerald_green", MapColor.EMERALD_GREEN);
        ID_MAPPER.put("spruce_brown", MapColor.SPRUCE_BROWN);
        ID_MAPPER.put("dark_red", MapColor.DARK_RED);
        ID_MAPPER.put("terracotta_white", MapColor.TERRACOTTA_WHITE);
        ID_MAPPER.put("terracotta_orange", MapColor.TERRACOTTA_ORANGE);
        ID_MAPPER.put("terracotta_magenta", MapColor.TERRACOTTA_MAGENTA);
        ID_MAPPER.put("terracotta_light_blue", MapColor.TERRACOTTA_LIGHT_BLUE);
        ID_MAPPER.put("terracotta_yellow", MapColor.TERRACOTTA_YELLOW);
        ID_MAPPER.put("terracotta_lime", MapColor.TERRACOTTA_LIME);
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
        ID_MAPPER.put("dull_red", MapColor.DULL_RED);
        ID_MAPPER.put("dull_pink", MapColor.DULL_PINK);
        ID_MAPPER.put("dark_crimson", MapColor.DARK_CRIMSON);
        ID_MAPPER.put("teal", MapColor.TEAL);
        ID_MAPPER.put("dark_aqua", MapColor.DARK_AQUA);
        ID_MAPPER.put("dark_dull_pink", MapColor.DARK_DULL_PINK);
        ID_MAPPER.put("bright_teal", MapColor.BRIGHT_TEAL);
        ID_MAPPER.put("deepslate_gray", MapColor.DEEPSLATE_GRAY);
        ID_MAPPER.put("raw_iron_pink", MapColor.RAW_IRON_PINK);
        ID_MAPPER.put("lichen_green", MapColor.LICHEN_GREEN);
    }
}
