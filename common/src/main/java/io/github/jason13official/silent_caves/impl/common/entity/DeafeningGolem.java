package io.github.jason13official.silent_caves.impl.common.entity;

import io.github.jason13official.silent_caves.api.client.sound.SoundSuppression;
import io.github.jason13official.silent_caves.impl.client.animation.DeafeningGolemAnimations;
import io.github.jason13official.silent_caves.impl.common.ModConfig;
import io.github.jason13official.silent_caves.impl.common.entity.crawl.CrawlPoseController;
import io.github.jason13official.silent_caves.impl.common.entity.navigation.CrawlCapableNavigation;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public class DeafeningGolem extends AbstractDeafeningBlockIdMonster {

  private static final byte EVENT_ATTACK_FLING = 64;
  private static final byte EVENT_ATTACK_SWIPE_RIGHT = 65;
  private static final byte EVENT_HEAR_NO_EVIL = 66;
  private static final byte FLAG_IS_AGGRESSIVE = 0x01;
  private static final byte FLAG_IS_CRAWLING = 0x02;

  /// matches the animation length of [DeafeningGolemAnimations#HEAR_NO_EVIL]; navigation is paused for the duration so the golem doesn't walk mid-animation.
  private static final int HEAR_NO_EVIL_FREEZE_TICKS = 80;
  private int hearNoEvilFreezeTicks;

  private static final String GOLEM_FLAGS = "GolemFlags";

  private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(DeafeningGolem.class, EntityDataSerializers.BYTE);

  private final CrawlPoseController crawlPoseController = new CrawlPoseController(this);

  public final AnimationState IDLE_ANIM_STATE = new AnimationState();
  public final AnimationState WALK_ANIM_STATE = new AnimationState();
  public final AnimationState SPRINT_ANIM_STATE = new AnimationState();
  public final AnimationState ATTACK_FLING_ANIM_STATE = new AnimationState();
  public final AnimationState ATTACK_SWIPE_RIGHT_ANIM_STATE = new AnimationState();
  public final AnimationState CRAWL_ANIM_STATE = new AnimationState();
  public final AnimationState HEAR_NO_EVIL_ANIM_STATE = new AnimationState();

  public DeafeningGolem(EntityType<? extends AbstractDeafeningBlockIdMonster> entityType, Level level) {
    super(entityType, level);

    this.setPathfindingMalus(PathType.WATER, 0.0F);
  }

  public static AttributeSupplier.Builder createMobAttributes() {

    return LivingEntity.createLivingAttributes()
        .add(Attributes.MAX_HEALTH, ModConfig.MAX_HEALTH.getter().get())
        .add(Attributes.MOVEMENT_SPEED, ModConfig.MOVEMENT_SPEED.getter().get())
        .add(Attributes.ATTACK_KNOCKBACK, ModConfig.ATTACK_KNOCKBACK.getter().get())
        .add(Attributes.KNOCKBACK_RESISTANCE, ModConfig.KNOCKBACK_RESISTANCE.getter().get())
        .add(Attributes.ATTACK_DAMAGE, ModConfig.ATTACK_DAMAGE.getter().get())
        .add(Attributes.FOLLOW_RANGE, ModConfig.FOLLOW_RANGE.getter().get())
        .add(Attributes.STEP_HEIGHT, ModConfig.STEP_HEIGHT.getter().get())
        .add(Attributes.WATER_MOVEMENT_EFFICIENCY, ModConfig.WATER_MOVEMENT_EFFICIENCY.getter().get());
  }

  /// stops fluid currents from moving the golem
  @Override
  public boolean isPushedByFluid() {

    return false;
  }

  @Override
  protected void defineSynchedData(Builder builder) {
    super.defineSynchedData(builder);

    builder.define(DATA_ID_FLAGS, (byte) 0);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);

    compound.putByte(GOLEM_FLAGS, this.entityData.get(DATA_ID_FLAGS));
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);

    if (compound.contains(GOLEM_FLAGS)) {
      this.entityData.set(DATA_ID_FLAGS, compound.getByte(GOLEM_FLAGS));
    }
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level() instanceof ServerLevel level) {

      // every half of a second
      if (level.getGameTime() % 10 == 0) {
        // target nearest player
        Player p = level.getNearestPlayer(this, 16.0D);
        if (p != null) {
          this.setTarget(p);
        }
      }

      if (this.hearNoEvilFreezeTicks > 0) {
        this.hearNoEvilFreezeTicks--;
      }

      this.setFlag(FLAG_IS_AGGRESSIVE, this.getTarget() != null);

      boolean crawling = this.crawlPoseController.tick(this.wouldNotSuffocateAtTargetPose(Pose.STANDING));
      this.setFlag(FLAG_IS_CRAWLING, crawling);
      this.setPose(crawling ? Pose.CROUCHING : Pose.STANDING);
    }

    this.setupAnimationStates();
  }

  /// shrink the hitbox while crawling; note the [Pose] param, not the crawl flag directly; [#wouldNotSuffocateAtTargetPose] probes [Pose#STANDING] explicitly to decide when it's safe to stand back up, and that must keep resolving to the real full-size box regardless of current pose.
  @Override
  protected EntityDimensions getDefaultDimensions(Pose pose) {
    return pose == Pose.CROUCHING ? CrawlPoseController.CRAWL_DIMENSIONS : super.getDefaultDimensions(pose);
  }

  @Override
  protected PathNavigation createNavigation(Level level) {

    return new CrawlCapableNavigation(this, level, CrawlPoseController.CRAWL_WIDTH, CrawlPoseController.CRAWL_HEIGHT);
  }

  @Override
  protected void registerGoals() {

    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, true));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 24.0F));
    this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, other -> mayTargetForAttacking(this, other)));
  }

  @Override
  public void setTarget(@Nullable LivingEntity target) {

    // skip setting target if it's a golem that spawned on the same kind of block that we did
    if (target instanceof DeafeningGolem other) {
      if (this.getSpawnedOnBlock() == other.getSpawnedOnBlock()) {
        return;
      }
    }

    super.setTarget(target);
  }

  public static boolean mayTargetForAttacking(DeafeningGolem self, LivingEntity other) {

    return true;

    // allow targeting if not a golem, or is golem of different block
    // return !(other instanceof DeafeningGolem golem); // || self.getSpawnedOnBlock() != golem.getSpawnedOnBlock();
  }

  @Override
  public boolean killedEntity(ServerLevel level, LivingEntity entity) {
    // TODO circle back to this lol
//    if (entity instanceof Player) {
//      System.out.println("golem killed player");
//      this.level().broadcastEntityEvent(this, EVENT_HEAR_NO_EVIL);
//      this.hearNoEvilFreezeTicks = HEAR_NO_EVIL_FREEZE_TICKS;
//    }
    return super.killedEntity(level, entity);
  }

  /// zeroed out while [DeafeningGolem#hearNoEvilFreezeTicks] is active so [LivingEntity#travel] doesn't move the entity
  @Override
  public float getSpeed() {

    return this.hearNoEvilFreezeTicks > 0 ? 0.0F : super.getSpeed();
  }

  @Override
  protected boolean isNavigationPaused() {

    return this.hearNoEvilFreezeTicks > 0;
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    boolean result = super.doHurtTarget(target);
    if (result) {
      this.level().broadcastEntityEvent(this, this.random.nextBoolean() ? EVENT_ATTACK_FLING : EVENT_ATTACK_SWIPE_RIGHT);
    }

    if ( target instanceof Player player && player.getHealth() <= 0.00005f) {
      System.out.println("golem killed player ?");
      this.level().broadcastEntityEvent(this, EVENT_HEAR_NO_EVIL);
      this.hearNoEvilFreezeTicks = HEAR_NO_EVIL_FREEZE_TICKS;
    }

    return result;
  }

  @Override
  public void handleEntityEvent(byte id) {
    if (id == EVENT_ATTACK_FLING) {
      this.ATTACK_FLING_ANIM_STATE.start(this.tickCount);
    } else if (id == EVENT_ATTACK_SWIPE_RIGHT) {
      this.ATTACK_SWIPE_RIGHT_ANIM_STATE.start(this.tickCount);
    } else if (id == EVENT_HEAR_NO_EVIL) {

      if (this.level() instanceof ClientLevel level) {
        laugh(level, this.blockPosition());
      }

      this.HEAR_NO_EVIL_ANIM_STATE.start(this.tickCount);
    } else {
      super.handleEntityEvent(id);
    }
  }

  private static void laugh(ClientLevel level, BlockPos blockPos) {
    SoundSuppression.restoreClientVolumes(Minecraft.getInstance());
    if (Minecraft.getInstance().player != null) {
      level.playSound(Minecraft.getInstance().player, blockPos, SoundEvents.WITCH_CELEBRATE, SoundSource.PLAYERS, 1.0f, level.getRandom().nextBoolean() ? 0.2f : 0.5f);
      Minecraft.getInstance().player.sendSystemMessage(Component.translatable("text.silent_caves.laugh"));
    }
  }

  private void setupAnimationStates() {
    boolean moving = this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    boolean aggressive = this.getFlag(FLAG_IS_AGGRESSIVE);
    boolean crawling = this.getFlag(FLAG_IS_CRAWLING);

    if (crawling) {
      this.CRAWL_ANIM_STATE.startIfStopped(this.tickCount);
      this.IDLE_ANIM_STATE.stop();
      this.WALK_ANIM_STATE.stop();
      this.SPRINT_ANIM_STATE.stop();
    } else if (moving && aggressive) {
      this.SPRINT_ANIM_STATE.startIfStopped(this.tickCount);
      this.IDLE_ANIM_STATE.stop();
      this.WALK_ANIM_STATE.stop();
      this.CRAWL_ANIM_STATE.stop();
    } else if (moving) {
      this.WALK_ANIM_STATE.startIfStopped(this.tickCount);
      this.IDLE_ANIM_STATE.stop();
      this.SPRINT_ANIM_STATE.stop();
      this.CRAWL_ANIM_STATE.stop();
    } else {
      this.IDLE_ANIM_STATE.startIfStopped(this.tickCount);
      this.WALK_ANIM_STATE.stop();
      this.SPRINT_ANIM_STATE.stop();
      this.CRAWL_ANIM_STATE.stop();
    }
  }

  private boolean getFlag(byte flag) {
    return (this.entityData.get(DATA_ID_FLAGS) & flag) != 0;
  }

  private void setFlag(byte flag, boolean value) {
    byte current = this.entityData.get(DATA_ID_FLAGS);
    this.entityData.set(DATA_ID_FLAGS, value ? (byte) (current | flag) : (byte) (current & ~flag));
  }
}
