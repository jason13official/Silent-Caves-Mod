package io.github.jason13official.silent_caves.impl.common.entity.spawn;

import io.github.jason13official.silent_caves.Constants;
import io.github.jason13official.silent_caves.impl.common.ModConfig;
import io.github.jason13official.silent_caves.impl.common.entity.AbstractBlockIdMonster;
import io.github.jason13official.silent_caves.impl.common.entity.DeafeningGolem;
import io.github.jason13official.silent_caves.impl.common.registry.ModEntities;
import io.github.jason13official.silent_caves.platform.Services;
import java.util.Collections;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;

public final class DeafeningGolemSpawnRules {

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
    if (level.getRandom().nextFloat() >= ModConfig.SPAWN_CHANCE.getter().get()) {
      return false;
    }

    // if the collection of tags we can spawn on and the tags of block we're on contain similar elements (not disjointed)
    boolean shouldSpawn = !Collections.disjoint(AbstractBlockIdMonster.VALID_SPAWNS.get(), level.getBlockState(blockPos.below()).getTags().toList())
        || !Collections.disjoint(ModConfig.VALID_SPAWN_BLOCK_TAGS.getter().get(), level.getBlockState(blockPos.below()).getTags().toList());

    if (!shouldSpawn) {
      shouldSpawn = ModConfig.VALID_SPAWN_BLOCKS.getter().get().contains(level.getBlockState(blockPos.below()).getBlock());
    }

    if (shouldSpawn && Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info("Should spawn golem at {}", blockPos.toShortString());
    }

    return shouldSpawn;
  };

  private DeafeningGolemSpawnRules() {
  }
}
