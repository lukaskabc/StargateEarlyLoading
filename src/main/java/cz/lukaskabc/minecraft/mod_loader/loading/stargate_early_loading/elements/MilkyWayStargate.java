package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.SGJourneyModel;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.ASSETS_DIRECTORY;

public class MilkyWayStargate {
    public static final float SCALE = 120;
    protected static final int DEFAULT_SIDES = 36;
    protected static final float DEFAULT_RADIUS = 3.5f;
    protected static final float STARGATE_RING_SHRINK = 0.001f;
    protected static final float STARGATE_RING_OUTER_RADIUS = DEFAULT_RADIUS - STARGATE_RING_SHRINK;
    protected static final float STARGATE_RING_OUTER_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_OUTER_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_OUTER_CENTER = STARGATE_RING_OUTER_LENGTH / 2;
    protected static final float STARGATE_RING_THICKNESS = 7F;
    protected static final float STARGATE_RING_OFFSET
            = STARGATE_RING_THICKNESS / 2 / 16;
    protected static final float STARGATE_RING_STOP_RADIUS = DEFAULT_RADIUS - STARGATE_RING_THICKNESS;
    protected static final float STARGATE_EDGE_TO_CUTOUT_HEIGHT = STARGATE_RING_OUTER_RADIUS - STARGATE_RING_STOP_RADIUS;
    protected static final float STARGATE_RING_STOP_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_STOP_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_STOP_CENTER = STARGATE_RING_STOP_LENGTH / 2;
    protected static final float DEFAULT_ANGLE = 360F / DEFAULT_SIDES;
    protected static final float DEFAULT_RING_HEIGHT = 1F;
    protected static final int SYMBOL_COUNT = 39;
    protected static final float SYMBOL_ANGLE = 360f / SYMBOL_COUNT;

    protected static final float STARGATE_SYMBOL_RING_OUTER_HEIGHT
            = DEFAULT_RADIUS - 6F / 16;
    protected static final float STARGATE_SYMBOL_RING_INNER_HEIGHT
            = DEFAULT_RADIUS - 14F / 16;
    protected static final float STARGATE_SYMBOL_RING_HEIGHT = STARGATE_SYMBOL_RING_OUTER_HEIGHT - STARGATE_SYMBOL_RING_INNER_HEIGHT;
    protected static final float DIVIDER_Y_CENTER = STARGATE_SYMBOL_RING_HEIGHT / 2 + STARGATE_SYMBOL_RING_INNER_HEIGHT;
    protected static final float STARGATE_RING_INNER_HEIGHT = (DEFAULT_RADIUS - (DEFAULT_RING_HEIGHT - STARGATE_RING_SHRINK)) * SCALE;
    protected static final float STARGATE_RING_START_RADIUS = (DEFAULT_RADIUS - 13F / 16) * SCALE;
    protected static final float DIVIDER_THICKNESS = 1F / 16;
    protected static final float DIVIDER_CENTER = DIVIDER_THICKNESS / 2;
    protected static final float DIVIDER_HEIGHT = 8F / 16;
    protected static final float DIVIDER_OFFSET = 0.5F / 16;
    protected static final float CHEVRON_ANGLE = 360F / 9;
    private static final int COLOR = (255 << 24) | 0xFFFFFF;
    private static final float centerX = 954f / 2;
    private static final float centerY = 947f / 2;
    private static final float STARGATE_RADIUS = (DEFAULT_RADIUS - STARGATE_RING_SHRINK) * SCALE;
    private static final float STARGATE_RING_THICK = 7f / 16f * SCALE;
    private static final float SYMBOL_TEX0 = 0;
    private static final float SYMBOL_TEX1 = 7 / 64f;
    private static final float SYMBOL_TEY0 = 42 / 64f;
    private static final float SYMBOL_TEY1 = 49 / 64f;

    public static RenderElement create() {
        return RefRenderElement.createQuad(ASSETS_DIRECTORY + "/gates/milky_way_stargate.png", 2608, 2, MilkyWayStargate::render);
    }

    public static void render(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        final double angleHalf = DEFAULT_ANGLE / 2;
        final float rotation = frame % 360;

        for (int j = 0; j < SYMBOL_COUNT; j++) {
            renderSymbolRing(bb, j, rotation);
            renderSymbolDividers(bb, j, rotation);
        }

        for (int j = 0; j < DEFAULT_SIDES; j++) {

            final double angle = Math.toRadians(-90 - DEFAULT_ANGLE * j - angleHalf);
            final double angle1 = Math.toRadians(-90 - DEFAULT_ANGLE * j + angleHalf);
            final float x = 10f * (j % 4);

            renderOuterRing(bb, angle, angle1, x);
            renderInnerRing(bb, angle, angle1, x);


        }
    }

    private static void renderSymbolDividers(final SimpleBufferBuilder bb, final int j, final float rotation) {
        final double angle = Math.toRadians(-90 - SYMBOL_ANGLE * j + SYMBOL_ANGLE / 2f + rotation - 1);
        final double angle1 = Math.toRadians(-90 - SYMBOL_ANGLE * j + SYMBOL_ANGLE / 2f + rotation - 0.5);

        final float cos = (float) Math.cos(angle);
        final float sin = (float) Math.sin(angle);
        final float cos1 = (float) Math.cos(angle1);
        final float sin1 = (float) Math.sin(angle1);

        final float x0 = SCALE * STARGATE_SYMBOL_RING_OUTER_HEIGHT * cos;
        final float y0 = SCALE * STARGATE_SYMBOL_RING_OUTER_HEIGHT * sin;
        final float x1 = SCALE * STARGATE_SYMBOL_RING_OUTER_HEIGHT * cos1;
        final float y1 = SCALE * STARGATE_SYMBOL_RING_OUTER_HEIGHT * sin1;
        final float x2 = SCALE * STARGATE_SYMBOL_RING_INNER_HEIGHT * cos;
        final float y2 = SCALE * STARGATE_SYMBOL_RING_INNER_HEIGHT * sin;
        final float x3 = SCALE * STARGATE_SYMBOL_RING_INNER_HEIGHT * cos1;
        final float y3 = SCALE * STARGATE_SYMBOL_RING_INNER_HEIGHT * sin1;

        renderTexture(bb, x0, y0, x1, y1, x2, y2, x3, y3, 10 / 64f, 43 / 64f, 10 / 64f, SYMBOL_TEY1);
    }


