package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.StaticSTBHelper;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import net.minecraftforge.fml.earlydisplay.ElementShader;
import net.minecraftforge.fml.earlydisplay.QuadHelper;
import net.minecraftforge.fml.earlydisplay.SimpleBufferBuilder;
import org.jline.utils.Log;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL32C;

import java.io.FileNotFoundException;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.COLOR;

/**
 * Background element rendering static texture.
 */
public class Background implements ElementSupplier {

    private final int textureId;


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
        try {
            textureId = StaticSTBHelper.resolveAndBindTexture(texture, new int[1], new int[1]);
        } catch (FileNotFoundException e) {
            Log.error("Failed to load texture: ", e.getMessage());
            throw new InitializationException(e);
        }
    }

    /**
     * Renders the background texture using the provided buffer context.
     *
     * @param contextSimpleBuffer the buffer context for rendering.
     * @param frame               the current frame number.
     */
    @Override
    public void render(ContextSimpleBuffer contextSimpleBuffer, int frame) {
        contextSimpleBuffer.context().elementShader().updateTextureUniform(0);
        contextSimpleBuffer.context().elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
        GL32C.glBindTexture(GL11C.GL_TEXTURE_2D, textureId);
        contextSimpleBuffer.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
        QuadHelper.loadQuad(contextSimpleBuffer.simpleBufferBuilder(), 0f, contextSimpleBuffer.context().scaledWidth(), 0f, contextSimpleBuffer.context().scaledHeight(), 0f, 1f, 0f, 1f, COLOR);
        contextSimpleBuffer.simpleBufferBuilder().draw();
    }

}
