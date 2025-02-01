package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class BufferHelper {
    private static final int COLOR = (255 << 24) | 0xFFFFFF;

    private BufferHelper() {
        throw new AssertionError();
    }

    public static void renderTexture(ContextSimpleBuffer bb, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4, Vector2f center) {
        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, center, COLOR);
    }

    public static void renderTextureAlt(ContextSimpleBuffer bb, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4, Vector2f center, int color) {
        v1.add(center);
        v2.add(center);
        v3.add(center);
        v4.add(center);
        bbPosTexAlt(bb, v2, u2, color);
        bbPosTexAlt(bb, v3, u3, color);
        bbPosTexAlt(bb, v1, u1, color);
        bbPosTexAlt(bb, v4, u4, color);
    }

    public static void renderTexture(ContextSimpleBuffer bb, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4, Vector2f center, int color) {
        v1.add(center);
        v2.add(center);
        v3.add(center);
        v4.add(center);
        bbPosTex(bb, v2, u2, color);
        bbPosTex(bb, v3, u3, color);
        bbPosTex(bb, v1, u1, color);
        bbPosTex(bb, v4, u4, color);
    }

    public static void renderTexture(ContextSimpleBuffer bb, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4, Vector2f center) {
        renderTexture(bb, v1.xy(new Vector2f()), v2.xy(new Vector2f()), v3.xy(new Vector2f()), v4.xy(new Vector2f()), u1, u2, u3, u4, center);
    }

    public static void bbPosTex(ContextSimpleBuffer bb, Vector2f v, Vector2f u, int color) {
        bb.simpleBufferBuilder().pos(v.x(), v.y()).tex(u.x() / 64f, u.y() / 64f).colour(color).endVertex();
    }

    public static void bbPosTexAlt(ContextSimpleBuffer bb, Vector2f v, Vector2f u, int color) {
        bb.simpleBufferBuilder().pos(v.x(), v.y()).tex(u.x(), u.y()).colour(color).endVertex();
    }
}
