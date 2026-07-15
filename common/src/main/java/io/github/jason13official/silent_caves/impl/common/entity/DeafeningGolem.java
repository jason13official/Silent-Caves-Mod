package io.github.jason13official.silent_caves.impl.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class DeafeningGolem extends AbstractDeafeningBlockIdMonster {

  public DeafeningGolem(EntityType<? extends AbstractDeafeningBlockIdMonster> entityType, Level level) {
    super(entityType, level);
  }

  public static AttributeSupplier.Builder createMobAttributes() {

    return LivingEntity.createLivingAttributes()
        .add(Attributes.MAX_HEALTH, 200.0F)
        .add(Attributes.MOVEMENT_SPEED, 0.25F)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
        .add(Attributes.ATTACK_DAMAGE, 15.0F)
        .add(Attributes.FOLLOW_RANGE, 48.0)
        .add(Attributes.STEP_HEIGHT, 1.2);
  }
}
