package io.github.jason13official.silent_caves.impl.common;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.silent_caves.Constants;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfigIO {

  public static void load(Path configDir) {

    File configDirectory = new File(configDir.toUri());
    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {
      Constants.LOG.info("failed to get or create config directory");
      return;
    }

    Config.setInsertionOrderPreserved(true);
    loadConfig(configDir, Constants.MOD_ID + "-server.toml");
  }

  private static void loadConfig(Path configDir, String filename) {

    Path configFilepath = configDir.resolve(filename);
    File configFile = new File(configFilepath.toUri());

    try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).build()) {

      if (Files.exists(configFilepath)) {
        config.load();
      }

      // getters (loading from config file)
      ModConfig.SPAWN_CHANCE.setter().accept(Float.parseFloat(config.getOrElse(ModConfig.SPAWN_CHANCE.key(), String.valueOf(ModConfig.SPAWN_CHANCE.getter().get().floatValue()))));

      ModConfig.MAX_HEALTH.setter().accept(Double.parseDouble(config.getOrElse(ModConfig.MAX_HEALTH.key(), String.valueOf(ModConfig.MAX_HEALTH.getter().get().doubleValue()))));
      ModConfig.MOVEMENT_SPEED.setter().accept(Double.parseDouble(config.getOrElse(ModConfig.MOVEMENT_SPEED.key(), String.valueOf(ModConfig.MOVEMENT_SPEED.getter().get().doubleValue()))));
      ModConfig.KNOCKBACK_RESISTANCE.setter().accept(Double.parseDouble(config.getOrElse(ModConfig.KNOCKBACK_RESISTANCE.key(), String.valueOf(ModConfig.KNOCKBACK_RESISTANCE.getter().get().doubleValue()))));
      ModConfig.ATTACK_DAMAGE.setter().accept(Double.parseDouble(config.getOrElse(ModConfig.ATTACK_DAMAGE.key(), String.valueOf(ModConfig.ATTACK_DAMAGE.getter().get().doubleValue()))));
      ModConfig.FOLLOW_RANGE.setter().accept(Double.parseDouble(config.getOrElse(ModConfig.FOLLOW_RANGE.key(), String.valueOf(ModConfig.FOLLOW_RANGE.getter().get().doubleValue()))));
      ModConfig.STEP_HEIGHT.setter().accept(Double.parseDouble(config.getOrElse(ModConfig.STEP_HEIGHT.key(), String.valueOf(ModConfig.STEP_HEIGHT.getter().get().doubleValue()))));
      ModConfig.WATER_MOVEMENT_EFFICIENCY.setter().accept(Double.parseDouble(config.getOrElse(ModConfig.WATER_MOVEMENT_EFFICIENCY.key(), String.valueOf(ModConfig.WATER_MOVEMENT_EFFICIENCY.getter().get().doubleValue()))));

      // setters (saving to config file)
      config.setComment(ModConfig.SPAWN_CHANCE.key(), " Chance for a golem to spawn in a valid location. (Range: 0.0 to 1.0, Default: 0.05) RELOADABLE WITH /reload");
      config.set(ModConfig.SPAWN_CHANCE.key(), String.valueOf(ModConfig.SPAWN_CHANCE.getter().get().floatValue()));

      config.setComment(ModConfig.MAX_HEALTH.key(), " Golem initial health amount. (RESTART REQUIRED)");
      config.set(ModConfig.MAX_HEALTH.key(), String.valueOf(ModConfig.MAX_HEALTH.getter().get().doubleValue()));
      config.setComment(ModConfig.MOVEMENT_SPEED.key(), " Max speed of the golem. (RESTART REQUIRED)");
      config.set(ModConfig.MOVEMENT_SPEED.key(), String.valueOf(ModConfig.MOVEMENT_SPEED.getter().get().doubleValue()));
      config.setComment(ModConfig.KNOCKBACK_RESISTANCE.key(), " Golem's resistance to being knocked back by other attacks. (RESTART REQUIRED)");
      config.set(ModConfig.KNOCKBACK_RESISTANCE.key(), String.valueOf(ModConfig.KNOCKBACK_RESISTANCE.getter().get().doubleValue()));
      config.setComment(ModConfig.ATTACK_DAMAGE.key(), " Golem attack damage amount. (RESTART REQUIRED)");
      config.set(ModConfig.ATTACK_DAMAGE.key(), String.valueOf(ModConfig.ATTACK_DAMAGE.getter().get().doubleValue()));
      config.setComment(ModConfig.FOLLOW_RANGE.key(), " Distance in blocks that a golem will search for and track entities. (RESTART REQUIRED)");
      config.set(ModConfig.FOLLOW_RANGE.key(), String.valueOf(ModConfig.FOLLOW_RANGE.getter().get().doubleValue()));
      config.setComment(ModConfig.STEP_HEIGHT.key(), " Block height that a golem can step up, or walk easily upwards through. (RESTART REQUIRED)");
      config.set(ModConfig.STEP_HEIGHT.key(), String.valueOf(ModConfig.STEP_HEIGHT.getter().get().doubleValue()));
      config.setComment(ModConfig.WATER_MOVEMENT_EFFICIENCY.key(), " Slowness penalty for golem navigating through water. (RESTART REQUIRED)");
      config.set(ModConfig.WATER_MOVEMENT_EFFICIENCY.key(), String.valueOf(ModConfig.WATER_MOVEMENT_EFFICIENCY.getter().get().doubleValue()));

      config.save();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      Constants.LOG.info("failed to read or write config file");
      e.printStackTrace();
    }
  }
}
