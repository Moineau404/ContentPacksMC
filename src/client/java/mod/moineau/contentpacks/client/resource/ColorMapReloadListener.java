package mod.moineau.contentpacks.client.resource;

import com.mojang.blaze3d.platform.NativeImage;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.world.biome.ColorMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class ColorMapReloadListener extends SimplePreparableReloadListener<Map<ColorMap, int[]>> {
    private static final FileToIdConverter FINDER = new FileToIdConverter("textures/colormap", ".png");

    @Override
    protected Map<ColorMap, int[]> prepare(ResourceManager manager, ProfilerFiller profiler) {
        final Map<ColorMap, int[]> prepared = new HashMap<>();

        FINDER.listMatchingResources(manager).forEach(((png, resource) -> {
            Identifier identifier = FINDER.fileToId(png);
            ColorMap colorMap = ColorMap.MAP.get(identifier);

            if (colorMap != null) {
                try {
                    int[] pixels = getPixels(resource);
                    prepared.put(colorMap, pixels);
                } catch (IOException e) {
                    ContentPacksClient.LOGGER.debug("Failed to load color map {} from pack {}, ignoring", identifier, resource.sourcePackId(), e);
                }
            }
        }));

        return prepared;
    }

    @Override
    protected void apply(Map<ColorMap, int[]> prepared, ResourceManager manager, ProfilerFiller profiler) {
        prepared.forEach(ColorMap::update);
    }

    /**
     * Copied from {@link net.minecraft.client.resources.LegacyStuffWrapper}
     */
    @SuppressWarnings({"deprecation"})
    public static int[] getPixels(final Resource resource) throws IOException {
        int[] pixels;
        try (
                InputStream inputStream = resource.open();
                NativeImage image = NativeImage.read(inputStream);
        ) {
            pixels = image.makePixelArray();
        }

        return pixels;
    }
}
