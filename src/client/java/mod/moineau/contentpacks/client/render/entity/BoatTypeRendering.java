package mod.moineau.contentpacks.client.render.entity;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mod.moineau.contentpacks.entity.BoatType;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.model.object.boat.RaftModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BoatTypeRendering {
    public static final Map<BoatType<?>, ModelLayerLocation> MODEL_LAYERS = new Object2ObjectOpenHashMap<>();
    public static final Map<BoatType<?>, EntityRendererProvider<? extends AbstractBoat>> ENTITY_RENDER_PROVIDERS = new Object2ObjectOpenHashMap<>();

    public static void bootStrap() {
        BoatType.MAP.values().forEach((boatType) -> {
            ModelLayerLocation modelId =  new ModelLayerLocation(boatType.getTextureId(), "main");
            MODEL_LAYERS.put(boatType, modelId);
            final LayerDefinition layerDefinition = getTexturedModelData(boatType);
            ModelLayerRegistry.registerModelLayer(modelId, () -> layerDefinition);
            EntityRendererProvider<? extends AbstractBoat> provider = context -> new BoatRenderer(context, modelId);
            ENTITY_RENDER_PROVIDERS.put(boatType, provider);
            //noinspection unchecked,rawtypes
            EntityRenderers.register(boatType, (EntityRendererProvider) provider);
        });
    }

    private static @NotNull LayerDefinition getTexturedModelData(@NotNull BoatType<?> boatType) {
        LayerDefinition modelData;
        if (!boatType.isChest() & !boatType.isRaft()) {
            modelData = BoatModel.createBoatModel();
        } else if (!boatType.isChest() & boatType.isRaft()) {
            modelData = RaftModel.createRaftModel();
        } else if (!boatType.isRaft()) {
            modelData = BoatModel.createChestBoatModel();
        } else {
            modelData = RaftModel.createChestRaftModel();
        }
        return modelData;
    }
}
