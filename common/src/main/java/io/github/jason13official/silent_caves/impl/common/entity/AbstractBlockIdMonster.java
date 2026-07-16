package io.github.jason13official.silent_caves.impl.common.entity;

import io.github.jason13official.silent_caves.api.common.entity.IBlockIdHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBlockIdMonster extends Monster implements IBlockIdHolder {

  public static final Supplier<List<TagKey<Block>>> VALID_SPAWNS = () -> new ArrayList<>(List.of(
      BlockTags.GOLD_ORES, BlockTags.IRON_ORES, BlockTags.DIAMOND_ORES,
      BlockTags.REDSTONE_ORES, BlockTags.LAPIS_ORES, BlockTags.COAL_ORES,
      BlockTags.EMERALD_ORES, BlockTags.COPPER_ORES, BlockTags.MINEABLE_WITH_PICKAXE,
      BlockTags.BASE_STONE_OVERWORLD, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES,
      BlockTags.BASE_STONE_NETHER, BlockTags.DIRT));

  /// minecraft:stone, an identifier within the BLOCKS registry
  public static ResourceLocation DEFAULT_IDENTIFIER = ResourceLocation.withDefaultNamespace("stone");

  /// tag for saved data
  public static final String SPAWNED_ON_BLOCK_TAG = "SpawnedOnBlock";

  /// accessor for saved data
  private static final EntityDataAccessor<String> SPAWNED_ON_BLOCK_ID = SynchedEntityData.defineId(AbstractBlockIdMonster.class, EntityDataSerializers.STRING);

  private Block spawnedOnBlock;

  public AbstractBlockIdMonster(EntityType<? extends AbstractBlockIdMonster> entityType, Level level) {
    super(entityType, level);

    this.spawnedOnBlock = Blocks.STONE;
  }

  @Override
  protected void defineSynchedData(Builder builder) {
    super.defineSynchedData(builder);

    builder.define(SPAWNED_ON_BLOCK_ID, DEFAULT_IDENTIFIER.toString());
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);

    compound.putString(SPAWNED_ON_BLOCK_TAG, this.entityData.get(SPAWNED_ON_BLOCK_ID));
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);

    if (compound.contains(SPAWNED_ON_BLOCK_TAG)) {
      this.entityData.set(SPAWNED_ON_BLOCK_ID, compound.getString(SPAWNED_ON_BLOCK_TAG));
      this.spawnedOnBlock = BuiltInRegistries.BLOCK.get(this.getBlockId());
    }
  }

  @Override
  public ResourceLocation getBlockId() {

    return ResourceLocation.parse(this.getEntityData().get(SPAWNED_ON_BLOCK_ID));
  }

  public Block getSpawnedOnBlock() {

    return spawnedOnBlock;
  }

  @Override
  public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {

    BlockState spawnedOnState = level.getBlockState(this.blockPosition().below());
    Block spawnedOnBlock = spawnedOnState.isAir() ? Blocks.STONE : spawnedOnState.getBlock();
    this.spawnedOnBlock = spawnedOnBlock;
    this.getEntityData().set(SPAWNED_ON_BLOCK_ID, BuiltInRegistries.BLOCK.getKey(spawnedOnBlock).toString());

    return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
  }
}
