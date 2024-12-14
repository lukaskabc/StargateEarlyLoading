package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.STBHelper;
import net.neoforged.fml.earlydisplay.ElementShader;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;

public class RefRenderElement extends ReflectionAccessor {
    public static final Class<?> TEXTURE_RENDERER_CLASS;
    public static final Class<?> RENDERER_CLASS;
    public static final Class<?> INITIALIZER_CLASS;

    static {
        try {
            TEXTURE_RENDERER_CLASS = Class.forName("net.neoforged.fml.earlydisplay.RenderElement$TextureRenderer");
            RENDERER_CLASS = Class.forName("net.neoforged.fml.earlydisplay.RenderElement$Renderer");
            INITIALIZER_CLASS = Class.forName("net.neoforged.fml.earlydisplay.RenderElement$Initializer");
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    public RefRenderElement(RenderElement target) {
        super(target, RenderElement.class);
    }

    public static RenderElement create(final String textureFileName, int size, int textureNumber, TextureRenderer positionAndColour) {
        try {
            return constructor(initializeTexture(textureFileName, size, textureNumber, positionAndColour));
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        }
    }

    private static RenderElement constructor(final Supplier<?> rendererInitializer) {
        try {
            return getConstructor(RenderElement.class, INITIALIZER_CLASS).newInstance(rendererInitializer);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Supplier<?> initializeTexture(final String textureFileName, int size, int textureNumber, TextureRenderer positionAndColour) throws NoSuchMethodException {
        return (Supplier<?>) Proxy.newProxyInstance(INITIALIZER_CLASS.getClassLoader(),
                new Class[]{INITIALIZER_CLASS},
                (proxy, method, args) -> {
                    if (method.getName().equals("get")) {
                        return createInternalRenderer(textureFileName, size, textureNumber, positionAndColour);
                    }
                    if (!method.canAccess(proxy)) {
                        method.setAccessible(true);
                    }
                    return method.invoke(proxy, args);
                });
    }

    private static Object createInternalRenderer(final String textureFileName, int size, int textureNumber, TextureRenderer positionAndColour) {
        final int INDEX_TEXTURE_OFFSET = (int) getFieldValue(RenderElement.class, null, "INDEX_TEXTURE_OFFSET");
        final int[] imgSize = STBHelper.loadTextureFromClasspath(textureFileName, size, GL_TEXTURE0 + textureNumber + INDEX_TEXTURE_OFFSET);
        final Method renderTexture = getMethod(RenderElement.class, "renderTexture", SimpleBufferBuilder.class, RenderElement.DisplayContext.class, int.class, int[].class, TEXTURE_RENDERER_CLASS);
        final Object proxiedTextureRenderer = proxyTextureRenderer(positionAndColour);
        return Proxy.newProxyInstance(RENDERER_CLASS.getClassLoader(),
                new Class[]{RENDERER_CLASS},
                (renderProxy, renderMethod, renderArgs) -> {
                    if (renderMethod.getName().equals("accept")) {

                        final SimpleBufferBuilder bb = (SimpleBufferBuilder) renderArgs[0];
                        final RenderElement.DisplayContext ctx = (RenderElement.DisplayContext) renderArgs[1];
                        final int frame = (int) renderArgs[2];
                        // from RenderElement#Initializer
                        ctx.elementShader().updateTextureUniform(textureNumber + INDEX_TEXTURE_OFFSET);
                        ctx.elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
                        try {
                            return renderTexture.invoke(null, bb, ctx, frame, imgSize, proxiedTextureRenderer);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            throw new ReflectionException(e);
                        }
                    }
                    if (!renderMethod.canAccess(renderProxy)) {
                        renderMethod.setAccessible(true);
                    }
                    return renderMethod.invoke(renderProxy, renderArgs);
                });
    }

    private static Object proxyTextureRenderer(TextureRenderer textureRenderer) {
        return Proxy.newProxyInstance(TEXTURE_RENDERER_CLASS.getClassLoader(),
                new Class[]{TEXTURE_RENDERER_CLASS},
                (proxy, method, args) -> {
                    if (method.getName().equals("accept")) {
                        final SimpleBufferBuilder bb = (SimpleBufferBuilder) args[0];
                        final RenderElement.DisplayContext ctx = (RenderElement.DisplayContext) args[1];
                        final int[] size = (int[]) args[2];
                        final int frame = (int) args[3];
                        textureRenderer.accept(bb, ctx, size, frame);
                        return null;
                    }
                    if (!method.canAccess(proxy)) {
                        method.setAccessible(true);
                    }
                    return method.invoke(proxy, args);
                });
    }

    public interface TextureRenderer {
        void accept(SimpleBufferBuilder bb, RenderElement.DisplayContext context, int[] size, int frame);
    }

}
