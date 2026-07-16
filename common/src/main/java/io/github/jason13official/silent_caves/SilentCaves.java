package io.github.jason13official.silent_caves;

import io.github.jason13official.silent_caves.impl.common.ModConfigIO;
import io.github.jason13official.silent_caves.impl.common.entity.DeafeningGolem;
import io.github.jason13official.silent_caves.impl.common.entity.MonsterCube;
import io.github.jason13official.silent_caves.impl.common.registry.ModEntities;
import io.github.jason13official.silent_caves.platform.Services;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.Block;

public class SilentCaves {

  public static void init() {

    ModConfigIO.load(Services.PLATFORM.getConfigDirectory());
  }

  public static ResourceLocation identifier(final String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  public static void registerEntityAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> consumer) {

    consumer.accept(ModEntities.MONSTER_CUBE, MonsterCube.createMobAttributes());
    consumer.accept(ModEntities.DEAFENING_GOLEM, DeafeningGolem.createMobAttributes());
  }
}