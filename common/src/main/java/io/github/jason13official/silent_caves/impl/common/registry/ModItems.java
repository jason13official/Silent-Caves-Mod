package io.github.jason13official.silent_caves.impl.common.registry;

import io.github.jason13official.silent_caves.SilentCaves;
import io.github.jason13official.silent_caves.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ModItems {

  public static Item MONSTER_CUBE_SPAWN_EGG;

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    MONSTER_CUBE_SPAWN_EGG = Services.PLATFORM.createSpawnEggItem(() -> ModEntities.MONSTER_CUBE, 0x000000, 0xf800f8, new Properties());
    consumer.accept(MONSTER_CUBE_SPAWN_EGG, SilentCaves.identifier("monster_cube_spawn_egg"));
  }
}
