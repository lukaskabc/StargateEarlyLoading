package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate.CENTER;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.renderTexture;

public class GenericChevron {
    protected static final float CHEVRON_LIGHT_FRONT_LENGTH = 4F / 16;
    protected static final float CHEVRON_LIGHT_DIVIDER_LENGTH = 1F / 16;
    protected static final float CHEVRON_LIGHT_BACK_LENGTH = 4F / 16;

    protected static final float CHEVRON_LIGHT_LENGTH = CHEVRON_LIGHT_FRONT_LENGTH + CHEVRON_LIGHT_DIVIDER_LENGTH + CHEVRON_LIGHT_BACK_LENGTH;

    protected static final float CHEVRON_DIVIDER_HEIGHT = 1F / 16;

    protected static final float CHEVRON_LIGHT_Z_OFFSET = CHEVRON_LIGHT_DIVIDER_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_FRONT_Z_OFFSET = 1; // CHEVRON_LIGHT_Z_OFFSET + CHEVRON_LIGHT_FRONT_LENGTH;
    protected static final float CHEVRON_LIGHT_BACK_Z_OFFSET = -CHEVRON_LIGHT_Z_OFFSET - CHEVRON_LIGHT_BACK_LENGTH;

    protected static final float CHEVRON_LIGHT_TOP_WIDTH = 6F / 16;
    protected static final float CHEVRON_LIGHT_TOP_CENTER = CHEVRON_LIGHT_TOP_WIDTH / 2;
    protected static final float CHEVRON_LIGHT_BOTTOM_WIDTH = 3F / 16;
    protected static final float CHEVRON_LIGHT_BOTTOM_CENTER = CHEVRON_LIGHT_BOTTOM_WIDTH / 2;
    protected static final float CHEVRON_LIGHT_HEIGHT = 7F / 16;
    protected static final float CHEVRON_LIGHT_HEIGHT_CENTER = CHEVRON_LIGHT_HEIGHT / 2;
    protected static final float CHEVRON_LIGHT_DIVIDER_BOTTOM_CENTER = CHEVRON_LIGHT_BOTTOM_CENTER + (CHEVRON_LIGHT_TOP_CENTER - CHEVRON_LIGHT_BOTTOM_CENTER) * ((CHEVRON_LIGHT_HEIGHT - CHEVRON_DIVIDER_HEIGHT) / CHEVRON_LIGHT_HEIGHT);

    protected static final float CHEVRON_LIGHT_DIVIDER_BOTTOM_HEIGHT = CHEVRON_LIGHT_HEIGHT_CENTER - CHEVRON_DIVIDER_HEIGHT;

    protected static final float OUTER_CHEVRON_Z_THICKNESS = 1F / 16;
    protected static final float OUTER_CHEVRON_Z_OFFSET = 1;// OUTER_CHEVRON_Z_THICKNESS + STARGATE_RING_OFFSET;

    protected static final float OUTER_CHEVRON_SIDE_HEIGHT = 7F / 16;
    protected static final float OUTER_CHEVRON_SIDE_BOTTOM_THICKNESS = 2F / 16;
    protected static final float OUTER_CHEVRON_SIDE_TOP_THICKNESS = OUTER_CHEVRON_SIDE_BOTTOM_THICKNESS + 1F / 16;
    protected static final float OUTER_CHEVRON_TOP_OFFSET = 4F / 16;

    protected static final float OUTER_CHEVRON_LOWER_BOTTOM_WIDTH = 4F / 16;
    protected static final float OUTER_CHEVRON_LOWER_BOTTOM_CENTER = OUTER_CHEVRON_LOWER_BOTTOM_WIDTH / 2;

    protected static final float OUTER_CHEVRON_BOTTOM_HEIGHT = 2F / 16;
    protected static final float OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER = OUTER_CHEVRON_BOTTOM_HEIGHT / 2;
    protected static final float OUTER_CHEVRON_Y_OFFSET = -6.5F / 16;

    protected static final float OUTER_CHEVRON_UPPER_BOTTOM_CENTER = (OUTER_CHEVRON_TOP_OFFSET / (OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_BOTTOM_HEIGHT)) * OUTER_CHEVRON_BOTTOM_HEIGHT;


