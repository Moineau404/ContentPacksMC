package mod.moineau.contentpacks.resource;

import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.world.biome.ColorMap;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ColorMapLoader extends SinglePreparationResourceReloader<Map<ColorMap, int[]>> {
    public static final ResourceFinder FINDER = new ResourceFinder("textures/colormap", ".png");

    @Override
    protected Map<ColorMap, int[]> prepare(ResourceManager manager, Profiler profiler) {
        final Map<ColorMap, int[]> prepared = new HashMap<>();

        // TODO Bypass if namespace = minecraft
        FINDER.findResources(manager).forEach(((png, resource) -> {
            Identifier identifier = FINDER.toResourceId(png);

            ColorMap colorMap = ColorMap.MAP.get(identifier);

            if (colorMap != null) {
                try {
                    int[] pixels = loadRawTextureData(resource);
                    prepared.put(colorMap, pixels);
                } catch (IOException e) {
                    ContentPacks.LOGGER.debug("Failed to load color map {} from pack {}, ignoring", identifier, resource.getPackId(), e);
                }
            }
        }));

        return prepared;
    }

    @Override
    protected void apply(Map<ColorMap, int[]> prepared, ResourceManager manager, Profiler profiler) {
        prepared.forEach(ColorMap::setColorMap);
    }

    /**
     * Copied from {@link net.minecraft.client.util.RawTextureDataLoader}
     */
    @SuppressWarnings({"deprecation", "ConstantValue"})
    public static int[] loadRawTextureData(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();

        int[] colorMap;
        try (NativeImage nativeImage = NativeImage.read(inputStream)) {
            colorMap = nativeImage.makePixelArray();
        } catch (Throwable t) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable t1) {
                    t.addSuppressed(t1);
                }
            }

            throw t;
        }

        if (inputStream != null) {
            inputStream.close();
        }

        return colorMap;
    }
}
