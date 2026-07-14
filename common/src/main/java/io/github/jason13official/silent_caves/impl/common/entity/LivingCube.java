package io.github.jason13official.silent_caves.impl.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class LivingCube extends AbstractIdentifierMob {

  public LivingCube(EntityType<? extends LivingCube> entityType, Level level) {
    super(entityType, level);
  }

  public static AttributeSupplier.Builder createMobAttributes() {

    return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 48.0);
  }
}
