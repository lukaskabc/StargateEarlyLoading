package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import net.neoforged.fml.earlydisplay.*;

import java.util.function.Supplier;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.MEMORY_BAR_HEIGHT;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

public class MojangLogo implements Supplier<RenderElement> {
    public static final int LOGO_WIDTH = 256;
    public static final int LOGO_NEGATIVE_HEIGHT_OFFSET = 64 + 16;
    private final int textureId;
    private final int frameStart;

    public MojangLogo(int textureId, int frameStart) {
        this.textureId = textureId;
        this.frameStart = frameStart;
    }

    @Override
    public RenderElement get() {
        return RefRenderElement.constructor((bb, frame) -> {
            final RenderElement.DisplayContext ctx = bb.context();
            final int logoWidth = LOGO_WIDTH * ctx.scale();
            final int logoHeight = logoWidth / 2;
            final float x0 = (ctx.scaledWidth() - logoWidth * 2) / 2f;
            final float y0 = (ctx.scaledHeight() - logoHeight) / 2f - LOGO_NEGATIVE_HEIGHT_OFFSET + MEMORY_BAR_HEIGHT;
            ctx.elementShader().updateTextureUniform(0);
            ctx.elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
            final int fade = Math.min((frame - frameStart) * 10, 255);
            glBindTexture(GL_TEXTURE_2D, textureId);
            bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
            QuadHelper.loadQuad(bb.simpleBufferBuilder(), x0, x0 + logoWidth, y0, y0 + logoHeight, 0f, 1f, 0f, 0.5f, ColourScheme.BLACK.foreground().packedint(fade));
            QuadHelper.loadQuad(bb.simpleBufferBuilder(), x0 + logoWidth, x0 + 2 * logoWidth, y0, y0 + logoHeight, 0f, 1f, 0.5f, 1f, ColourScheme.BLACK.foreground().packedint(fade));
            bb.simpleBufferBuilder().draw();
            glBindTexture(GL_TEXTURE_2D, 0);
        });
    }
}
