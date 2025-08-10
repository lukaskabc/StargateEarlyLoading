package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;


import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import net.minecraftforge.fml.earlydisplay.RenderElement;

import java.util.function.Supplier;

/**
 * Supplier providing new instance of RenderElement.
 */
public interface ElementSupplier extends Supplier<RenderElement> {
    /**
     * Called on each frame to render the element.
     *
     * @param csb   FrameBuffer with context
     * @param frame frame number
     */
    void render(ContextSimpleBuffer csb, int frame);

    /**
     * Called before constructing the RenderElement.
     */
    default void initialize() {

    }

    /**
     * Returns a RenderElement rendering using {@link #render(ContextSimpleBuffer, int)}
     * <p>
     * Calls {@link #initialize()}.
     *
     * @return a RenderElement for rendering the element
     */
    @Override
    default RenderElement get() {
        initialize();
        return RefRenderElement.constructor(this::render);
    }
}
