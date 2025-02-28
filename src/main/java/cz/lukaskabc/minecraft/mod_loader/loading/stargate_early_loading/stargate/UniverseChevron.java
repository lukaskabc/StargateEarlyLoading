package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.SGJourneyCoordinateHelper;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.renderTextureCentered;

public class UniverseChevron {
    protected static final float STARGATE_RING_FRONT_THICKNESS = 3F;
    protected static final float STARGATE_RING_BACK_THICKNESS = 4F;
    protected static final float STARGATE_RING_OFFSET = (STARGATE_RING_FRONT_THICKNESS + STARGATE_RING_BACK_THICKNESS) / 2 / 16;

    protected static final float CHEVRON_LIGHT_THICKNESS = 1F / 16;
    protected static final float CHEVRON_LIGHT_Z_OFFSET = STARGATE_RING_OFFSET + CHEVRON_LIGHT_THICKNESS;

    protected static final float CHEVRON_LIGHT_TOP_LENGTH = 4F / 16;
    protected static final float CHEVRON_LIGHT_TOP_CENTER = CHEVRON_LIGHT_TOP_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_TOP_HEIGHT = 5F / 16;

    protected static final float CHEVRON_LIGHT_MID1_LENGTH = 6F / 16;
    protected static final float CHEVRON_LIGHT_MID1_CENTER = CHEVRON_LIGHT_MID1_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_MID1_HEIGHT = 4F / 16;

    protected static final float CHEVRON_LIGHT_MID2_LENGTH = 3F / 16;
    protected static final float CHEVRON_LIGHT_MID2_CENTER = CHEVRON_LIGHT_MID2_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_MID2_HEIGHT = 2F / 16;

    protected static final float CHEVRON_LIGHT_BOTTOM_LENGTH = 1F / 16;
    protected static final float CHEVRON_LIGHT_BOTTOM_CENTER = CHEVRON_LIGHT_BOTTOM_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_BOTTOM_HEIGHT = 0;

    protected static final float OUTER_CHEVRON_THICKNESS = 0.5F / 16;
    protected static final float OUTER_CHEVRON_Z_OFFSET = STARGATE_RING_OFFSET + OUTER_CHEVRON_THICKNESS;

    public static void renderChevron(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        renderChevronLight1(bb, matrix3f);
        renderChevronLight2(bb, matrix3f);
        renderChevronLight3(bb, matrix3f);

        // Left 1
        renderLightStrip(bb, matrix3f,
                4F / 16, 1F / 16, 0.5F / 16,
                -6F / 16, 3.5F / 16, OUTER_CHEVRON_Z_OFFSET,
                10, 5F, 48F);

        // Left 2
        renderLightStrip(bb, matrix3f,
                3F / 16, 1F / 16, 0.5F / 16,
                -4F / 16, 2F / 16, OUTER_CHEVRON_Z_OFFSET,
                10, 6F, 52F);

        // Left 3
        renderLightStrip(bb, matrix3f,
                2F / 16, 1F / 16, 0.5F / 16,
                -2.5F / 16, 0.5F / 16, OUTER_CHEVRON_Z_OFFSET,
                10, 7F, 56F);

        // Right 1
        renderLightStrip(bb, matrix3f,
                4F / 16, 1F / 16, 0.5F / 16,
                6F / 16, 3.5F / 16, OUTER_CHEVRON_Z_OFFSET,
                -10, 21F, 48F);

        // Left 2
        renderLightStrip(bb, matrix3f,
                3F / 16, 1F / 16, 0.5F / 16,
                4F / 16, 2F / 16, OUTER_CHEVRON_Z_OFFSET,
                -10, 21F, 52F);

        // Left 3
        renderLightStrip(bb, matrix3f,
                2F / 16, 1F / 16, 0.5F / 16,
                2.5F / 16, 0.5F / 16, OUTER_CHEVRON_Z_OFFSET,
                -10, 21F, 56F);
    }

