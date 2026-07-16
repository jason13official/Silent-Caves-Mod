package io.github.jason13official.silent_caves;

import io.github.jason13official.silent_caves.impl.common.ModConfigIO;
import io.github.jason13official.silent_caves.impl.common.entity.DeafeningGolem;
import io.github.jason13official.silent_caves.impl.common.registry.ModBlocks;
import io.github.jason13official.silent_caves.impl.common.registry.ModEntities;
import io.github.jason13official.silent_caves.impl.common.registry.ModItems;
import io.github.jason13official.silent_caves.impl.common.registry.ModMenus;
import io.github.jason13official.silent_caves.impl.common.registry.ModParticles;
import io.github.jason13official.silent_caves.impl.common.registry.ModTabs;
import io.github.jason13official.silent_caves.impl.common.registry.ModTiles;
import io.github.jason13official.silent_caves.platform.Services;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public class SilentCavesFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    bind(BuiltInRegistries.BLOCK, ModBlocks::register);
    bind(BuiltInRegistries.ENTITY_TYPE, ModEntities::register);
    bind(BuiltInRegistries.ITEM, ModItems::register);
    bind(BuiltInRegistries.PARTICLE_TYPE, ModParticles::register);
    bind(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModTiles::register);
    bind(BuiltInRegistries.MENU, ModMenus::register);
    bind(BuiltInRegistries.CREATIVE_MODE_TAB, ModTabs::register);

    SilentCaves.init();

    SilentCaves.registerEntityAttributes(FabricDefaultAttributeRegistry::register);

    SpawnPlacements.register(ModEntities.DEAFENING_GOLEM, SpawnPlacementTypes.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, DeafeningGolem.SPAWN_PREDICATE);
    BiomeModifications.addSpawn(
        context -> {
          return BiomeSelectors.foundInOverworld().test(context) || BiomeSelectors.foundInTheNether().test(context);
        },
        MobCategory.MONSTER,
        ModEntities.DEAFENING_GOLEM,
        50,  // weight — intentionally rare
        2,  // minCount
        6   // maxCount
    );

    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ResourceReloadListener());
  }

  public <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {

    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }

  public static class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
      return SilentCaves.identifier(Constants.MOD_ID);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
      ModConfigIO.load(Services.PLATFORM.getConfigDirectory());
    }
  }
}
