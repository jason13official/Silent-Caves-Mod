package io.github.jason13official.silent_caves.impl.client.model;

import io.github.jason13official.silent_caves.Constants;
import io.github.jason13official.silent_caves.impl.common.entity.LivingCube;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class LivingCubeModel extends HierarchicalModel<LivingCube> {

  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cube"), "main");

  private final ModelPart root;
  private final ModelPart body;

  public LivingCubeModel(ModelPart root) {
    this.root = root;
    this.body = root.getChild("body");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition root = meshdefinition.getRoot();

    PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 16, 16);
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(LivingCube livingCube, float v, float v1, float v2, float v3, float v4) {

  }
}
