package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.STBHelper;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.TextureIdentifierConstants;
import net.neoforged.fml.earlydisplay.*;
import org.jline.utils.Log;
import org.jspecify.annotations.Nullable;

import java.io.FileNotFoundException;
import java.util.function.Supplier;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.MEMORY_BAR_HEIGHT;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.MojangLogo.LOGO_NEGATIVE_HEIGHT_OFFSET;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.MojangLogo.LOGO_WIDTH;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement.INDEX_TEXTURE_OFFSET;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;

public class CenteredLogo implements Supplier<RenderElement> {
    private static final int DEFAULT_TEXTURE_SIZE = 11195;
    private final int[] textureSize;
    private int fadeOutStart = -1;

    /**
     * File path to the texture relative to the mod config directory or classpath.
     *
     * @see cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.STBHelper#resolveAndBindTexture(String, int, int)
     */
    @Nullable
    private final String texture;


    /**
     * Constructs a Centered Logo with a specified texture.
     *
     * @param texture The file path to the texture relative to the mod config directory or classpath.
     * @throws InitializationException if texture is null.
     */
    public CenteredLogo(@Nullable String texture, int[] textureSize) {
        if (texture == null) {
            throw new InitializationException("Centered logo texture is null");
        }
        this.texture = texture;
        this.textureSize = textureSize;
    }


    /**
     * Returns a RenderElement rendering the logo texture.
     *
     * @return a RenderElement for rendering the background.
     * @throws InitializationException if texture loading fails.
     */
    @Override
    public RenderElement get() {
        try {
            STBHelper.resolveAndBindTexture(texture, DEFAULT_TEXTURE_SIZE, GL_TEXTURE0 + TextureIdentifierConstants.CENTERED_LOGO + INDEX_TEXTURE_OFFSET);
        } catch (FileNotFoundException e) {
            Log.error("Failed to load texture: ", e.getMessage());
            throw new InitializationException(e);
        }

        return RefRenderElement.constructor(this::render);
    }

    /**
     * Renders the logo texture using the provided buffer context.
     * The logo is centered on the place of mojang logo.
     * The texture blends out when mojang logo appears.
     *
     * @param contextSimpleBuffer the buffer context for rendering.
     * @param frame               the current frame number.
     */
    private void render(ContextSimpleBuffer contextSimpleBuffer, int frame) {
        final RenderElement.DisplayContext ctx = contextSimpleBuffer.context();
        final SimpleBufferBuilder bb = contextSimpleBuffer.simpleBufferBuilder();
        contextSimpleBuffer.context().elementShader().updateTextureUniform(TextureIdentifierConstants.CENTERED_LOGO + INDEX_TEXTURE_OFFSET);
        contextSimpleBuffer.context().elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
        glBindTexture(GL_TEXTURE_2D, TextureIdentifierConstants.CENTERED_LOGO + INDEX_TEXTURE_OFFSET);

        bb.begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);

        final int logoWidth = LOGO_WIDTH * 2 * ctx.scale();
        final int logoHeight = (int) ((float) textureSize[1] / textureSize[0] * logoWidth);
        final float x0 = (ctx.scaledWidth() - logoWidth) / 2f;
        final float y0 = (ctx.scaledHeight() - logoHeight) / 2f - LOGO_NEGATIVE_HEIGHT_OFFSET + MEMORY_BAR_HEIGHT;

        final int fade = fadeOutStart > 0 ? Math.max(255 - (frame - fadeOutStart) * 40, 0) : 255;
        QuadHelper.loadQuad(contextSimpleBuffer.simpleBufferBuilder(), x0, x0 + logoWidth, y0, y0 + logoHeight, 0f, 1f, 0f, 1f, ColourScheme.BLACK.foreground().packedint(fade));
        bb.draw();
    }

    public void setFadeOutStart(int fadeOutStart) {
        this.fadeOutStart = fadeOutStart;
    }
}