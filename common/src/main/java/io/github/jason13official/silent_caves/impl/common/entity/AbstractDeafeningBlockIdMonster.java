package io.github.jason13official.silent_caves.impl.common.entity;

import io.github.jason13official.silent_caves.api.client.sound.SoundSuppression;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class AbstractDeafeningBlockIdMonster extends AbstractBlockIdMonster {

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
    }
  }

  /// note: usage of [SoundSuppression] to undo changes to client sound on entity killed
  @Override
  public boolean killedEntity(ServerLevel level, LivingEntity entity) {
    if (this.level().isClientSide()) {
      SoundSuppression.restoreClientVolumes(Minecraft.getInstance());
    }
    return super.killedEntity(level, entity);
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
