package mod.moineau.contentpacks.render.entity;

import mod.moineau.contentpacks.entity.BoatType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.RaftEntityModel;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;

public class BoatTypeRendering {
    public static final Map<BoatType<?>, EntityModelLayer> MODEL_LAYERS = new IdentityHashMap<>();

    public static void bootstrap() {
        BoatType.MAP.values().forEach((boatType) -> {
            EntityModelLayer modelLayer =  new EntityModelLayer(boatType.getTextureId(), "main");
            MODEL_LAYERS.put(boatType, modelLayer);
            final TexturedModelData modelData = getTexturedModelData(boatType);
            EntityModelLayerRegistry.registerModelLayer(modelLayer, () -> modelData);
            EntityRendererRegistry.register(boatType, context -> new BoatEntityRenderer(context, modelLayer));
        });
    }

    private static @NotNull TexturedModelData getTexturedModelData(@NotNull BoatType<?> boatType) {
        TexturedModelData modelData;
        if (!boatType.isChest() & !boatType.isRaft()) {
            modelData = BoatEntityModel.getTexturedModelData();
        } else if (!boatType.isChest() & boatType.isRaft()) {
            modelData = RaftEntityModel.getTexturedModelData();
        } else if (!boatType.isRaft()) {
            modelData = BoatEntityModel.getChestTexturedModelData();
        } else {
            modelData = RaftEntityModel.getChestTexturedModelData();
        }
        return modelData;
    }
}
