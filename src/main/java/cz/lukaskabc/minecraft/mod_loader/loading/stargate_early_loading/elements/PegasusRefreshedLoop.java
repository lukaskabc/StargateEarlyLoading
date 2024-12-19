package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import net.neoforged.fml.earlydisplay.QuadHelper;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.util.List;
import java.util.Set;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.ASSETS_DIRECTORY;

public class PegasusRefreshedLoop {
    private static final int FRAME_COUNT = 36;
    private static final int COLOR = (255 << 24) | 0xFFFFFF;

    public static List<RenderElement> create() {
        return List.of(
                staticGate(),
                symbolAnimation()
        );
    }

    public static RenderElement staticGate() {
        return RefRenderElement.createQuad(ASSETS_DIRECTORY + "/gates/pegasus_refreshed/base.png", 445969, 2, PegasusRefreshedLoop::renderStaticGate);
    }

    public static RenderElement symbolAnimation() {
        return RefRenderElement.createQuad(ASSETS_DIRECTORY + "/gates/pegasus_refreshed/symbols.png", 188598, 3, PegasusRefreshedLoop::renderSymbolAnimation);
    }

    public static void renderSymbolAnimation(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        float widthScale = (float) ctx.width() / imgSize[0];
        float heightScale = (float) ctx.height() / imgSize[1];
        float scale = Math.min(widthScale, heightScale);

        float x0 = Math.abs((ctx.width() - imgSize[0]) / 2);
        float y0 = Math.abs((ctx.height() - imgSize[1]) / 2);

        int symbolId = frame % FRAME_COUNT;

        renderSymbol(bb, symbolId, x0, y0);
    }

public static void renderSymbol(SimpleBufferBuilder bb, int symbol, float baseX0, float baseY0) {
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
    final float y0 = centerY + outerRadius * (float) Math.sin(angle) ;
    final float x1 = centerX + outerRadius * (float) Math.cos(angle + angleSize);
    final float y1 = centerY + outerRadius * (float) Math.sin(angle + angleSize);

    final float x2 = centerX + innerRadius * (float) Math.cos(angle);
    final float y2 = centerY + innerRadius * (float) Math.sin(angle);
    final float x3 = centerX + innerRadius * (float) Math.cos(angle + angleSize);
    final float y3 = centerY + innerRadius * (float) Math.sin(angle + angleSize);

    bb.pos(baseX0 + x0, baseY0 + y0).tex(x0 / width, y0 / height).colour(COLOR).endVertex();
    bb.pos(baseX0 + x1, baseY0 + y1).tex(x1 / width, y1 / height).colour(COLOR).endVertex();
    bb.pos(baseX0 + x2, baseY0 + y2).tex(x2 / width, y2 / height).colour(COLOR).endVertex();
    bb.pos(baseX0 + x3, baseY0 + y3).tex(x3 / width, y3 / height).colour(COLOR).endVertex();
}

    public static void renderStaticGate(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        float widthScale = (float) ctx.width() / imgSize[0];
        float heightScale = (float) ctx.height() / imgSize[1];
        float scale = Math.min(widthScale, heightScale);

        float x1 = Math.min(imgSize[0] * scale, imgSize[0]);
        float y1 = Math.min(imgSize[1] * scale, imgSize[1]);

        float x0 = (ctx.width() - x1) / 2;
        float y0 = (ctx.height() - y1) / 2;

        QuadHelper.loadQuad(bb, x0, x0 + x1, y0, y0 + y1, 0f, 1f, 0f, 1f, COLOR);
    }
}
