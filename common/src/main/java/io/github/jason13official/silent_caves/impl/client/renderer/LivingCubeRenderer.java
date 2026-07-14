package io.github.jason13official.silent_caves.impl.client.renderer;

import io.github.jason13official.silent_caves.impl.client.model.LivingCubeModel;
import io.github.jason13official.silent_caves.impl.client.renderer.layer.TextureLayer;
import io.github.jason13official.silent_caves.impl.common.entity.LivingCube;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;

public class LivingCubeRenderer extends MobRenderer<LivingCube, LivingCubeModel> {

  public LivingCubeRenderer(Context context) {
    super(context, new LivingCubeModel(context.bakeLayer(LivingCubeModel.LAYER_LOCATION)), 0.0f);
    this.addLayer(new TextureLayer<>(this));
  }

  /// @return `null` since we delegate to a custom layer
  /// @see TextureLayer
  @Override
  protected @Nullable RenderType getRenderType(LivingCube livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {

    return null;
  }

  @Override
  public ResourceLocation getTextureLocation(LivingCube livingCube) {

    return InventoryMenu.BLOCK_ATLAS;
  }
}
