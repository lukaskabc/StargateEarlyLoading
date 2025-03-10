package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.getCenter;

public class BufferHelper {
    public static final int COLOR = (255 << 24) | 0xFFFFFF;

    private BufferHelper() {
        throw new AssertionError();
    }

    public static void renderTextureCentered(ContextSimpleBuffer bb, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4) {
        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4, COLOR);
    }

    public static void renderTextureAlt(ContextSimpleBuffer bb, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4, int color) {
        final Vector2f center = getCenter();
        v1.add(center);
        v2.add(center);
        v3.add(center);
        v4.add(center);
        bbPosTexAlt(bb, v2, u2, color);
        bbPosTexAlt(bb, v3, u3, color);
        bbPosTexAlt(bb, v1, u1, color);
        bbPosTexAlt(bb, v4, u4, color);
    }

    public static void renderTextureCentered(ContextSimpleBuffer bb, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4, int color) {
        final Vector2f center = getCenter();
        v1.add(center);
        v2.add(center);
        v3.add(center);
        v4.add(center);
        bbPosTex(bb, v2, u2, color);
        bbPosTex(bb, v3, u3, color);
        bbPosTex(bb, v1, u1, color);
        bbPosTex(bb, v4, u4, color);
    }

    public static void renderTextureCentered(ContextSimpleBuffer bb, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4) {
        renderTextureCentered(bb, new Vector2f(v1.x(), v1.y()), new Vector2f(v2.x(), v2.y()), new Vector2f(v3.x(), v3.y()), new Vector2f(v4.x(), v4.y()), u1, u2, u3, u4);
    }

    public static void bbPosTex(ContextSimpleBuffer bb, Vector2f v, Vector2f u, int color) {
        bb.simpleBufferBuilder().pos(v.x(), v.y()).tex(u.x() / 64f, u.y() / 64f).colour(color).endVertex();
    }

    public static void bbPosTexAlt(ContextSimpleBuffer bb, Vector2f v, Vector2f u, int color) {
        bb.simpleBufferBuilder().pos(v.x(), v.y()).tex(u.x(), u.y()).colour(color).endVertex();
    }
}
