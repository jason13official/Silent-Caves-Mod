package io.github.jason13official.silent_caves;

import io.github.jason13official.silent_caves.api.client.sound.SoundSuppression;
import io.github.jason13official.silent_caves.platform.Services;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;

public class SilentCavesClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    SilentCavesClient.init();

    ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> {
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("Client player leaving/changing level.");
      }
      SoundSuppression.restoreClientVolumes(Minecraft.getInstance());
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("Restored sound levels.");
      }
    });

    SilentCavesClient.registerEntityRenderers(EntityRendererRegistry::register);

    // why must you make it ugly, UGLY, UGLYYYYYY
    SilentCavesClient.registerEntityModels((modelLayerLocation, layerDefinitionSupplier) -> {
      EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get);
    });
  }
}
