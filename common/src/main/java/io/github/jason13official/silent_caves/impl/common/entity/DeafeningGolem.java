package io.github.jason13official.silent_caves.impl.common.entity;

import io.github.jason13official.silent_caves.Constants;
import io.github.jason13official.silent_caves.api.common.entity.IBlockIdHolder;
import io.github.jason13official.silent_caves.impl.client.animation.DeafeningGolemAnimations;
import io.github.jason13official.silent_caves.impl.common.entity.navigation.CrawlCapableNavigation;
import io.github.jason13official.silent_caves.impl.common.registry.ModEntities;
import io.github.jason13official.silent_caves.platform.Services;
import java.util.Collections;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

public class DeafeningGolem extends AbstractDeafeningBlockIdMonster {

  public static final SpawnPlacements.SpawnPredicate<DeafeningGolem> SPAWN_PREDICATE = (entityType, level, mobSpawnType, blockPos, randomSource) -> {

    // standard monster spawning
    if (!Monster.checkMonsterSpawnRules(ModEntities.DEAFENING_GOLEM, level, mobSpawnType, blockPos, randomSource)) {
      return false;
    }

    // must be underground
    if (level.canSeeSky(blockPos)) {
      return false;
    }

    // 5% chance to spawn naturally
    if (level.getRandom().nextFloat() >= 0.05f) {
      return false;
    }

    // if the collection of tags we can spawn on and tags of block we're on contain similar elements (ARE jointed)
    boolean shouldSpawn = !Collections.disjoint(
        AbstractBlockIdMonster.VALID_SPAWNS.get(),
        level.getBlockState(blockPos.below()).getTags().toList()
    );

    if (!shouldSpawn) {
      return false;
    }

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info("Should spawn golem at {}", blockPos.toShortString());
    }

