package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import net.neoforged.fml.earlydisplay.QuadHelper;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.ASSETS_DIRECTORY;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.globalAlpha;

public class DarkSkyBackground {
    private static final String TEXTURE = "dark_sky.png";

    public static RenderElement create() {
        return RefRenderElement.create(ASSETS_DIRECTORY + "/" + TEXTURE, 34881, 1, DarkSkyBackground::render);
    }

    private static void render(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        QuadHelper.loadQuad(bb, 0f, ctx.width(), 0f, ctx.height(), 0f, 1f, 0f, 1f, (255 << 24) | 0xFFFFFF);
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
