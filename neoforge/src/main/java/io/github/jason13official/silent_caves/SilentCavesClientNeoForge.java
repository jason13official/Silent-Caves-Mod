package io.github.jason13official.silent_caves;

import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class SilentCavesClientNeoForge {

  public SilentCavesClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> SilentCavesClient.init());

    modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {

      SilentCavesClient.registerEntityRenderers(event::registerEntityRenderer);
    });

    modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {

      SilentCavesClient.registerEntityModels(event::registerLayerDefinition);
    });
  }
}
