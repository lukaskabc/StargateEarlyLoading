package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.ReflectionException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.neoforged.fml.earlydisplay.ElementShader;
import net.neoforged.fml.earlydisplay.QuadHelper;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import java.util.function.Supplier;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.ASSETS_DIRECTORY;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.globalAlpha;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

public class DarkSkyBackground {
    private static final String TEXTURE = "dark_sky.png";
    //private static final RenderElement element = RefRenderElement.create(ASSETS_DIRECTORY + "/" + "fox_running.png", 128000, 2, DarkSkyBackground::foxRender);

    public static RenderElement create() {
        return RefRenderElement.create(ASSETS_DIRECTORY + "/" + TEXTURE, 128000, 3, DarkSkyBackground::render);
    }

    private static void render(SimpleBufferBuilder bb, RenderElement.DisplayContext ctx, int[] imgSize, int frame) {
        ctx.elementShader().updateTextureUniform(0);
        ctx.elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
        glBindTexture(GL_TEXTURE_2D, 3);
        bb.begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
        QuadHelper.loadQuad(bb, 0f, 0f, 1080, 1920f, 0f, 1f, 0f, 0.5f, (255 << 24) | 0xFFFFFF);
        bb.draw();
        glBindTexture(GL_TEXTURE_2D, 0);

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
