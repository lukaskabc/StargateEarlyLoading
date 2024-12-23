package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.SGJourneyModel;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;
import org.joml.Matrix2f;
import org.joml.Vector2f;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.renderTexture;

public abstract class GenericStargate {
    public static final float SCALE = 120;
    protected static final float DEFAULT_RADIUS = 3.5F;
    protected static final int DEFAULT_SIDES = 36;
    protected static final float DEFAULT_RING_HEIGHT = 1F;
    protected static final float STARGATE_RING_SHRINK = 0.001F;

    protected static final float DEFAULT_ANGLE = 360F / DEFAULT_SIDES;
    protected static final float NUMBER_OF_CHEVRONS = 9;
    protected static final float CHEVRON_ANGLE = 360F / 9;

    // Ring
    protected static final float STARGATE_RING_THICKNESS = 7F;
    protected static final float STARGATE_RING_OFFSET = STARGATE_RING_THICKNESS / 2 / 16;

    protected static final float STARGATE_RING_OUTER_RADIUS = DEFAULT_RADIUS - STARGATE_RING_SHRINK;
    protected static final float STARGATE_RING_OUTER_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_OUTER_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_OUTER_CENTER = STARGATE_RING_OUTER_LENGTH / 2;

    protected static final float STARGATE_RING_STOP_RADIUS = DEFAULT_RADIUS - 7F / 16;
    protected static final float STARGATE_RING_STOP_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_STOP_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_STOP_CENTER = STARGATE_RING_STOP_LENGTH / 2;
    protected static final float STARGATE_EDGE_TO_CUTOUT_HEIGHT = STARGATE_RING_OUTER_RADIUS - STARGATE_RING_STOP_RADIUS;
    protected static final float STARGATE_RING_START_RADIUS = DEFAULT_RADIUS - 13F / 16;
    protected static final float STARGATE_RING_START_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_START_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_START_CENTER = STARGATE_RING_START_LENGTH / 2;
    protected static final float STARGATE_RING_CUTOUT_HEIGHT = STARGATE_RING_STOP_RADIUS - STARGATE_RING_START_RADIUS;
    protected static final float STARGATE_RING_INNER_HEIGHT = DEFAULT_RADIUS - (DEFAULT_RING_HEIGHT - STARGATE_RING_SHRINK);
    protected static final float STARGATE_RING_INNER_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_INNER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_INNER_CENTER = STARGATE_RING_INNER_LENGTH / 2;
    protected static final float STARGATE_RING_HEIGHT = STARGATE_RING_OUTER_RADIUS - STARGATE_RING_INNER_HEIGHT;
    protected static final float STARGATE_CUTOUT_TO_INNER_HEIGHT = STARGATE_RING_START_RADIUS - STARGATE_RING_INNER_HEIGHT;
    protected static final float STARGATE_SYMBOL_RING_OUTER_HEIGHT = DEFAULT_RADIUS - 6F / 16;
    protected static final float STARGATE_SYMBOL_RING_INNER_HEIGHT = DEFAULT_RADIUS - 14F / 16;
    protected static final float STARGATE_SYMBOL_RING_HEIGHT = STARGATE_SYMBOL_RING_OUTER_HEIGHT - STARGATE_SYMBOL_RING_INNER_HEIGHT;
    protected static final float DIVIDER_Y_CENTER = STARGATE_SYMBOL_RING_HEIGHT / 2 + STARGATE_SYMBOL_RING_INNER_HEIGHT;
    protected static final float DIVIDER_THICKNESS = 1F / 16;
    protected static final float DIVIDER_CENTER = DIVIDER_THICKNESS / 2;
    protected static final float DIVIDER_HEIGHT = 8F / 16;
    protected static final float DIVIDER_OFFSET = 0.5F / 16;
    private static final Vector2f CENTER = new Vector2f(954f / 2, 947f / 2);
    protected final int symbolCount;
    protected final float symbolAngle;
    protected final float stargateSymbolRingOuterLength;
    protected final float stargateSymbolRingOuterCenter;
    protected final float stargateSymbolRingInnerLength;
    protected final float stargateSymbolRingInnerCenter;

    protected GenericStargate(short symbolCount) {
        this.symbolCount = symbolCount;
        this.symbolAngle = 360F / symbolCount;

        this.stargateSymbolRingOuterLength = SGJourneyModel.getUsedWidth(symbolCount, STARGATE_SYMBOL_RING_OUTER_HEIGHT, DEFAULT_RADIUS);
        this.stargateSymbolRingOuterCenter = stargateSymbolRingOuterLength / 2;

        this.stargateSymbolRingInnerLength = SGJourneyModel.getUsedWidth(symbolCount, STARGATE_SYMBOL_RING_INNER_HEIGHT, DEFAULT_RADIUS);
        this.stargateSymbolRingInnerCenter = stargateSymbolRingInnerLength / 2;
    }