    private static void renderSymbolRing(final SimpleBufferBuilder bb, final int j, final float rotation) {
        final double angle = Math.toRadians(-90 - SYMBOL_ANGLE * j - SYMBOL_ANGLE / 2f + rotation);
        final double angle1 = Math.toRadians(-90 - SYMBOL_ANGLE * j + SYMBOL_ANGLE / 2f + rotation);

        final float cos = (float) Math.cos(angle);
        final float sin = (float) Math.sin(angle);
        final float cos1 = (float) Math.cos(angle1);
        final float sin1 = (float) Math.sin(angle1);

        final float x0 = SCALE * STARGATE_SYMBOL_RING_OUTER_HEIGHT * cos;
        final float y0 = SCALE * STARGATE_SYMBOL_RING_OUTER_HEIGHT * sin;
        final float x1 = SCALE * STARGATE_SYMBOL_RING_OUTER_HEIGHT * cos1;
        final float y1 = SCALE * STARGATE_SYMBOL_RING_OUTER_HEIGHT * sin1;
        final float x2 = SCALE * STARGATE_SYMBOL_RING_INNER_HEIGHT * cos;
        final float y2 = SCALE * STARGATE_SYMBOL_RING_INNER_HEIGHT * sin;
        final float x3 = SCALE * STARGATE_SYMBOL_RING_INNER_HEIGHT * cos1;
        final float y3 = SCALE * STARGATE_SYMBOL_RING_INNER_HEIGHT * sin1;

        renderTexture(bb, x0, y0, x1, y1, x2, y2, x3, y3, SYMBOL_TEX0, SYMBOL_TEY0, SYMBOL_TEX1, SYMBOL_TEY1);
    }

    private static void renderInnerRing(SimpleBufferBuilder bb, final double angle, final double angle1, final float x) {
        final float tey0 = 32 / 64f;
        final float tey1 = 35 / 64f;

        final float tex0 = x / 64f;
        final float tex1 = (x + 9) / 64f;

        final float cos = (float) Math.cos(angle);
        final float sin = (float) Math.sin(angle);
        final float cos1 = (float) Math.cos(angle1);
        final float sin1 = (float) Math.sin(angle1);

        final float x0 = STARGATE_RING_START_RADIUS * cos;
        final float y0 = STARGATE_RING_START_RADIUS * sin;
        final float x1 = STARGATE_RING_START_RADIUS * cos1;
        final float y1 = STARGATE_RING_START_RADIUS * sin1;
        final float x2 = STARGATE_RING_INNER_HEIGHT * cos;
        final float y2 = STARGATE_RING_INNER_HEIGHT * sin;
        final float x3 = STARGATE_RING_INNER_HEIGHT * cos1;
        final float y3 = STARGATE_RING_INNER_HEIGHT * sin1;

        renderTexture(bb, x0, y0, x1, y1, x2, y2, x3, y3, tex0, tey0, tex1, tey1);
    }

    private static void renderOuterRing(SimpleBufferBuilder bb, final double angle, final double angle1, final float x) {
        final float tey0 = 7 / 64f;
        final float tey1 = 14 / 64f;
        final float tex0 = x / 64f;
        final float tex1 = (x + 9) / 64f;
        final float x0 = STARGATE_RADIUS * (float) Math.cos(angle);
        final float y0 = STARGATE_RADIUS * (float) Math.sin(angle);
        final float x1 = STARGATE_RADIUS * (float) Math.cos(angle1);
        final float y1 = STARGATE_RADIUS * (float) Math.sin(angle1);
        final float x2 = (STARGATE_RADIUS - STARGATE_RING_THICK) * (float) Math.cos(angle);
        final float y2 = (STARGATE_RADIUS - STARGATE_RING_THICK) * (float) Math.sin(angle);
        final float x3 = (STARGATE_RADIUS - STARGATE_RING_THICK) * (float) Math.cos(angle1);
        final float y3 = (STARGATE_RADIUS - STARGATE_RING_THICK) * (float) Math.sin(angle1);

        renderTexture(bb, x0, y0, x1, y1, x2, y2, x3, y3, tex0, tey0, tex1, tey1);
    }

    private static void renderTexture(final SimpleBufferBuilder bb, final float x0, final float y0, final float x1, final float y1, final float x2, final float y2, final float x3, final float y3, final float tex0, final float tey0, final float tex1, final float tey1) {
        // BOTTOM LEFT
        bb.pos(centerX + x2, centerY + y2).tex(tex1, tey1).colour(COLOR).endVertex();
        // BOTTOM RIGHT
        bb.pos(centerX + x3, centerY + y3).tex(tex0, tey1).colour(COLOR).endVertex();
        // TOP LEFT
        bb.pos(centerX + x0, centerY + y0).tex(tex1, tey0).colour(COLOR).endVertex();
        // TOP RIGHT
        bb.pos(centerX + x1, centerY + y1).tex(tex0, tey0).colour(COLOR).endVertex();
    }
}
