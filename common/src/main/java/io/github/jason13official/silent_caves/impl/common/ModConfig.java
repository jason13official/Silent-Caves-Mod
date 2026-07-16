package io.github.jason13official.silent_caves.impl.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModConfig {

  private static float spawnChance = 0.05f;

  private static double maxHealth = 200.0D;
  private static double movementSpeed = 0.25D;
  private static double knockbackResistance = 1.0D;
  private static double attackDamage = 15.0D;
  private static double followRange = 32.0D;
  private static double stepHeight = 1.2D;
  private static double waterMovementEfficiency = 0.85D;

  public static final ConfigGetterSetter<Float> SPAWN_CHANCE = new ConfigGetterSetter<>("spawn_chance", () -> Math.clamp(spawnChance, 0.0f, 1.0f), f -> spawnChance = Math.clamp(spawnChance, 0.0f, 1.0f));
  
  public static final ConfigGetterSetter<Double> MAX_HEALTH = new ConfigGetterSetter<>("max_health", () -> maxHealth, D -> maxHealth = D);
  public static final ConfigGetterSetter<Double> MOVEMENT_SPEED = new ConfigGetterSetter<>("movement_speed", () -> movementSpeed, D -> movementSpeed = D);
  public static final ConfigGetterSetter<Double> KNOCKBACK_RESISTANCE = new ConfigGetterSetter<>("knockback_resistance", () -> knockbackResistance, D -> knockbackResistance = D);
  public static final ConfigGetterSetter<Double> ATTACK_DAMAGE = new ConfigGetterSetter<>("attack_damage", () -> attackDamage, D -> attackDamage = D);
  public static final ConfigGetterSetter<Double> FOLLOW_RANGE = new ConfigGetterSetter<>("follow_range", () -> followRange, D -> followRange = D);
  public static final ConfigGetterSetter<Double> STEP_HEIGHT = new ConfigGetterSetter<>("step_height", () -> stepHeight, D -> stepHeight = D);
  public static final ConfigGetterSetter<Double> WATER_MOVEMENT_EFFICIENCY = new ConfigGetterSetter<>("water_movement_efficiency", () -> waterMovementEfficiency, D -> waterMovementEfficiency = D);

  public record ConfigGetterSetter<T>(String key, Supplier<T> getter, Consumer<T> setter) {
  }
}
