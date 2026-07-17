package io.github.jason13official.silent_caves.impl.common;

import io.github.jason13official.silent_caves.impl.common.entity.AbstractBlockIdMonster;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModConfig {

  private static float spawnChance = 0.05f;

  private static double maxHealth = 200.0D;
  private static double movementSpeed = 0.25D;
  private static double attackKnockback = 2.0D;
  private static double knockbackResistance = 1.0D;
  private static double attackDamage = 15.0D;
  private static double followRange = 32.0D;
  private static double stepHeight = 1.2D;
  private static double waterMovementEfficiency = 0.85D;

  private static List<TagKey<Block>> validSpawnBlockTags = AbstractBlockIdMonster.VALID_SPAWNS.get();
  private static List<Block> validSpawnBlocks = new ArrayList<>();

  public static final ConfigGetterSetter<Float> SPAWN_CHANCE = new ConfigGetterSetter<>("spawn_chance", () -> Math.clamp(spawnChance, 0.0f, 1.0f), f -> spawnChance = Math.clamp(spawnChance, 0.0f, 1.0f));
  
  public static final ConfigGetterSetter<Double> MAX_HEALTH = new ConfigGetterSetter<>("max_health", () -> maxHealth, D -> maxHealth = D);
  public static final ConfigGetterSetter<Double> MOVEMENT_SPEED = new ConfigGetterSetter<>("movement_speed", () -> movementSpeed, D -> movementSpeed = D);
  public static final ConfigGetterSetter<Double> ATTACK_KNOCKBACK = new ConfigGetterSetter<>("attack_knockback", () -> attackKnockback, D -> attackKnockback = D);
  public static final ConfigGetterSetter<Double> KNOCKBACK_RESISTANCE = new ConfigGetterSetter<>("knockback_resistance", () -> knockbackResistance, D -> knockbackResistance = D);
  public static final ConfigGetterSetter<Double> ATTACK_DAMAGE = new ConfigGetterSetter<>("attack_damage", () -> attackDamage, D -> attackDamage = D);
  public static final ConfigGetterSetter<Double> FOLLOW_RANGE = new ConfigGetterSetter<>("follow_range", () -> followRange, D -> followRange = D);
  public static final ConfigGetterSetter<Double> STEP_HEIGHT = new ConfigGetterSetter<>("step_height", () -> stepHeight, D -> stepHeight = D);
  public static final ConfigGetterSetter<Double> WATER_MOVEMENT_EFFICIENCY = new ConfigGetterSetter<>("water_movement_efficiency", () -> waterMovementEfficiency, D -> waterMovementEfficiency = D);

  /// gets the current value, or clears current list and overwrites with given list instead
  public static final ConfigGetterSetter<List<TagKey<Block>>> VALID_SPAWN_BLOCK_TAGS = new ConfigGetterSetter<>("valid_spawn_block_tags", () -> validSpawnBlockTags, list -> {

    // setter logic

    validSpawnBlockTags.clear();

    // add if they are not in the list
    for (TagKey<Block> blockTagKey : list) {
      if (!validSpawnBlockTags.contains(blockTagKey)) {
        validSpawnBlockTags.add(blockTagKey);
      }
    }
  });

  public static final ConfigGetterSetter<List<Block>> VALID_SPAWN_BLOCKS = new ConfigGetterSetter<>("valid_spawn_blocks", () -> validSpawnBlocks, list -> {

    validSpawnBlocks.clear();

    for (Block block : list) {
      if (!validSpawnBlocks.contains(block)) {
        validSpawnBlocks.add(block);
      }
    }
  });

  public record ConfigGetterSetter<T>(String key, Supplier<T> getter, Consumer<T> setter) {
  }
}
