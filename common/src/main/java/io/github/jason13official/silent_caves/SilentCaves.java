package io.github.jason13official.silent_caves;

import io.github.jason13official.silent_caves.impl.common.entity.LivingCube;
import io.github.jason13official.silent_caves.impl.common.registry.ModEntities;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class SilentCaves {

  public static void init() {
  }

  public static ResourceLocation identifier(final String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  public static void registerEntityAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> consumer) {

    consumer.accept(ModEntities.LIVING_CUBE, LivingCube.createMobAttributes());
  }
}