    return true;
  };

  private static final byte EVENT_ATTACK_FLING = 64;
  private static final byte EVENT_ATTACK_SWIPE_RIGHT = 65;
  private static final byte EVENT_HEAR_NO_EVIL = 66;
  private static final byte FLAG_IS_AGGRESSIVE = 0x01;
  private static final byte FLAG_IS_CRAWLING = 0x02;

  /// patch fix for not crawling everywhere
  private static final int STAND_UP_DELAY_TICKS = 10;
  private int ticksSinceCrawlNeeded;

  /// matches the animation length of [DeafeningGolemAnimations#HEAR_NO_EVIL]; navigation is paused for the duration so the golem doesn't walk mid-animation.
  private static final int HEAR_NO_EVIL_FREEZE_TICKS = 80;
  private int hearNoEvilFreezeTicks;

  private static final String GOLEM_FLAGS = "GolemFlags";

  private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(DeafeningGolem.class, EntityDataSerializers.BYTE);

  /// smaller hitbox used while crawling through tight spaces, so the golem doesn't suffocate; the visual model is unchanged, only collision.
  private static final float CRAWL_WIDTH = 0.7F;
  private static final float CRAWL_HEIGHT = 0.75F;
  private static final EntityDimensions CRAWL_DIMENSIONS = EntityDimensions.scalable(CRAWL_WIDTH, CRAWL_HEIGHT);

  public final AnimationState IDLE_ANIM_STATE = new AnimationState();
  public final AnimationState WALK_ANIM_STATE = new AnimationState();
  public final AnimationState SPRINT_ANIM_STATE = new AnimationState();
  public final AnimationState ATTACK_FLING_ANIM_STATE = new AnimationState();
  public final AnimationState ATTACK_SWIPE_RIGHT_ANIM_STATE = new AnimationState();
  public final AnimationState CRAWL_ANIM_STATE = new AnimationState();
  public final AnimationState HEAR_NO_EVIL_ANIM_STATE = new AnimationState();

  public DeafeningGolem(EntityType<? extends AbstractDeafeningBlockIdMonster> entityType, Level level) {
    super(entityType, level);
  }

  public static AttributeSupplier.Builder createMobAttributes() {

    return LivingEntity.createLivingAttributes()
        .add(Attributes.MAX_HEALTH, 200.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.ATTACK_DAMAGE, 15.0D)
        .add(Attributes.FOLLOW_RANGE, 32.0D)
        .add(Attributes.STEP_HEIGHT, 1.2D);
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

    if (!this.level().isClientSide()) {
      if (this.hearNoEvilFreezeTicks > 0) {
        this.hearNoEvilFreezeTicks--;
      }

      this.setFlag(FLAG_IS_AGGRESSIVE, this.getTarget() != null);

      boolean needsToCrawl = !this.wouldNotSuffocateAtTargetPose(Pose.STANDING) || this.pathLeadsThroughTightSpace();
      this.ticksSinceCrawlNeeded = needsToCrawl ? 0 : this.ticksSinceCrawlNeeded + 1;

      boolean crawling = needsToCrawl || this.ticksSinceCrawlNeeded < STAND_UP_DELAY_TICKS;
      this.setFlag(FLAG_IS_CRAWLING, crawling);
      this.setPose(crawling ? Pose.CROUCHING : Pose.STANDING);
    }

    this.setupAnimationStates();
  }

  /// shrink the hitbox while crawling; note the [Pose] param, not the crawl flag directly; [#wouldNotSuffocateAtTargetPose] probes [Pose#STANDING] explicitly to decide when it's safe to stand back up, and that must keep resolving to the real full-size box regardless of current pose.
  @Override
  protected EntityDimensions getDefaultDimensions(Pose pose) {
    return pose == Pose.CROUCHING ? CRAWL_DIMENSIONS : super.getDefaultDimensions(pose);
  }

  /// true when solid blocks occupy the space between crawl height and standing height at the given position, i.e. the golem can't stand upright there.
  private boolean hasBlockedOverhead(double x, double y, double z) {
    AABB overhead = new AABB(x - CRAWL_WIDTH / 2.0, y + CRAWL_HEIGHT, z - CRAWL_WIDTH / 2.0, x + CRAWL_WIDTH / 2.0, y + this.getType().getHeight() + 1.0, z + CRAWL_WIDTH / 2.0);
    return !this.level().noCollision(this, overhead);
  }

  /// true when any of the next few nodes on the active path require crawling, so the golem starts crawling before it physically reaches the tight space instead of bumping into the entrance.
  private boolean pathLeadsThroughTightSpace() {
    Path path = this.navigation.getPath();
    if (path == null || path.isDone()) {
      return false;
    }

    int next = path.getNextNodeIndex();
    // int limit = Math.min(next + 5, path.getNodeCount());
    int limit = Math.min(next + 2, path.getNodeCount());
    for (int i = next; i < limit; i++) {
      Node node = path.getNode(i);
      if (this.hasBlockedOverhead(node.x + 0.5, node.y, node.z + 0.5)) {
        return true;
      }
    }

    return false;
  }

  @Override
  protected PathNavigation createNavigation(Level level) {

    return new CrawlCapableNavigation(this, level, CRAWL_WIDTH, CRAWL_HEIGHT);
  }

  @Override
  protected void registerGoals() {

    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, true));
    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 48.0F));
    this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, living -> {

      if (living instanceof DeafeningGolem golem) {
        return this.getSpawnedOnBlock() != golem.getSpawnedOnBlock();
      }

      return true;
    }));
  }

  @Override
  public boolean killedEntity(ServerLevel level, LivingEntity entity) {
    this.level().broadcastEntityEvent(this, EVENT_HEAR_NO_EVIL);
    this.hearNoEvilFreezeTicks = HEAR_NO_EVIL_FREEZE_TICKS;
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
    return result;
  }

  @Override
  public void handleEntityEvent(byte id) {
    if (id == EVENT_ATTACK_FLING) {
      this.ATTACK_FLING_ANIM_STATE.start(this.tickCount);
    } else if (id == EVENT_ATTACK_SWIPE_RIGHT) {
      this.ATTACK_SWIPE_RIGHT_ANIM_STATE.start(this.tickCount);
    } else if (id == EVENT_HEAR_NO_EVIL) {
      this.HEAR_NO_EVIL_ANIM_STATE.start(this.tickCount);
    } else {
      super.handleEntityEvent(id);
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
