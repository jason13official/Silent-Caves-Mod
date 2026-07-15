package io.github.jason13official.silent_caves;

import io.github.jason13official.silent_caves.api.client.sound.SoundSuppression;
import io.github.jason13official.silent_caves.platform.Services;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

public class SilentCavesClientNeoForge {

  public SilentCavesClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> SilentCavesClient.init());

    NeoForge.EVENT_BUS.addListener((Consumer<LevelEvent.Unload>) event -> {

      if (!FMLLoader.getDist().isClient()) return;
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("Client player leaving/changing level.");
      }
      SoundSuppression.restoreClientVolumes(Minecraft.getInstance());
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("Restored sound levels.");
      }
    });

    modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {

      SilentCavesClient.registerEntityRenderers(event::registerEntityRenderer);
    });

    modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {

      SilentCavesClient.registerEntityModels(event::registerLayerDefinition);
    });
  }
}
