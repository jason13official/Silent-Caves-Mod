package io.github.jason13official.silent_caves.impl.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

public class TextureLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

  public TextureLayer(RenderLayerParent<T, M> renderer) {
    super(renderer);
  }

  @Override
  public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

    if (entity.isInvisible()) return;

    this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.cutout()), packedLight, OverlayTexture.NO_OVERLAY);
  }
}
