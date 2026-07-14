package io.github.jason13official.silent_caves;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SilentCavesClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    SilentCavesClient.init();

    SilentCavesClient.registerEntityRenderers(EntityRendererRegistry::register);

    // why must you make it ugly, UGLY, UGLYYYYYY
    SilentCavesClient.registerEntityModels((modelLayerLocation, layerDefinitionSupplier) -> {
      EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get);
    });
  }
}
