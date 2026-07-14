package io.github.jason13official.silent_caves;

import net.fabricmc.api.ClientModInitializer;

public class SilentCavesClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    SilentCavesClient.init();
  }
}
