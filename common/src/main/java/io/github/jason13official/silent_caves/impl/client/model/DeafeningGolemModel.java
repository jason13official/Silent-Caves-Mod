package io.github.jason13official.silent_caves.impl.client.model;

import io.github.jason13official.silent_caves.SilentCaves;
import io.github.jason13official.silent_caves.impl.common.entity.DeafeningGolem;
import io.github.jason13official.silent_caves.impl.common.entity.MonsterCube;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class DeafeningGolemModel extends HierarchicalModel<DeafeningGolem> {

  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(SilentCaves.identifier("deafening_golem"), "main");

  private final ModelPart root;
  private final ModelPart body;
  private final ModelPart armLeft;
  private final ModelPart armRight;
  private final ModelPart head;

  public DeafeningGolemModel(ModelPart root) {
    this.root = root;
    this.body = root.getChild("body");
    this.armLeft = this.body.getChild("armLeft");
    this.armRight = this.body.getChild("armRight");
    this.head = root.getChild("head");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition root = meshdefinition.getRoot();

    PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.1125F, 1.25F, 0.5236F, 0.0F, 0.0F));

    body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(1, 0).addBox(-8.0F, -22.5F, -6.0F, 16.0F, 34.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.1961F, 4.0F, 0.0873F, 0.0F, 0.0F));

    body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 5).addBox(-5.5F, -3.5F, -5.5F, 11.0F, 7.0F, 11.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 10.8038F, 5.2F, 0.0873F, 0.0F, 0.0F));

    body.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(10, 8).addBox(-4.0F, 0.1029F, -4.0981F, 8.0F, 34.0F, 8.0F, new CubeDeformation(0.0F))
        .texOffs(10, 8).addBox(-4.0F, 36.1029F, -4.0981F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
        .texOffs(15, 8).addBox(1.0F, 34.1029F, -4.0981F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, -25.1125F, 6.0F, -0.5236F, 0.0F, 0.0F));

    body.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(10, 8).mirror().addBox(-4.0F, 0.1029F, -4.0981F, 8.0F, 34.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
        .texOffs(12, 8).mirror().addBox(-4.0F, 36.1029F, -4.0981F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
        .texOffs(15, 8).mirror().addBox(-4.0F, 34.1029F, -4.0981F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.0F, -25.1125F, 6.0F, -0.5236F, 0.0F, 0.0F));

    root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -12.0F, -10.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -12.9572F, -8.6526F));

    return LayerDefinition.create(meshdefinition, 16, 16);
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(DeafeningGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.root().getAllParts().forEach(ModelPart::resetPose);
    this.applyHeadRotation(netHeadYaw);
  }

  private void applyHeadRotation(float headRotation) {
    this.head.yRot = headRotation * (float) (Math.PI / 180.0);
  }
}
