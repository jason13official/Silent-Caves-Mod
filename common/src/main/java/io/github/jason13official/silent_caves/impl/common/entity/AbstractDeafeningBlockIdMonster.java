package io.github.jason13official.silent_caves.impl.common.entity;

import io.github.jason13official.silent_caves.api.client.sound.SoundSuppression;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractDeafeningBlockIdMonster extends AbstractBlockIdMonster {

  /// ticks (1 second) without a block-position change before the mob is assumed wedged (e.g. the intermittent stuck-spin pathing bug on stairs/hilly terrain) and given a fresh destination. This is a recovery net, not a fix for the underlying pathing cause.
  private static final int STUCK_TICKS_THRESHOLD = 20;
  private static final TargetingConditions UNSTICK_TARGETING_CONDITIONS = TargetingConditions.forCombat().range(16.0).ignoreLineOfSight();

  private BlockPos lastStuckCheckPos;
  private int stuckTicks;

  public AbstractDeafeningBlockIdMonster(EntityType<? extends AbstractDeafeningBlockIdMonster> entityType, Level level) {
    super(entityType, level);
  }

  /// stops drowning damage
  @Override
  protected int decreaseAirSupply(int currentAir) {

    return currentAir;
  }

  /// note: usage of [SoundSuppression] to quiet sounds for nearby players
  @Override
  public void tick() {
    super.tick();

    if (this.level().isClientSide()) {
      SoundSuppression.adjustClientVolumeLevels(Minecraft.getInstance(), this);
    } else {
      this.detectAndRecoverFromStuck();
    }
  }

  /// subclasses may intentionally hold the mob still, so we need to recover
  protected boolean isNavigationPaused() {

    return false;
  }

  private void detectAndRecoverFromStuck() {
    if (this.isNavigationPaused()) {
      this.lastStuckCheckPos = this.blockPosition();
      this.stuckTicks = 0;
      return;
    }

    BlockPos pos = this.blockPosition();
    if (pos.equals(this.lastStuckCheckPos)) {
      this.stuckTicks++;
    } else {
      this.lastStuckCheckPos = pos;
      this.stuckTicks = 0;
    }

    if (this.stuckTicks < STUCK_TICKS_THRESHOLD) {
      return;
    }

    this.stuckTicks = 0;
    this.navigation.stop();

    LivingEntity nearby = this.level().getNearestEntity(LivingEntity.class, UNSTICK_TARGETING_CONDITIONS, this,
        this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(16.0));

    if (nearby != null) {
      this.setTarget(nearby);
      this.navigation.moveTo(nearby, 1.0);
      return;
    }

    Vec3 randomTarget = DefaultRandomPos.getPos(this, 10, 7);
    if (randomTarget != null) {
      this.navigation.moveTo(randomTarget.x, randomTarget.y, randomTarget.z, 1.0);
    }
  }

  /// note: usage of [SoundSuppression] to undo changes to client sound on entity removal
  @Override
  public void remove(RemovalReason reason) {
    if (this.level().isClientSide()) {
      SoundSuppression.restoreClientVolumes(Minecraft.getInstance());
    }
    super.remove(reason);
  }

  /// note: usage of [SoundSuppression] to undo changes to client sound on entity death
  @Override
  public void die(DamageSource damageSource) {
    if (this.level().isClientSide()) {
      SoundSuppression.restoreClientVolumes(Minecraft.getInstance());
    }
    super.die(damageSource);
  }

  /// note: usage of [SoundSuppression] to undo changes to client sound when entity too far
  @Override
  public boolean removeWhenFarAway(double distanceToClosestPlayer) {
    if (this.level().isClientSide()) {
      SoundSuppression.restoreClientVolumes(Minecraft.getInstance());
    }
    return super.removeWhenFarAway(distanceToClosestPlayer);
  }

  /// note: usage of [SoundSuppression] to undo changes to client sound on entity dimension change
  @Override
  protected void removeAfterChangingDimensions() {
    if (this.level().isClientSide()) {
      SoundSuppression.restoreClientVolumes(Minecraft.getInstance());
    }
    super.removeAfterChangingDimensions();
  }
}
