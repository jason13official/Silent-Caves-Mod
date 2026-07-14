package io.github.jason13official.silent_caves;

import net.minecraft.resources.ResourceLocation;

public class SilentCaves {

  public static void init() {
  }

  public static ResourceLocation identifier(final String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}