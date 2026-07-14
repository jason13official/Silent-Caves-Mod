package io.github.jason13official.silent_caves.impl.common.registry;

import io.github.jason13official.silent_caves.SilentCaves;
import io.github.jason13official.silent_caves.impl.common.entity.LivingCube;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

  public static EntityType<LivingCube> LIVING_CUBE;

  public static void register(BiConsumer<EntityType<?>, ResourceLocation> consumer) {

    ResourceLocation livingCubeId = SilentCaves.identifier("living_cube");
    LIVING_CUBE = EntityType.Builder.of(LivingCube::new, MobCategory.MISC)
        .sized(1f, 1f)
        .clientTrackingRange(16)
        .build(livingCubeId.toString());

    consumer.accept(LIVING_CUBE, livingCubeId);
  }
}
