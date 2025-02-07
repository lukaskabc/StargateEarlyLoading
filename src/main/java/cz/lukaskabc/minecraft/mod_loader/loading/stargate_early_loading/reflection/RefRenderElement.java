package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.function.Supplier;

public class RefRenderElement extends ReflectionAccessor {
    public static final Class<?> TEXTURE_RENDERER_CLASS;
    public static final Class<?> RENDERER_CLASS;
    public static final Class<?> INITIALIZER_CLASS;
    public static final int LOADING_INDEX_TEXTURE_OFFSET = 10;
    public static final int INDEX_TEXTURE_OFFSET;

    static {
        try {
            TEXTURE_RENDERER_CLASS = Class.forName("net.neoforged.fml.earlydisplay.RenderElement$TextureRenderer");
            RENDERER_CLASS = Class.forName("net.neoforged.fml.earlydisplay.RenderElement$Renderer");
            INITIALIZER_CLASS = Class.forName("net.neoforged.fml.earlydisplay.RenderElement$Initializer");
            INDEX_TEXTURE_OFFSET = (int) getFieldValue(RenderElement.class, null, "INDEX_TEXTURE_OFFSET") + LOADING_INDEX_TEXTURE_OFFSET;
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    public RefRenderElement(RenderElement target) {
        super(target, RenderElement.class);
    }


    private static RenderElement constructor(final Supplier<?> rendererInitializer) {
        try {
            return getConstructor(RenderElement.class, INITIALIZER_CLASS).newInstance(rendererInitializer);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * @return creates a new {@link net.neoforged.fml.earlydisplay.RenderElement RenderElement$Initializer}
     * @implNote You must initialize the texture
     * <p>
     * Example renderer implementation:
     * <code><pre>
     * ctx.elementShader().updateTextureUniform(textureNumber + INDEX_TEXTURE_OFFSET);
     * ctx.elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
     * bb.begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, bufferMode);
     * // rendering code
     * bb.draw();
     * </pre></code>
     */
    public static RenderElement constructor(final TextureRenderer renderer) {
        return constructor(proxyInitializer(renderer));
    }

    public static Supplier<?> proxyInitializer(TextureRenderer textureRenderer) {
        return (Supplier<?>) Proxy.newProxyInstance(INITIALIZER_CLASS.getClassLoader(),
                new Class[]{INITIALIZER_CLASS},
                (proxy, method, args) -> {
                    if (method.getName().equals("get")) {
                        return proxyRenderer(textureRenderer);
                    }
                    if (!method.canAccess(proxy)) {
                        method.setAccessible(true);
                    }
                    return method.invoke(proxy, args);
                });
    }

    private static Object proxyRenderer(TextureRenderer textureRenderer) {
        return Proxy.newProxyInstance(RENDERER_CLASS.getClassLoader(),
                new Class[]{RENDERER_CLASS},
                (renderProxy, renderMethod, renderArgs) -> {
                    if (renderMethod.getName().equals("accept")) {
                        final SimpleBufferBuilder bb = (SimpleBufferBuilder) renderArgs[0];
                        final RenderElement.DisplayContext ctx = (RenderElement.DisplayContext) renderArgs[1];
                        final int frame = (int) renderArgs[2];
                        textureRenderer.accept(new ContextSimpleBuffer(bb, ctx), frame);
                        return null;
                    }
                    if (!renderMethod.canAccess(renderProxy)) {
                        renderMethod.setAccessible(true);
                    }
                    return renderMethod.invoke(renderProxy, renderArgs);
                });
    }

    public interface TextureRenderer {
        void accept(ContextSimpleBuffer contextSimpleBuffer, int frame);
    }

}
