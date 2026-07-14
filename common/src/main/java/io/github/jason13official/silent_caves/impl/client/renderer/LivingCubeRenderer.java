package io.github.jason13official.silent_caves.impl.client.renderer;

import io.github.jason13official.silent_caves.impl.client.model.LivingCubeModel;
import io.github.jason13official.silent_caves.impl.common.entity.LivingCube;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class LivingCubeRenderer extends MobRenderer<LivingCube, LivingCubeModel> {

  public LivingCubeRenderer(Context context) {
    super(context, new LivingCubeModel(context.bakeLayer(LivingCubeModel.LAYER_LOCATION)), 0.0f);
  }

  @Override
  public ResourceLocation getTextureLocation(LivingCube livingCube) {

    return InventoryMenu.BLOCK_ATLAS;
  }
}
