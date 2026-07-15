package io.github.jason13official.silent_caves.api.client.sound;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

/// Used to gradually decrease the client's volume levels
public class SoundSuppression {

  /// used to cache the original volumes before a golem begins lowering the values
  public static final Map<SoundSource, Double> ORIGINAL_VOLUMES = new EnumMap<>(SoundSource.class);

  /// total amount we are decreasing the volume by
  private static double currentSuppression = 0.0D;

  private static int lastComputeTick = Integer.MIN_VALUE;
  private static int lastSuppressTick = Integer.MIN_VALUE;

  /// general reset method
  public static void restoreClientVolumes(Minecraft mc) {

    // restore original volumes
    ORIGINAL_VOLUMES.forEach((source, amount) -> {
      mc.options.getSoundSourceOptionInstance(source).set(amount);
    });

    // clear map and suppression amount
    ORIGINAL_VOLUMES.clear();
    currentSuppression = 0.0D;
  }

  /// adjust client volume levels based on proximity to an entity
  public static void adjustClientVolumeLevels(Minecraft mc, LivingEntity deafener) {

    // the player is not loaded in a level
    if (mc.player == null) {
      return;
    }

    // all loaded golems update the shared max-suppression value; we reset it once per tick
    if (deafener.tickCount != lastComputeTick) {
      currentSuppression = 0.0D;
      lastComputeTick = deafener.tickCount;
    }

    double distance = mc.player.distanceTo(deafener);

    double deafenedRange = 16.0D; // fully silent within 16 blocks
    double hearingRange = deafener.getAttributeValue(Attributes.FOLLOW_RANGE); // no suppression beyond 48 blocks

    // inverse interpolation
    double factor = inverseLerp(distance, deafenedRange, hearingRange);

    if (factor > currentSuppression) {
      currentSuppression = factor;
    }

    if (currentSuppression > 0.0) {
      // cache initial volume levels before adjustments occur
      cacheVolumes(mc);

      applySuppression(mc);
      lastSuppressTick = deafener.tickCount;
    }

    if (!ORIGINAL_VOLUMES.isEmpty() && deafener.tickCount - lastSuppressTick > 20) {

      // restore original sound if no golem suppressing for > 20 ticks (1 second)
      restoreClientVolumes(mc);
    }
  }

  /// cache current user volumes once at the start of a suppression session
  private static void cacheVolumes(Minecraft mc) {
    if (ORIGINAL_VOLUMES.isEmpty()) {
      for (SoundSource source : SoundSource.values()) {
        if (source == SoundSource.MASTER || source == SoundSource.PLAYERS) {
          continue;
        }
        ORIGINAL_VOLUMES.put(source, mc.options.getSoundSourceOptionInstance(source).get());
      }
    }
  }

  /// fade to zero proportional to proximity
  private static void applySuppression(Minecraft mc) {
    for (Map.Entry<SoundSource, Double> entry : ORIGINAL_VOLUMES.entrySet()) {
      double target = entry.getValue() * (1.0 - currentSuppression);
      mc.options.getSoundSourceOptionInstance(entry.getKey()).set(target);
    }
  }

  private static double inverseLerp(double distance, double closeRange, double maxRange) {

    if (distance >= maxRange) {
      return 0.0D;
    } else if (distance <= closeRange) {
      return 1.0D;
    }

    return 1.0D - ((distance - closeRange) / (maxRange - closeRange));
  }
}
