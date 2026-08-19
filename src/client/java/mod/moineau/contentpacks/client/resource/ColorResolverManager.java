package mod.moineau.contentpacks.client.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.world.biome.ColorMap;
import mod.moineau.contentpacks.client.world.biome.ColorResolvers;
import mod.moineau.contentpacks.resource.ResourceLoader;
import net.fabricmc.fabric.api.client.rendering.v1.ColorResolverRegistry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;

import java.io.Reader;
import java.util.Map;

public final class ColorResolverManager implements ResourceLoader {
    public static final FileToIdConverter FINDER = FileToIdConverter.json("color_resolver");

    @Override
    public void load(ResourceManager resourceManager) {
        Map<Identifier, Resource> resourceMap = FINDER.listMatchingResources(resourceManager);

        for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
            Identifier id = FINDER.fileToId(entry.getKey());
            Resource resource = entry.getValue();

            try (Reader reader = resource.openAsReader()) {
                JsonElement jsonElement = StrictJsonParser.parse(reader);

                DataResult<ColorMap> result = ColorMap.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                ColorMap value = result.getPartialOrThrow(JsonParseException::new);

                register(id, value);
                result.ifError(error -> ContentPacksClient.LOGGER.error("Partially loaded color resolver {} from pack {}: {}", id, resource.sourcePackId(), error.message()));
            } catch (Exception e) {
                ContentPacksClient.LOGGER.error("Failed to load color resolver {} from pack {}", id, resource.sourcePackId(), e);
            }
        }
    }

    private void register(Identifier id, ColorMap entry) {
        ColorMap.MAP.put(id, entry);
        ColorResolverRegistry.register(entry); // TODO Proper implementation (currently only loads on start)
        ColorResolvers.ID_MAPPER.put(id, entry);
    }
}
