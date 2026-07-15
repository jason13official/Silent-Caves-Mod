package io.github.jason13official.silent_caves;

import io.github.jason13official.silent_caves.impl.client.model.DeafeningGolemModel;
import io.github.jason13official.silent_caves.impl.client.model.MonsterCubeModel;
import io.github.jason13official.silent_caves.impl.client.renderer.DeafeningGolemRenderer;
import io.github.jason13official.silent_caves.impl.client.renderer.MonsterCubeRenderer;
import io.github.jason13official.silent_caves.impl.common.registry.ModEntities;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class SilentCavesClient {

  public static void init() {
  }

  @SuppressWarnings("all")
  public static void registerEntityRenderers(BiConsumer<EntityType, EntityRendererProvider> consumer) {

    consumer.accept(ModEntities.MONSTER_CUBE, MonsterCubeRenderer::new);
    consumer.accept(ModEntities.DEAFENING_GOLEM, DeafeningGolemRenderer::new);
  }

  public static void registerEntityModels(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {

    consumer.accept(MonsterCubeModel.LAYER_LOCATION, MonsterCubeModel::createBodyLayer);
    consumer.accept(DeafeningGolemModel.LAYER_LOCATION, DeafeningGolemModel::createBodyLayer);
  }
}