package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.STBHelper;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
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

public class DarkSkyBackground implements Supplier<RenderElement> {
    private static final int TEXTURE_ID = 1;
    private static final int DEFAULT_TEXTURE_SIZE = 34881;
    
    @Nullable
    private final String texture;

    public DarkSkyBackground(@Nullable String texture) {
        this.texture = texture;
    }


    @Override
    public RenderElement get() {
        try {
            STBHelper.resolveAndBindTexture(texture, DEFAULT_TEXTURE_SIZE, GL_TEXTURE0 + TEXTURE_ID + INDEX_TEXTURE_OFFSET);
        } catch (FileNotFoundException e) {
            Log.error("Failed to load texture: ", e.getMessage());
            throw new InitializationException(e);
        }
        return RefRenderElement.constructor(this::render);
    }

    private void render(ContextSimpleBuffer contextSimpleBuffer, int frame) {
        contextSimpleBuffer.context().elementShader().updateTextureUniform(TEXTURE_ID + INDEX_TEXTURE_OFFSET);
        contextSimpleBuffer.context().elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
        contextSimpleBuffer.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
        QuadHelper.loadQuad(contextSimpleBuffer.simpleBufferBuilder(), 0f, contextSimpleBuffer.context().width(), 0f, contextSimpleBuffer.context().height(), 0f, 1f, 0f, 1f, COLOR);
        contextSimpleBuffer.simpleBufferBuilder().draw();
    }

}
