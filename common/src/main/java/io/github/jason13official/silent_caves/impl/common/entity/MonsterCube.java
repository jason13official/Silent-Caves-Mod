package io.github.jason13official.silent_caves.impl.common.entity;

import io.github.jason13official.silent_caves.impl.common.entity.navigation.CrawlCapableNavigation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;

public class MonsterCube extends AbstractDeafeningBlockIdMonster {

  public MonsterCube(EntityType<? extends MonsterCube> entityType, Level level) {
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

  @Override
  protected PathNavigation createNavigation(Level level) {

    // no hitbox-shrink logic here yet (unlike DeafeningGolem), so pass this mob's real
    // standing size -- the crawl-footprint fallback then matches the standard footprint
    // exactly and never grants pathfinding leniency the real body can't back up.
    return new CrawlCapableNavigation(this, level, 1.0F, 1.0F);
  }

  @Override
  protected void registerGoals() {

    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, true));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 48.0F));
    this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, living -> {

      if (living instanceof MonsterCube cube) {
        return this.getSpawnedOnBlock() != cube.getSpawnedOnBlock();
      }

      return true;
    }));
  }
}