    public static void renderChevronLight(ContextSimpleBuffer bb, Matrix3f matrix3f, boolean isRaised) {
        float yOffset = isRaised ? 2F / 16 : 0;
        Vector3f v1 = new Vector3f(-CHEVRON_LIGHT_TOP_CENTER, CHEVRON_LIGHT_HEIGHT_CENTER + yOffset, CHEVRON_LIGHT_FRONT_Z_OFFSET);
        Vector2f u1 = new Vector2f(48f, 17f);
        Vector3f v2 = new Vector3f(-CHEVRON_LIGHT_BOTTOM_CENTER, -CHEVRON_LIGHT_HEIGHT_CENTER + yOffset, CHEVRON_LIGHT_FRONT_Z_OFFSET);
        Vector2f u2 = new Vector2f(49.5f, 24f);
        Vector3f v3 = new Vector3f(CHEVRON_LIGHT_BOTTOM_CENTER, -CHEVRON_LIGHT_HEIGHT_CENTER + yOffset, CHEVRON_LIGHT_FRONT_Z_OFFSET);
        Vector2f u3 = new Vector2f(52.5f, 24f);
        Vector3f v4 = new Vector3f(CHEVRON_LIGHT_TOP_CENTER, CHEVRON_LIGHT_HEIGHT_CENTER + yOffset, CHEVRON_LIGHT_FRONT_Z_OFFSET);
        Vector2f u4 = new Vector2f(54f, 17f);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    public static void renderOuterChevronFront(ContextSimpleBuffer bb, Matrix3f matrix3f, boolean isRaised) {
        float yOffset = isRaised ? -2F / 16 : 0;
        renderCenterOuterChevron(bb, matrix3f, isRaised, yOffset);
        renderLeftOuterChevron(bb, matrix3f, isRaised, yOffset);
        renderRightOuterChevron(bb, matrix3f, isRaised, yOffset);
    }

    private static void renderRightOuterChevron(ContextSimpleBuffer bb, Matrix3f matrix3f, boolean isRaised, float yOffset) {
        Vector3f v1 = new Vector3f(OUTER_CHEVRON_TOP_OFFSET, OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u1 = new Vector2f(60F, 36F);
        Vector3f v2 = new Vector3f(0F / 16, -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u2 = new Vector2f(56F, 45F);
        Vector3f v3 = new Vector3f(OUTER_CHEVRON_SIDE_BOTTOM_THICKNESS, -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u3 = new Vector2f(58F, 45F);
        Vector3f v4 = new Vector3f(OUTER_CHEVRON_TOP_OFFSET + OUTER_CHEVRON_SIDE_TOP_THICKNESS, OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u4 = new Vector2f(63F, 36F);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    private static void renderLeftOuterChevron(ContextSimpleBuffer bb, Matrix3f matrix3f, boolean isRaised, float yOffset) {
        Vector3f v1 = new Vector3f(-(OUTER_CHEVRON_TOP_OFFSET + OUTER_CHEVRON_SIDE_TOP_THICKNESS), OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u1 = new Vector2f(47F, 36F);
        Vector3f v2 = new Vector3f(-OUTER_CHEVRON_SIDE_BOTTOM_THICKNESS, -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u2 = new Vector2f(52F, 45F);
        Vector3f v3 = new Vector3f(0F / 16, -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u3 = new Vector2f(54F, 45F);
        Vector3f v4 = new Vector3f(-OUTER_CHEVRON_TOP_OFFSET, OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u4 = new Vector2f(50F, 36F);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    private static void renderCenterOuterChevron(ContextSimpleBuffer bb, Matrix3f matrix3f, boolean isRaised, float yOffset) {
        Vector3f v1 = new Vector3f(-OUTER_CHEVRON_UPPER_BOTTOM_CENTER, OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u1 = new Vector2f((55F - OUTER_CHEVRON_SIDE_BOTTOM_THICKNESS * 16), 43F);
        Vector3f v2 = new Vector3f(0, -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u2 = new Vector2f(55F, 45F);
        Vector3f v3 = new Vector3f(0, -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u3 = new Vector2f(55F, 45F);
        Vector3f v4 = new Vector3f(OUTER_CHEVRON_UPPER_BOTTOM_CENTER, OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + yOffset, OUTER_CHEVRON_Z_OFFSET);
        Vector2f u4 = new Vector2f((55F + OUTER_CHEVRON_SIDE_BOTTOM_THICKNESS * 16), 43F);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    private GenericChevron() {
        throw new AssertionError();
    }
}
