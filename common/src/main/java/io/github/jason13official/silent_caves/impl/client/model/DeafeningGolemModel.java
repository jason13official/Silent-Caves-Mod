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

  public DeafeningGolemModel(ModelPart root) {
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
  public void setupAnim(DeafeningGolem deafeningGolem, float v, float v1, float v2, float v3, float v4) {

  }
}
