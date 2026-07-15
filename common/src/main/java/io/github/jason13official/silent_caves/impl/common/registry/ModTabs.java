package io.github.jason13official.silent_caves.impl.common.registry;

import io.github.jason13official.silent_caves.Constants;
import io.github.jason13official.silent_caves.SilentCaves;
import io.github.jason13official.silent_caves.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModTabs {

  public static CreativeModeTab SILENT_CAVES;

  public static void register(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {

    SILENT_CAVES = Services.PLATFORM.tabBuilder()
        .icon(() -> new ItemStack(ModItems.DEAFENING_GOLEM_SPAWN_EGG))
        .title(Component.translatable("itemGroup.silentCaves"))
        .displayItems((itemDisplayParameters, output) -> {

          output.accept(ModItems.DEAFENING_GOLEM_SPAWN_EGG);
          output.accept(ModItems.MONSTER_CUBE_SPAWN_EGG);
        }).build();
    consumer.accept(SILENT_CAVES, SilentCaves.identifier(Constants.MOD_ID));
  }
}