    private static void renderInnerRing(final SimpleBufferBuilder bb, final Matrix2f matrix2f, final int j) {
        final float texBase = 10f * (j % 4) + 5;

        Vector2f v1 = new Vector2f(-STARGATE_RING_START_CENTER, STARGATE_RING_START_RADIUS);
        Vector2f v2 = new Vector2f(-STARGATE_RING_INNER_CENTER, STARGATE_RING_INNER_HEIGHT);
        Vector2f v3 = new Vector2f(STARGATE_RING_INNER_CENTER, STARGATE_RING_INNER_HEIGHT);
        Vector2f v4 = new Vector2f(STARGATE_RING_START_CENTER, STARGATE_RING_START_RADIUS);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(texBase - STARGATE_RING_START_CENTER * 16, 33.5F + STARGATE_CUTOUT_TO_INNER_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(texBase + STARGATE_RING_INNER_CENTER * 16, 33.5F + STARGATE_CUTOUT_TO_INNER_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(texBase - STARGATE_RING_INNER_CENTER * 16, 33.5F - STARGATE_CUTOUT_TO_INNER_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(texBase + STARGATE_RING_START_CENTER * 16, 33.5F - STARGATE_CUTOUT_TO_INNER_HEIGHT / 2 * 16);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    private static void renderOuterRing(final SimpleBufferBuilder bb, final Matrix2f matrix2f, final int j) {
        final float texBase = 10F * (j % 4) + 5;

        // TOP LEFT
        Vector2f v1 = new Vector2f(-STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);
        // BOTTOM LEFT
        Vector2f v2 = new Vector2f(-STARGATE_RING_STOP_CENTER, STARGATE_RING_STOP_RADIUS);
        // BOTTOM RIGHT
        Vector2f v3 = new Vector2f(STARGATE_RING_STOP_CENTER, STARGATE_RING_STOP_RADIUS);
        // TOP RIGHT
        Vector2f v4 = new Vector2f(STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(texBase - STARGATE_RING_STOP_CENTER * 16, 10.5F + STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(texBase + STARGATE_RING_STOP_CENTER * 16, 10.5F + STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(texBase - STARGATE_RING_OUTER_CENTER * 16, 10.5F - STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(texBase + STARGATE_RING_OUTER_CENTER * 16, 10.5F - STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }


    public void render(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        final float rotation = ((frame / 5f) % 360) / 156f * 360F;

        Matrix2f matrix2f = new Matrix2f();
        matrix2f.scale(SCALE);
        matrix2f.rotate((float) Math.toRadians(180));
        for (int j = 0; j < symbolCount; j++) {
            renderSymbolRing(bb, matrix2f, j, rotation);
            renderSymbolDividers(bb, matrix2f, j, rotation);
        }

        for (int j = 0; j < DEFAULT_SIDES; j++) {
            Matrix2f m = new Matrix2f(matrix2f);
            m.rotate((float) Math.toRadians(j * -DEFAULT_ANGLE));
            renderOuterRing(bb, m, j);
            renderInnerRing(bb, m, j);
        }
    }

    protected void renderSymbolDividers(SimpleBufferBuilder bb, Matrix2f m, int j, float rotation) {
        Matrix2f matrix2f = new Matrix2f(m);
        matrix2f.rotate((float) Math.toRadians(j * -symbolAngle - symbolAngle / 2 + rotation));

        Vector2f v1 = new Vector2f(-DIVIDER_CENTER, DIVIDER_Y_CENTER + DIVIDER_HEIGHT / 2);
        Vector2f v2 = new Vector2f(-DIVIDER_CENTER, DIVIDER_Y_CENTER - DIVIDER_HEIGHT / 2);
        Vector2f v3 = new Vector2f(DIVIDER_CENTER, DIVIDER_Y_CENTER - DIVIDER_HEIGHT / 2);
        Vector2f v4 = new Vector2f(DIVIDER_CENTER, DIVIDER_Y_CENTER + DIVIDER_HEIGHT / 2);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(9.5F - DIVIDER_CENTER * 16, 46 + DIVIDER_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(9.5F + DIVIDER_CENTER * 16, 46 + DIVIDER_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(9.5F - DIVIDER_CENTER * 16, 46 - DIVIDER_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(9.5F + DIVIDER_CENTER * 16, 46 - DIVIDER_HEIGHT / 2 * 16);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    protected void renderSymbolRing(SimpleBufferBuilder bb, Matrix2f m, int j, float rotation) {
        Matrix2f matrix2f = new Matrix2f(m);
        matrix2f.rotate((float) Math.toRadians(j * -symbolAngle + rotation));

        Vector2f v1 = new Vector2f(-stargateSymbolRingOuterCenter, STARGATE_SYMBOL_RING_OUTER_HEIGHT);
        Vector2f v2 = new Vector2f(-stargateSymbolRingInnerCenter, STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v3 = new Vector2f(stargateSymbolRingInnerCenter, STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v4 = new Vector2f(stargateSymbolRingOuterCenter, STARGATE_SYMBOL_RING_OUTER_HEIGHT);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(4 - stargateSymbolRingOuterCenter * 16, 46 + STARGATE_SYMBOL_RING_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(4 + stargateSymbolRingInnerCenter * 16, 46 + STARGATE_SYMBOL_RING_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(4 - stargateSymbolRingInnerCenter * 16, 46 - STARGATE_SYMBOL_RING_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(4 + stargateSymbolRingOuterCenter * 16, 46 - STARGATE_SYMBOL_RING_HEIGHT / 2 * 16);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    protected void renderSymbols(SimpleBufferBuilder bb) {

    }
}
