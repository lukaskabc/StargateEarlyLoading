package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.STBHelper;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.TextureIdentifierConstants;
import net.neoforged.fml.earlydisplay.ElementShader;
import net.neoforged.fml.earlydisplay.QuadHelper;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;
import org.jline.utils.Log;
import org.jspecify.annotations.Nullable;

import java.io.FileNotFoundException;
import java.util.function.Supplier;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement.INDEX_TEXTURE_OFFSET;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.COLOR;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;

/**
 * Background element rendering static texture.
 */
public class Background implements Supplier<RenderElement> {
    private static final int DEFAULT_TEXTURE_SIZE = 34881;

    /**
     * File path to the texture relative to the mod config directory or classpath.
     *
     * @see STBHelper#resolveAndBindTexture(String, int, int)
     */
    @Nullable
    private final String texture;


    /**
     * Constructs a Background with a specified texture.
     *
     * @param texture The file path to the texture relative to the mod config directory or classpath.
     * @throws InitializationException if texture is null.
     */
    public Background(@Nullable String texture) {
        if (texture == null) {
            throw new InitializationException("Background texture is null");
        }
        this.texture = texture;
    }


    /**
     * Returns a RenderElement rendering the background texture.
     *
     * @return a RenderElement for rendering the background.
     * @throws InitializationException if texture loading fails.
     */
    @Override
    public RenderElement get() {
        try {
            STBHelper.resolveAndBindTexture(texture, DEFAULT_TEXTURE_SIZE, GL_TEXTURE0 + TextureIdentifierConstants.BACKGROUND + INDEX_TEXTURE_OFFSET);
        } catch (FileNotFoundException e) {
            Log.error("Failed to load texture: ", e.getMessage());
            throw new InitializationException(e);
        }
        return RefRenderElement.constructor(this::render);
    }

    /**
     * Renders the background texture using the provided buffer context.
     *
     * @param contextSimpleBuffer the buffer context for rendering.
     * @param frame               the current frame number.
     */
    private void render(ContextSimpleBuffer contextSimpleBuffer, int frame) {
        contextSimpleBuffer.context().elementShader().updateTextureUniform(TextureIdentifierConstants.BACKGROUND + INDEX_TEXTURE_OFFSET);
        contextSimpleBuffer.context().elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
        contextSimpleBuffer.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
        QuadHelper.loadQuad(contextSimpleBuffer.simpleBufferBuilder(), 0f, contextSimpleBuffer.context().scaledWidth(), 0f, contextSimpleBuffer.context().scaledHeight(), 0f, 1f, 0f, 1f, COLOR);
        contextSimpleBuffer.simpleBufferBuilder().draw();
    }

}
