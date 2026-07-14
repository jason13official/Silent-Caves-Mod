package io.github.jason13official.silent_caves.impl.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jason13official.silent_caves.Constants;
import io.github.jason13official.silent_caves.api.common.entity.IBlockIdHolder;
import io.github.jason13official.silent_caves.impl.common.entity.AbstractIdentifierMob;
import io.github.jason13official.silent_caves.platform.Services;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TextureLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

  /// cache of Block ID -> Texture IDs (top, sides, bottom, overlays, etc.)
  private static final Map<ResourceLocation, List<ResourceLocation>> TEXTURE_CACHE = new HashMap<>();

  public TextureLayer(RenderLayerParent<T, M> renderer) {
    super(renderer);
  }

  @Override
  public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

    if (entity.isInvisible()) return;

    ResourceLocation blockId = entity instanceof IBlockIdHolder idHolder ? idHolder.getBlockId() : AbstractIdentifierMob.DEFAULT_IDENTIFIER;

    List<ResourceLocation> textures = resolveTextures(blockId);

    // choose our texture based on modulus of the entity ID;
    // golems from blocks with multiple textures get an essentially random texture,
    // but it's consistent/deterministic this way
    ResourceLocation textureName = textures.get(Math.floorMod(entity.getId(), textures.size()));

    // create material out of chosen texture ID
    Material material = new Material(InventoryMenu.BLOCK_ATLAS, textureName);

    // use our created material to create a vertex consumer for rendering
    VertexConsumer consumer = material.buffer(buffer, this.getParentModel()::renderType); // <-- delegates to SpriteCoordinateExpander :(

    // old version just renders the pure block atlas texture, which is not what we want lol
    // this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.cutout()), packedLight, OverlayTexture.NO_OVERLAY);

    // int overlay = entity instanceof LivingEntity living ? LivingEntityRenderer.getOverlayCoords(living, 0.0f) : OverlayTexture.NO_OVERLAY;
    this.getParentModel().renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
  }


  private static List<ResourceLocation> resolveTextures(ResourceLocation blockId) {

    return TEXTURE_CACHE.computeIfAbsent(blockId, TextureLayer::getTexturesFromBlockId);
  }

  /// gather textures for all quads of a block model into a list (or the `particle` texture ID if no other textures are found)
  private static List<ResourceLocation> getTexturesFromBlockId(ResourceLocation blockId) {

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info("Caching textures of {}", blockId.toString());
    }

    BlockState state = BuiltInRegistries.BLOCK.getOptional(blockId).orElse(Blocks.STONE).defaultBlockState();

    BlockModelShaper shaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
    BakedModel model = shaper.getBlockModel(state);
    RandomSource random = RandomSource.create(0L);
    LinkedHashSet<ResourceLocation> names = new LinkedHashSet<>();

    gatherDirectionalTextureIds(model, state, random, names);
    gatherNonDirectionalTextureIds(model, state, random, names);
    gatherParticleTextureId(model, names);

    // safeguard against blocks with no textures (i.e. air or similar blocks added by mods)
    if (names.isEmpty()) {
      return TEXTURE_CACHE.computeIfAbsent(AbstractIdentifierMob.DEFAULT_IDENTIFIER, TextureLayer::getTexturesFromBlockId);
    }

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info("Gathered textures of {}", blockId.toString());
    }

    return new ArrayList<>(names);
  }

  private static void gatherParticleTextureId(BakedModel model, LinkedHashSet<ResourceLocation> names) {
    // particle texture (only if we didn't get other textures)
    ResourceLocation particle = model.getParticleIcon().contents().name();
    if (names.isEmpty() && !particle.getPath().isEmpty()) {
      names.add(particle);
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info(" - has particle texture {}", particle.toString());
      }
    }
  }

  private static void gatherNonDirectionalTextureIds(BakedModel model, BlockState state, RandomSource random, LinkedHashSet<ResourceLocation> names) {
    // non-directional quads
    int nonDirectionalQuadCount = 0;
    for (BakedQuad quad : model.getQuads(state, null, random)) {
      ++nonDirectionalQuadCount;
      ResourceLocation textureId = quad.getSprite().contents().name();
      names.add(textureId);
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("Discovered non-directional: {}", textureId);
      }
    }
    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info(" - counted {} non-directional quads", nonDirectionalQuadCount);
    }
  }

  private static void gatherDirectionalTextureIds(BakedModel model, BlockState state, RandomSource random, LinkedHashSet<ResourceLocation> names) {
    // directional quads
    int directionalQuadCount = 0;
    for (Direction dir : Direction.values()) {
      for (BakedQuad quad : model.getQuads(state, dir, random)) {
        ++directionalQuadCount;
        ResourceLocation textureId = quad.getSprite().contents().name();
        names.add(textureId);
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
          Constants.LOG.info("Discovered directional: {}", textureId);
        }
      }
    }
    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info(" - counted {} directional quads", directionalQuadCount);
    }
  }
}
