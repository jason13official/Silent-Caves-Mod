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
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class SilentCavesNeoForge {

  public static IEventBus EVENT_BUS;

  public SilentCavesNeoForge(final IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    bind(Registries.BLOCK, ModBlocks::register);
    bind(Registries.ENTITY_TYPE, ModEntities::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.PARTICLE_TYPE, ModParticles::register);
    bind(Registries.BLOCK_ENTITY_TYPE, ModTiles::register);
    bind(Registries.MENU, ModMenus::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);

    EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> SilentCaves.init());

    EVENT_BUS.addListener((Consumer<EntityAttributeCreationEvent>) event -> {
      SilentCaves.registerEntityAttributes((entityType, builder) -> {
        event.put(entityType, builder.build());
      });
    });

    EVENT_BUS.addListener((Consumer<RegisterSpawnPlacementsEvent>) event -> {
      event.register(ModEntities.DEAFENING_GOLEM, SpawnPlacementTypes.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, DeafeningGolem.SPAWN_PREDICATE, Operation.REPLACE);
    });

    NeoForge.EVENT_BUS.addListener((Consumer<AddReloadListenerEvent>) event -> {
      event.addListener(new ResourceReloadListener());
    });

    if (FMLLoader.getDist() == Dist.CLIENT) {
      new SilentCavesClientNeoForge(EVENT_BUS);
    }
  }

  public <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, ResourceLocation>> source) {

    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return SilentCaves.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      ModConfigIO.load(Services.PLATFORM.getConfigDirectory());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}