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
      ModConfig.SPAWN_CHANCE.setter().accept(Float.parseFloat(config.getOrElse(ModConfig.SPAWN_CHANCE.key(), String.valueOf(0.05f))));

      // setters (saving to config file)
      config.setComment(ModConfig.SPAWN_CHANCE.key(), " Chance for a golem to spawn in a valid location. (Range: 0.0 to 1.0, Default: 0.05)");
      config.set(ModConfig.SPAWN_CHANCE.key(), String.valueOf(ModConfig.SPAWN_CHANCE.getter().get().floatValue()));

      config.save();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      Constants.LOG.info("failed to read or write config file");
      e.printStackTrace();
    }
  }
}
