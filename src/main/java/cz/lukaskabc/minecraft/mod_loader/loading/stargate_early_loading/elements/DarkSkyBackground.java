package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import net.neoforged.fml.earlydisplay.QuadHelper;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.globalAlpha;

public class DarkSkyBackground {
    private static final String TEXTURE = "dark_sky.png";

//    public static RenderElement create() {
//        return RefRenderElement.createQuad(ASSETS_DIRECTORY + "/" + TEXTURE, 34881, 1, DarkSkyBackground::render);
//    }

    private static void render(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        float widthScale = (float) ctx.width() / imgSize[0];
        float heightScale = (float) ctx.height() / imgSize[1];
        float scale = Math.max(widthScale, heightScale);
        float x1 = imgSize[0] * scale;
        float y1 = imgSize[1] * scale;
        QuadHelper.loadQuad(bb, 0f, x1, 0f, y1, 0f, 1f, 0f, 1f, (255 << 24) | 0xFFFFFF);
    }

    private static void foxRender(SimpleBufferBuilder bb, RenderElement.DisplayContext context, int[] size, int frame) {
        int framecount = 28;
        float aspect = size[0] * (float) framecount / size[1];
        int outsize = size[0];
        int offset = outsize / 6;
        var x0 = context.scaledWidth() - outsize * context.scale() + offset;
        var x1 = context.scaledWidth() + offset;
        var y0 = context.scaledHeight() - outsize * context.scale() / aspect + 4 - 25;
        var y1 = context.scaledHeight() + 4 - 25;
        int frameidx = frame % framecount;
        float framesize = 1 / (float) framecount;
        float framepos = frameidx * framesize;
        QuadHelper.loadQuad(bb, x0, x1, y0, y1, 0f, 1f, framepos, framepos + framesize, globalAlpha << 24 | 0xFFFFFF);
    }

}
