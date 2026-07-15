package io.github.jason13official.silent_caves.impl.client.renderer.layer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;

/// A [VertexConsumer] wrapper that tiles an atlas sprite across entity model faces.
///
/// <p>[ModelPart.Cube] can produce UV > 1.0 on faces bigger than the texture. Naively
/// applying `frac()` fixes atlas bleed but makes UVs crossing an integer boundary
/// interpolate backwards on the GPU.</p>
///
/// <p>Fix: slice each quad along integer UV boundaries so every sub-quad stays within
/// one `[0, 1]` tile, then bilinearly interpolate new corner vertices at the cuts.</p>
public class TilingVertexConsumer implements VertexConsumer {

  /// every quad (rectangle) has exactly 4 corners
  private static final int QUAD_SIZE = 4;

  /// the "real" vertex consumer we forward finished vertices to
  private final VertexConsumer delegate;
  /// the specific texture within the atlas that we're tiling
  private final TextureAtlasSprite sprite;

  /// holds up to 4 vertices while we wait for a full quad to arrive
  private final Vertex[] buffer = new Vertex[QUAD_SIZE];
  /// how many vertices are currently sitting in buffer
  private int buffered = 0;

  public TilingVertexConsumer(VertexConsumer delegate, TextureAtlasSprite sprite) {
    this.delegate = delegate;
    this.sprite = sprite;
  }

  /// @return `true` if `u` is an integer boundary that isn't the range's own start
  /// (i.e. it's a tile's far edge, not the near edge of the next tile).
  private static boolean isTileBoundary(float u, float rangeStart) {
    // Mth.frac(u) strips the integer part, leaving just the fractional (0-1) portion;
    // it being exactly 0 means u lands precisely on a whole-number line
    return u > rangeStart && Mth.frac(u) == 0f;
  }

  private static boolean near(float a, float b) {
    // treat values as equal if they're within a tiny tolerance, since floats
    // rarely compare exactly equal after math operations
    return Math.abs(a - b) < 1e-4f;
  }

  /// Splits `[min, max]` into per-tile edges: `min`, every integer boundary in between, `max`.
  /// Consecutive pairs are the sub-tile intervals to walk. `min == max` yields a single
  /// zero-width interval — callers must guard against dividing by it (see `du/dv > 0f` in [flushQuad]).
  private static float[] tileEdges(float min, float max) {
    // which whole-number tile the range starts and ends in
    int startTile = (int) Math.floor(min);
    int endTile = (int) Math.floor(max);
    // how many tiles the range spans (inclusive of both ends)
    int tileCount = endTile - startTile + 1;

    // edges has one extra slot vs tileCount, since N tiles need N+1 boundary lines
    float[] edges = new float[tileCount + 1];
    edges[0] = min; // first edge is always the true start of the range
    for (int t = 1; t < tileCount; t++) {
      // every edge in between is a whole-number tile boundary
      edges[t] = startTile + t;
    }
    edges[tileCount] = max; // last edge is always the true end of the range
    return edges;
  }

  /// builder pattern for non-entity renderers
  @Override
  public VertexConsumer addVertex(float x, float y, float z) {
    return this.delegate.addVertex(x, y, z);
  }

  @Override
  public VertexConsumer setColor(int red, int green, int blue, int alpha) {
    return this.delegate.setColor(red, green, blue, alpha);
  }

  @Override
  public VertexConsumer setUv(float u, float v) {
    // wrap u/v into the 0-1 range (frac), then map that 0-1 tile-local position
    // to the sprite's actual location within the shared atlas texture
    return this.delegate.setUv(this.sprite.getU(Mth.frac(u)), this.sprite.getV(Mth.frac(v)));
  }

  @Override
  public VertexConsumer setUv1(int u, int v) {
    return this.delegate.setUv1(u, v);
  }

  @Override
  public VertexConsumer setUv2(int u, int v) {
    return this.delegate.setUv2(u, v);
  }

