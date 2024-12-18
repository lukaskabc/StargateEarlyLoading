package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import net.neoforged.fml.earlydisplay.QuadHelper;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import java.util.List;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.ASSETS_DIRECTORY;

public class PegasusRefreshedLoop {
    private static final int FRAME_COUNT = 13;
    public static List<RenderElement> create() {
        return List.of(
                staticGate(),
                symbolAnimation()
        );
    }

    public static RenderElement staticGate() {
        return RefRenderElement.create(ASSETS_DIRECTORY + "/gates/pegasus_refreshed/base.png", 446000, 2, PegasusRefreshedLoop::renderStaticGate);
    }

    public static RenderElement symbolAnimation() {
        return RefRenderElement.create(ASSETS_DIRECTORY + "/gates/pegasus_refreshed/frames-dev.png", 500000, 3, PegasusRefreshedLoop::renderSymbolAnimation);
    }

    public static void renderSymbolAnimation(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        float widthScale = (float) ctx.width() / imgSize[0];
        float frameHeight = (float) imgSize[1] / FRAME_COUNT;
        float heightScale = ctx.height() / frameHeight;
        float scale = Math.min(widthScale, heightScale);

        float x1 = Math.min(imgSize[0] * scale, imgSize[0]);
        float y1 = Math.min(frameHeight * scale, frameHeight);

        float x0 = (ctx.width() - x1) / 2;
        float y0 = (ctx.height() - y1) / 2;

        int frameId = (frame / 2) % FRAME_COUNT;
        float frameSize = 1f / FRAME_COUNT;
        float framePos = frameId * frameSize;

        QuadHelper.loadQuad(bb, x0, x0 + x1, y0, y0 + y1, 0f, 1f, framePos, framePos + frameSize, (255 << 24) | 0xFFFFFF);
    }

    public static void renderStaticGate(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        float widthScale = (float) ctx.width() / imgSize[0];
        float heightScale = (float) ctx.height() / imgSize[1];
        float scale = Math.min(widthScale, heightScale);

        float x1 = Math.min(imgSize[0] * scale, imgSize[0]);
        float y1 = Math.min(imgSize[1] * scale, imgSize[1]);

        float x0 = (ctx.width() - x1) / 2;
        float y0 = (ctx.height() - y1) / 2;

        QuadHelper.loadQuad(bb, x0, x0 + x1, y0, y0 + y1, 0f, 1f, 0f, 1f, (255 << 24) | 0xFFFFFF);
    }
}
