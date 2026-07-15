package io.github.jason13official.silent_caves.impl.client.renderer;

import io.github.jason13official.silent_caves.impl.client.model.DeafeningGolemModel;
import io.github.jason13official.silent_caves.impl.client.renderer.layer.TextureLayer;
import io.github.jason13official.silent_caves.impl.common.entity.DeafeningGolem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;

public class DeafeningGolemRenderer extends MobRenderer<DeafeningGolem, DeafeningGolemModel> {

  public DeafeningGolemRenderer(Context context) {
    super(context, new DeafeningGolemModel(context.bakeLayer(DeafeningGolemModel.LAYER_LOCATION)), 1.0f);
    this.addLayer(new TextureLayer<>(this));
  }

  /// @return `null` since we delegate to a custom layer
  /// @see TextureLayer
  @Override
  protected @Nullable RenderType getRenderType(DeafeningGolem livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {

    return null;
  }

  @Override
  public ResourceLocation getTextureLocation(DeafeningGolem golem) {

    return InventoryMenu.BLOCK_ATLAS;
  }
}
