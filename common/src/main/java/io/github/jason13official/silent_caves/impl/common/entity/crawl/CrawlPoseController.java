package io.github.jason13official.silent_caves.impl.common.entity.crawl;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

/// tracks the crawl/stand hysteresis for a mob squeezing through tight spaces: shrinks to [#CRAWL_WIDTH]x[#CRAWL_HEIGHT] on demand and holds the crouch for [#STAND_UP_DELAY_TICKS] after the need passes, so it doesn't flicker pose every tick along an uneven ceiling.
///
/// the "would not suffocate standing" check itself stays with the caller: [net.minecraft.world.entity.LivingEntity#wouldNotSuffocateAtTargetPose] is protected, so only the owning entity can call it.
public class CrawlPoseController {

  private static final int STAND_UP_DELAY_TICKS = 10;

  public static final float CRAWL_WIDTH = 0.7F;
  public static final float CRAWL_HEIGHT = 0.75F;
  public static final EntityDimensions CRAWL_DIMENSIONS = EntityDimensions.scalable(CRAWL_WIDTH, CRAWL_HEIGHT);

  private final Mob owner;
  private int ticksSinceCrawlNeeded;

  public CrawlPoseController(Mob owner) {
    this.owner = owner;
  }

  /// call once per server tick; returns whether the owner should be crouched/crawling this tick.
  public boolean tick(boolean canStandUpright) {
    boolean needsToCrawl = !canStandUpright || this.pathLeadsThroughTightSpace();
    this.ticksSinceCrawlNeeded = needsToCrawl ? 0 : this.ticksSinceCrawlNeeded + 1;

    return needsToCrawl || this.ticksSinceCrawlNeeded < STAND_UP_DELAY_TICKS;
  }

  /// true when solid blocks occupy the space between crawl height and standing height at the given position, i.e. the owner can't stand upright there.
  private boolean hasBlockedOverhead(double x, double y, double z) {
    AABB overhead = new AABB(x - CRAWL_WIDTH / 2.0, y + CRAWL_HEIGHT, z - CRAWL_WIDTH / 2.0, x + CRAWL_WIDTH / 2.0, y + this.owner.getType().getHeight() + 1.0, z + CRAWL_WIDTH / 2.0);
    return !this.owner.level().noCollision(this.owner, overhead);
  }

  /// true when any of the next few nodes on the active path require crawling, so the owner starts crawling before it physically reaches the tight space instead of bumping into the entrance.
  private boolean pathLeadsThroughTightSpace() {
    Path path = this.owner.getNavigation().getPath();
    if (path == null || path.isDone()) {
      return false;
    }

    int next = path.getNextNodeIndex();
    int limit = Math.min(next + 2, path.getNodeCount());
    for (int i = next; i < limit; i++) {
      Node node = path.getNode(i);
      if (this.hasBlockedOverhead(node.x + 0.5, node.y, node.z + 0.5)) {
        return true;
      }
    }

    return false;
  }
}
