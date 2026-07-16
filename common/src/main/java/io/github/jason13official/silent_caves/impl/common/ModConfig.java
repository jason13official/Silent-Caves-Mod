package io.github.jason13official.silent_caves.impl.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModConfig {

  private static float spawnChance = 0.05f;
  public static final ConfigGetterSetter<Float> SPAWN_CHANCE = new ConfigGetterSetter<>("spawn_chance", () -> Math.clamp(spawnChance, 0.0f, 1.0f), f -> spawnChance = Math.clamp(spawnChance, 0.0f, 1.0f));

  public record ConfigGetterSetter<T>(String key, Supplier<T> getter, Consumer<T> setter) {
  }
}
