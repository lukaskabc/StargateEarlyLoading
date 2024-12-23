package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;
import org.joml.Vector2f;

public class BufferHelper {
    private static final int COLOR = (255 << 24) | 0xFFFFFF;

    private BufferHelper() {
        throw new AssertionError();
    }

    public static void renderTexture(SimpleBufferBuilder bb, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4, Vector2f u1, Vector2f u2, Vector2f u3, Vector2f u4, Vector2f center) {
        v1.add(center);
        v2.add(center);
        v3.add(center);
        v4.add(center);
        bbPosTex(bb, v2, u1);
        bbPosTex(bb, v3, u2);
        bbPosTex(bb, v1, u3);
        bbPosTex(bb, v4, u4);
    }

    public static void bbPosTex(SimpleBufferBuilder bb, Vector2f v, Vector2f u) {
        bb.pos(v.x(), v.y()).tex(u.x() / 64f, u.y() / 64f).colour(COLOR).endVertex();
    }
}
