package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import net.neoforged.fml.earlydisplay.QuadHelper;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.ASSETS_DIRECTORY;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.globalAlpha;

public class DarkSkyBackground {
    private static final String TEXTURE = "dark_sky.png";
    private static final RenderElement element = RefRenderElement.create(ASSETS_DIRECTORY + "/" + "fox_running.png", 128000, 2, DarkSkyBackground::foxRender);

    public static RenderElement create() {
        return element;
    }

    private static void render(SimpleBufferBuilder bb, RenderElement.DisplayContext context, int[] size, int frame) {
        var inset = 0f;
        var x0 = inset;
        var x1 = inset + size[0];
        var y0 = inset;
        var y1 = inset + size[1];
        QuadHelper.loadQuad(bb, x0, x1, y0, y1, 0f, 1f, 0f, 1f, globalAlpha << 24 | 0xFFFFFF);
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
