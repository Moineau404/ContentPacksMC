package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.world.biome.ColorMap;
import mod.moineau.contentpacks.world.biome.ColorResolvers;
import net.fabricmc.fabric.api.client.rendering.v1.ColorResolverRegistry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.StrictJsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.Map;

public final class ColorResolverManager implements ResourceLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Client/ColorResolverManager");
    public static final ResourceFinder FINDER = ResourceFinder.json("color_resolver");

    @Override
    public void load(ResourceManager resourceManager) {
        Map<Identifier, Resource> resourceMap = FINDER.findResources(resourceManager);

        for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
            Identifier id = FINDER.toResourceId(entry.getKey());

            Resource resource = entry.getValue();
            try {
                Reader reader = resource.getReader();

                JsonElement jsonElement = StrictJsonParser.parse(reader);

                DataResult<ColorMap> result = ColorMap.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                ColorMap value = result.getPartialOrThrow(JsonParseException::new);

                register(id, value);
                result.ifError(error -> LOGGER.error("Partially loaded color resolver {} from pack {}: {}",
                        id, resource.getPackId(), error.message()));

                try {
                    reader.close();
                } catch (Throwable ignored) {}
            } catch (Exception e) {
                LOGGER.error("Failed to load color resolver {} from pack {}", id, resource.getPackId(), e);
            }
        }
    }

    private void register(Identifier id, ColorMap entry) {
        ColorMap.MAP.put(id, entry);
        ColorResolverRegistry.register(entry); // TODO Proper implementation (currently only loads on start)
        ColorResolvers.ID_MAPPER.put(id, entry);
    }
}