  @Override
  public VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
    return this.delegate.setNormal(normalX, normalY, normalZ);
  }

  /// fast-path bulk-vertex; buffers 4 vertices before splitting and flushing quad
  @Override
  public void addVertex(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ) {
    // stash this vertex's raw (un-tiled) data instead of forwarding it immediately
    buffer[buffered] = new Vertex(x, y, z, color, u, v, packedOverlay, packedLight, normalX, normalY, normalZ);
    // once we've collected all 4 corners of the quad, process and emit them together
    if (++buffered == QUAD_SIZE) {
      flushQuad();
    }
  }

  /// Slices the buffered quad along tile boundaries and emits the resulting sub-quads.
  private void flushQuad() {

    // setup for finding the "footprint" of a quad or the total area it takes up
    // (start the min/max bounds at the first vertex's own coordinates)
    float u_min = buffer[0].u();
    float u_max = buffer[0].u();
    float v_min = buffer[0].v();
    float v_max = buffer[0].v();

    // for each vertex of the quad
    for (int i = 1; i < QUAD_SIZE; i++) {
      Vertex vert = buffer[i];

      // find farthest values
      if (vert.u() < u_min) {
        u_min = vert.u();
      }
      if (vert.u() > u_max) {
        u_max = vert.u();
      }
      if (vert.v() < v_min) {
        v_min = vert.v();
      }
      if (vert.v() > v_max) {
        v_max = vert.v();
      }
    }

    // get vertices in proper winding order TL/TR/BL/BR
    Corners corners = findCorners(u_min, u_max, v_min, v_max);

    // if we can't cleanly form a quad
    if (corners == null) {
      for (int i = 0; i < QUAD_SIZE; i++) {
        Vertex vert = buffer[i];

        // fallback to un-tiled behavior: just forward the vertex as-is,
        // wrapping its raw UV into the sprite's 0-1 tile space
        delegate.addVertex(vert.x(), vert.y(), vert.z(), vert.color(), sprite.getU(Mth.frac(vert.u())), sprite.getV(Mth.frac(vert.v())), vert.overlay(), vert.light(), vert.nx(), vert.ny(), vert.nz());
      }
      buffered = 0; // reset
      return;
    }

    float du = u_max - u_min; // u width of the quad
    float dv = v_max - v_min; // v height of the quad

    // create lists for cutting: every point along each axis where we need to
    // slice the quad so no sub-quad crosses a tile boundary
    float[] uEdges = tileEdges(u_min, u_max);
    float[] vEdges = tileEdges(v_min, v_max);

    // building smaller quads: walk every combination of adjacent u-edge and
    // v-edge pairs, each pair defining one sub-quad's rectangle
    for (int ui = 0; ui < uEdges.length - 1; ui++) {
      float prev_u = uEdges[ui]; // left edge
      float next_u = uEdges[ui + 1]; // right edge

      for (int vi = 0; vi < vEdges.length - 1; vi++) {
        float prev_v = vEdges[vi]; // top edge
        float next_v = vEdges[vi + 1]; // bottom edge

        // atlas UV coordinates for this sub-quad's edges.
        // When next_u/next_v falls on a tile boundary (frac==0 AND not the
        // starting point), use 1.0 so the sub-quad shows the full last slice
        // of the tile rather than the first slice of the next one.
        float su1 = Mth.frac(prev_u); // left edge, tile-local (0-1)
        float su2 = isTileBoundary(next_u, u_min) ? 1f : Mth.frac(next_u); // right edge, tile-local
        float sv1 = Mth.frac(prev_v); // top edge, tile-local
        float sv2 = isTileBoundary(next_v, v_min) ? 1f : Mth.frac(next_v); // bottom edge, tile-local

        // convert those [0,1] tile-local coords into actual atlas texture coords
        float au1 = sprite.getU(su1), au2 = sprite.getU(su2);
        float av1 = sprite.getV(sv1), av2 = sprite.getV(sv2);

        // normalized (0-1) position parameters within the full quad, used to
        // interpolate this sub-quad's corner positions from the original 4 corners.
        // du or dv can independently be 0 (a quad that's a UV-line on one axis
        // but spans multiple tiles on the other) even though the ambiguous-corner
        // check above already ruled out both being 0 at once — guard the divide.
        float pu1 = (du > 0f) ? (prev_u - u_min) / du : 0f; // left edge interpolation
        float pu2 = (du > 0f) ? (next_u - u_min) / du : 1f; // right edge lerp
        float pv1 = (dv > 0f) ? (prev_v - v_min) / dv : 0f; // top edge lerp
        float pv2 = (dv > 0f) ? (next_v - v_min) / dv : 1f; // bottom edge lerp

        // emit 4 vertices with winding order matching ModelPart.Polygon (see emitVertex).
        emitVertex(corners, pu2, pv1, au2, av1); // top-right
        emitVertex(corners, pu1, pv1, au1, av1); // top-left
        emitVertex(corners, pu1, pv2, au1, av2); // bottom-left
        emitVertex(corners, pu2, pv2, au2, av2); // bottom-right
      }
    }

    buffered = 0; // reset
  }

  /// Identifies the four corner vertices of a quad by their UV role. [ModelPart.Cube]
  /// always produces UV-rectangular faces with exactly two distinct `u`/`v` values each.
  ///
  /// @return null if the four corners can't be identified.
  private Corners findCorners(float u_min, float u_max, float v_min, float v_max) {
    // slots for the 4 named corners; filled in as we find matching vertices
    Vertex tl = null, tr = null, bl = null, br = null;
    for (int i = 0; i < QUAD_SIZE; i++) {
      Vertex vert = buffer[i];
      // is this vertex on the left/right edge, and top/bottom edge, of the bounding box?
      boolean L = near(vert.u(), u_min), R = near(vert.u(), u_max);
      boolean T = near(vert.v(), v_min), B = near(vert.v(), v_max);
      // classify the vertex into exactly one corner slot based on which edges it's on
      if (L && T && tl == null) {
        tl = vert;
      } else if (R && T && tr == null) {
        tr = vert;
      } else if (L && B && bl == null) {
        bl = vert;
      } else if (R && B && br == null) {
        br = vert;
      }
    }
    // if any corner slot is still empty, the 4 vertices didn't form a clean rectangle
    return (tl == null || tr == null || bl == null || br == null) ? null : new Corners(tl, tr, bl, br);
  }

  /// Emits one vertex, lerping its position across the original quad's four corners.
  /// `tu/tv` are normalized `[0, 1]` parameters in the original quad's UV space.
  private void emitVertex(Corners c, float tu, float tv, float atlas_u, float atlas_v) {
    // bilinear interpolation: first lerp along u (left-to-right) across the top
    // edge and the bottom edge separately, then lerp those two results along v
    // (top-to-bottom) to get the final 3D position for this sub-quad corner
    float x = Mth.lerp(tv, Mth.lerp(tu, c.tl().x(), c.tr().x()), Mth.lerp(tu, c.bl().x(), c.br().x()));
    float y = Mth.lerp(tv, Mth.lerp(tu, c.tl().y(), c.tr().y()), Mth.lerp(tu, c.bl().y(), c.br().y()));
    float z = Mth.lerp(tv, Mth.lerp(tu, c.tl().z(), c.tr().z()), Mth.lerp(tu, c.bl().z(), c.br().z()));
    // For entity rendering, color/light/overlay/normals are uniform across the quad.
    Vertex ref = c.tl();
    // forward the finished, positioned, tiled vertex to the real vertex consumer
    delegate.addVertex(x, y, z, ref.color(), atlas_u, atlas_v, ref.overlay(), ref.light(), ref.nx(), ref.ny(), ref.nz());
  }

  /// one buffered vertex, in the order supplied by [ModelPart]
  private record Vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float nx, float ny, float nz) {

  }

  /// the four corners of a quad's UV bounding box; T=v_min, B=v_max, L=u_min, R=u_max
  private record Corners(Vertex tl, Vertex tr, Vertex bl, Vertex br) {

  }
}