package io.github.jason13official.silent_caves.impl.common.registry;

import io.github.jason13official.silent_caves.SilentCaves;
import io.github.jason13official.silent_caves.impl.common.entity.DeafeningGolem;
import io.github.jason13official.silent_caves.impl.common.entity.MonsterCube;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

  public static EntityType<MonsterCube> MONSTER_CUBE;
  public static EntityType<DeafeningGolem> DEAFENING_GOLEM;

  public static void register(BiConsumer<EntityType<?>, ResourceLocation> consumer) {

    ResourceLocation monsterCubeId = SilentCaves.identifier("monster_cube");
    MONSTER_CUBE = EntityType.Builder.of(MonsterCube::new, MobCategory.MONSTER)
        .sized(1f, 1f)
        .clientTrackingRange(16)
        .build(monsterCubeId.toString());
    consumer.accept(MONSTER_CUBE, monsterCubeId);

    ResourceLocation deafeningGolemId = SilentCaves.identifier("deafening_golem");
    DEAFENING_GOLEM = EntityType.Builder.of(DeafeningGolem::new, MobCategory.MONSTER)
        .sized(1.4F, 2.7F)
        .clientTrackingRange(16)
        .build(deafeningGolemId.toString());
    consumer.accept(DEAFENING_GOLEM, deafeningGolemId);
  }
}