    public static void renderChevronLight1(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        Vector3f v1 = new Vector3f(-CHEVRON_LIGHT_TOP_CENTER, CHEVRON_LIGHT_TOP_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v2 = new Vector3f(-CHEVRON_LIGHT_MID1_CENTER, CHEVRON_LIGHT_MID1_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v3 = new Vector3f(CHEVRON_LIGHT_MID1_CENTER, CHEVRON_LIGHT_MID1_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v4 = new Vector3f(CHEVRON_LIGHT_TOP_CENTER, CHEVRON_LIGHT_TOP_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        Vector2f u1 = new Vector2f(13F, 48F);
        Vector2f u2 = new Vector2f(12F, 49F);
        Vector2f u3 = new Vector2f(18F, 49F);
        Vector2f u4 = new Vector2f(17F, 48F);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    public static void renderChevronLight2(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        Vector3f v1 = new Vector3f(-CHEVRON_LIGHT_MID1_CENTER, CHEVRON_LIGHT_MID1_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v2 = new Vector3f(-CHEVRON_LIGHT_MID2_CENTER, CHEVRON_LIGHT_MID2_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v3 = new Vector3f(CHEVRON_LIGHT_MID2_CENTER, CHEVRON_LIGHT_MID2_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v4 = new Vector3f(CHEVRON_LIGHT_MID1_CENTER, CHEVRON_LIGHT_MID1_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        Vector2f u1 = new Vector2f(12F, 49F);
        Vector2f u2 = new Vector2f(13.5F, 51F);
        Vector2f u3 = new Vector2f(16.5F, 51F);
        Vector2f u4 = new Vector2f(18F, 49F);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    public static void renderChevronLight3(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        Vector3f v1 = new Vector3f(-CHEVRON_LIGHT_MID2_CENTER, CHEVRON_LIGHT_MID2_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v2 = new Vector3f(-CHEVRON_LIGHT_BOTTOM_CENTER, CHEVRON_LIGHT_BOTTOM_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v3 = new Vector3f(CHEVRON_LIGHT_BOTTOM_CENTER, CHEVRON_LIGHT_BOTTOM_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);
        Vector3f v4 = new Vector3f(CHEVRON_LIGHT_MID2_CENTER, CHEVRON_LIGHT_MID2_HEIGHT, CHEVRON_LIGHT_Z_OFFSET);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        Vector2f u1 = new Vector2f(13.5F, 51F);
        Vector2f u2 = new Vector2f(14.5F, 53F);
        Vector2f u3 = new Vector2f(15.5F, 53F);
        Vector2f u4 = new Vector2f(16.5F, 51F);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    public static void renderLightStrip(ContextSimpleBuffer bb, Matrix3f matrix3f,
                                        float xSize, float ySize, float zSize,
                                        float xPos, float yPos, float zPos,
                                        float rotation, float textureX, float textureY) {
        float halfX = xSize / 2;
        float halfY = ySize / 2;

        float r1 = SGJourneyCoordinateHelper.CoordinateSystems.cartesianToPolarR(-halfX, halfY);
        float phi1 = SGJourneyCoordinateHelper.CoordinateSystems.cartesianToPolarPhi(-halfX, halfY);
        float x1 = xPos + SGJourneyCoordinateHelper.CoordinateSystems.polarToCartesianX(r1, phi1 + rotation);
        float y1 = yPos + SGJourneyCoordinateHelper.CoordinateSystems.polarToCartesianY(r1, phi1 + rotation);

        float r2 = SGJourneyCoordinateHelper.CoordinateSystems.cartesianToPolarR(-halfX, -halfY);
        float phi2 = SGJourneyCoordinateHelper.CoordinateSystems.cartesianToPolarPhi(-halfX, -halfY);
        float x2 = xPos + SGJourneyCoordinateHelper.CoordinateSystems.polarToCartesianX(r2, phi2 + rotation);
        float y2 = yPos + SGJourneyCoordinateHelper.CoordinateSystems.polarToCartesianY(r2, phi2 + rotation);

        float r3 = SGJourneyCoordinateHelper.CoordinateSystems.cartesianToPolarR(halfX, -halfY);
        float phi3 = SGJourneyCoordinateHelper.CoordinateSystems.cartesianToPolarPhi(halfX, -halfY);
        float x3 = xPos + SGJourneyCoordinateHelper.CoordinateSystems.polarToCartesianX(r3, phi3 + rotation);
        float y3 = yPos + SGJourneyCoordinateHelper.CoordinateSystems.polarToCartesianY(r3, phi3 + rotation);

        float r4 = SGJourneyCoordinateHelper.CoordinateSystems.cartesianToPolarR(halfX, halfY);
        float phi4 = SGJourneyCoordinateHelper.CoordinateSystems.cartesianToPolarPhi(halfX, halfY);
        float x4 = xPos + SGJourneyCoordinateHelper.CoordinateSystems.polarToCartesianX(r4, phi4 + rotation);
        float y4 = yPos + SGJourneyCoordinateHelper.CoordinateSystems.polarToCartesianY(r4, phi4 + rotation);

        //Front
        Vector3f v1 = new Vector3f(x1, y1, zPos);
        Vector3f v2 = new Vector3f(x2, y2, zPos);
        Vector3f v3 = new Vector3f(x3, y3, zPos);
        Vector3f v4 = new Vector3f(x4, y4, zPos);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        Vector2f u1 = new Vector2f(textureX, textureY);
        Vector2f u2 = new Vector2f(textureX, textureY - ySize * 16);
        Vector2f u3 = new Vector2f(textureX + xSize * 16, textureY - ySize * 16);
        Vector2f u4 = new Vector2f(textureX + xSize * 16, textureY);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    private UniverseChevron() {
        throw new AssertionError();
    }
}
