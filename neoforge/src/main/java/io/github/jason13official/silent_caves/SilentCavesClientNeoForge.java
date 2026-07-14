package io.github.jason13official.silent_caves;

import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class SilentCavesClientNeoForge {

  public SilentCavesClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> SilentCavesClient.init());
  }
}
