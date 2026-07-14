package io.github.jason13official.silent_caves;

import io.github.jason13official.silent_caves.impl.client.model.LivingCubeModel;
import io.github.jason13official.silent_caves.impl.client.renderer.LivingCubeRenderer;
import io.github.jason13official.silent_caves.impl.common.registry.ModEntities;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class SilentCavesClient {

  public static void init() {
  }

  @SuppressWarnings("all")
  public static void registerEntityRenderers(BiConsumer<EntityType, EntityRendererProvider> consumer) {

    consumer.accept(ModEntities.LIVING_CUBE, LivingCubeRenderer::new);
  }

  public static void registerEntityModels(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {

    consumer.accept(LivingCubeModel.LAYER_LOCATION, LivingCubeModel::createBodyLayer);
  }
}