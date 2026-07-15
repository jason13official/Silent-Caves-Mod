package io.github.jason13official.silent_caves.impl.common.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/// pathfinds using 1x1x1 block dimensions, so the golem can route through 1-block-tall/wide tunnels regardless of its standing bounding box size.
public class CrawlCapableNavigation extends GroundPathNavigation {

  public CrawlCapableNavigation(Mob mob, Level level) {
    super(mob, level);
  }

  @Override
  protected PathFinder createPathFinder(int maxVisitedNodes) {
    this.nodeEvaluator = new CrawlAwareNodeEvaluator();
    this.nodeEvaluator.setCanPassDoors(true);
    return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
  }

  /// forces entityWidth/Height/Depth to 1 after the standard prepare() call, which otherwise sets them from the mob's real (standing) bounding box.
  private static final class CrawlAwareNodeEvaluator extends WalkNodeEvaluator {

    @Override
    public void prepare(PathNavigationRegion region, Mob mob) {
      super.prepare(region, mob);
      this.entityWidth = 1;
      this.entityHeight = 1;
      this.entityDepth = 1;
    }
  }
}
