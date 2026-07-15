package io.github.jason13official.silent_caves.impl.common.entity.navigation;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;

/// pathfinds normally first; only falls back to a footprint sized to [#crawlWidth]x[#crawlHeight] (letting the mob route through tight gaps) when the normal-size path can't reach the target.
///
/// the fallback footprint is computed from real crawl dimensions (vanilla's own `floor(size + 1.0)` formula), not an arbitrary constant: forcing the footprint to `1` regardless of the mob's actual size under-scans wider mobs, and [WalkNodeEvaluator#tryJumpOn] skips its own extra real-width collision check whenever the mob's *current* bounding box is `>= 1` block wide (true for almost any mob that isn't already physically shrunk) -- so an under-scanned footprint can get nodes accepted around elevation changes (stairs, hills) with no real-geometry verification anywhere, producing waypoints the mob's real body can't actually occupy. We additionally verify the fallback path against a true crawl-sized [AABB] before trusting it, since the footprint scan alone isn't proof of fit.
public class CrawlCapableNavigation extends GroundPathNavigation {

  private final float crawlWidth;
  private final float crawlHeight;

  public CrawlCapableNavigation(Mob mob, Level level, float crawlWidth, float crawlHeight) {
    super(mob, level);
    this.crawlWidth = crawlWidth;
    this.crawlHeight = crawlHeight;
  }

  @Override
  protected PathFinder createPathFinder(int maxVisitedNodes) {
    WalkNodeEvaluator standardEvaluator = new WalkNodeEvaluator();
    standardEvaluator.setCanPassDoors(true);
    this.nodeEvaluator = standardEvaluator;

    CrawlAwareNodeEvaluator crawlEvaluator = new CrawlAwareNodeEvaluator(this.crawlWidth, this.crawlHeight);
    crawlEvaluator.setCanPassDoors(true);

    return new FallbackCrawlPathFinder(standardEvaluator, crawlEvaluator, maxVisitedNodes);
  }

  /// true only if a real [#crawlWidth]x[#crawlHeight] box clears every node in `path`; the node-evaluator footprint alone can't be trusted (see class doc).
  private boolean fitsCrawlBox(Path path) {
    double halfWidth = this.crawlWidth / 2.0;

    for (int i = 0; i < path.getNodeCount(); i++) {
      Node node = path.getNode(i);
      AABB box = new AABB(
          node.x + 0.5 - halfWidth, node.y, node.z + 0.5 - halfWidth,
          node.x + 0.5 + halfWidth, node.y + this.crawlHeight, node.z + 0.5 + halfWidth);
      if (!this.level.noCollision(this.mob, box)) {
        return false;
      }
    }

    return true;
  }

  /// wraps two independent [PathFinder]s (standard footprint, crawl footprint) so a tight-space route is only used when the standard one genuinely can't reach the target, and only after it's confirmed to physically fit.
  private final class FallbackCrawlPathFinder extends PathFinder {

    private final PathFinder crawlPathFinder;

    private FallbackCrawlPathFinder(WalkNodeEvaluator standardEvaluator, CrawlAwareNodeEvaluator crawlEvaluator, int maxVisitedNodes) {
      super(standardEvaluator, maxVisitedNodes);
      this.crawlPathFinder = new PathFinder(crawlEvaluator, maxVisitedNodes);
    }

    @Override
    public Path findPath(PathNavigationRegion region, Mob mob, Set<BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
      Path standardPath = super.findPath(region, mob, targetPositions, maxRange, accuracy, searchDepthMultiplier);
      if (standardPath != null && standardPath.canReach()) {
        return standardPath;
      }

      Path crawlPath = this.crawlPathFinder.findPath(region, mob, targetPositions, maxRange, accuracy, searchDepthMultiplier);
      if (crawlPath != null && crawlPath.canReach() && CrawlCapableNavigation.this.fitsCrawlBox(crawlPath)) {
        return crawlPath;
      }

      return standardPath != null ? standardPath : crawlPath;
    }
  }

  /// forces entityWidth/Height/Depth from the real crawl dimensions after the standard prepare() call, which otherwise sets them from the mob's full (standing) bounding box.
  private static final class CrawlAwareNodeEvaluator extends WalkNodeEvaluator {

    private final float crawlWidth;
    private final float crawlHeight;

    private CrawlAwareNodeEvaluator(float crawlWidth, float crawlHeight) {
      this.crawlWidth = crawlWidth;
      this.crawlHeight = crawlHeight;
    }

    @Override
    public void prepare(PathNavigationRegion region, Mob mob) {
      super.prepare(region, mob);
      this.entityWidth = Mth.floor(this.crawlWidth + 1.0F);
      this.entityHeight = Mth.floor(this.crawlHeight + 1.0F);
      this.entityDepth = this.entityWidth;
    }
  }
}
