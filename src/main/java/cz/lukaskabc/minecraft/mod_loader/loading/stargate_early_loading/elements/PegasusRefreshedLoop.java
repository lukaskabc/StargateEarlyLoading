package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import net.neoforged.fml.earlydisplay.QuadHelper;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import java.util.List;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.ASSETS_DIRECTORY;

public class PegasusRefreshedLoop {
    private static final int SYMBOL_SIZE = 16;
    private static final int PADDING = 15;
    private static final int SYMBOL_COUNT = 36;
    private static final int COLOR = (255 << 24) | 0xFFFFFF;
    private static final int SYMBOL_COLOR = (255 << 24) | 0xFF9B37;

    private static final int SYMBOL_TOP_LEFT = 446;
    private static final int SYMBOL_TOP_RIGHT = 507;
    private static final int SYMBOL_BOTTOM_LEFT = 450;
    private static final int SYMBOL_BOTTOM_RIGHT = 503;
    private static final float SYMBOL_TOP = 121;
    private static final float SYMBOL_BOTTOM = 76;

    private static float baseX = 0;
    private static float baseY = 0;

    public static List<RenderElement> create() {
        return List.of(
                staticGate(),
                symbolAnimation()
        );
    }

    public static RenderElement staticGate() {
        return RefRenderElement.createQuad(ASSETS_DIRECTORY + "/gates/pegasus_refreshed/base.png", 69182, 2, PegasusRefreshedLoop::renderStaticGate);
    }

    public static RenderElement symbolAnimation() {
        return RefRenderElement.createQuad(ASSETS_DIRECTORY + "/gates/pegasus_refreshed/lantea.png", 2057, 3, PegasusRefreshedLoop::renderSymbolAnimation);
    }

    public static void renderSymbolAnimation(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        int symbolId = (frame / 10) % SYMBOL_COUNT;
        renderSymbol(bb, symbolId, imgSize);
    }

    public static void renderSymbol(SimpleBufferBuilder bb, int symbol, int[] imgSize) {

        final int symbolX = symbol * SYMBOL_SIZE;
        final float symbolLeft = 1f / imgSize[0] * symbolX;
        final float symbolRight = 1f / imgSize[0] * (symbolX + SYMBOL_SIZE);

        bb.pos(baseX + SYMBOL_TOP_LEFT, baseY + SYMBOL_TOP).tex(symbolLeft, 0).colour(SYMBOL_COLOR).endVertex();
        bb.pos(baseX + SYMBOL_TOP_RIGHT, baseY + SYMBOL_TOP).tex(symbolRight, 0).colour(SYMBOL_COLOR).endVertex();
        bb.pos(baseX + SYMBOL_BOTTOM_LEFT, baseY + SYMBOL_BOTTOM).tex(symbolLeft, 1).colour(SYMBOL_COLOR).endVertex();
        bb.pos(baseX + SYMBOL_BOTTOM_RIGHT, baseY + SYMBOL_BOTTOM).tex(symbolRight, 1).colour(SYMBOL_COLOR).endVertex();

        /*

        final float width = 840f;
        final float height = 834f;
        final float topTopSymbolY = 63f;
        final float bottomTopSymbolY = 110f;
        final float centerX = width / 2;
        final float centerY = height / 2;
        final float outerRadius = Math.abs(topTopSymbolY - centerY - 10);
        final float innerRadius = Math.abs(bottomTopSymbolY - centerY + 5);

        final double angle = Math.toRadians((356 + (10 * symbol)) % 360);
        final double angleSize = Math.toRadians(10); // 10 = 360 / FRAME_COUNT    // FRAME_COUNT = Symbol count

        final float x0 = centerX + outerRadius * (float) Math.cos(angle);
        final float y0 = centerY + outerRadius * (float) Math.sin(angle);
        final float x1 = centerX + outerRadius * (float) Math.cos(angle + angleSize);
        final float y1 = centerY + outerRadius * (float) Math.sin(angle + angleSize);

        final float x2 = centerX + innerRadius * (float) Math.cos(angle);
        final float y2 = centerY + innerRadius * (float) Math.sin(angle);
        final float x3 = centerX + innerRadius * (float) Math.cos(angle + angleSize);
        final float y3 = centerY + innerRadius * (float) Math.sin(angle + angleSize);

        bb.pos(baseX0 + x0, baseY0 + y0).tex(x0 / width, y0 / height).colour(COLOR).endVertex();
        bb.pos(baseX0 + x1, baseY0 + y1).tex(x1 / width, y1 / height).colour(COLOR).endVertex();
        bb.pos(baseX0 + x2, baseY0 + y2).tex(x2 / width, y2 / height).colour(COLOR).endVertex();
        bb.pos(baseX0 + x3, baseY0 + y3).tex(x3 / width, y3 / height).colour(COLOR).endVertex();*/
    }

    public static void renderStaticGate(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        float widthScale = (float) ctx.width() / imgSize[0];
        float heightScale = (float) ctx.height() / imgSize[1];
        float scale = Math.min(widthScale, heightScale);

        float x1 = Math.min(imgSize[0] * scale, imgSize[0]);
        float y1 = Math.min(imgSize[1] * scale, imgSize[1]);

        baseX = (ctx.width() - x1) / 2;
        baseY = (ctx.height() - y1) / 2;

        QuadHelper.loadQuad(bb, baseX, baseX + x1, baseY, baseY + y1, 0f, 1f, 0f, 1f, COLOR);
    }
}
