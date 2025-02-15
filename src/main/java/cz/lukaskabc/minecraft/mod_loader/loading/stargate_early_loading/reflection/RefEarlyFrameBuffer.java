package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import net.neoforged.fml.earlydisplay.RenderElement;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class RefEarlyFrameBuffer extends ReflectionAccessor {
    private static final Class<?> FRAME_BUFFER_CLASS = findClass("net.neoforged.fml.earlydisplay.EarlyFramebuffer");
    private static final MethodHandles.Lookup lookup = privateLookup(FRAME_BUFFER_CLASS);
    private static final MethodHandle constructor = findConstructor(lookup, RenderElement.DisplayContext.class);
    private static final MethodHandle close = findVirtual(lookup, "close", void.class);

    private RefEarlyFrameBuffer(Object earlyFramebuffer) {
        super(earlyFramebuffer, FRAME_BUFFER_CLASS);
        throw new AssertionError();
    }

    public static Object constructor(RenderElement.DisplayContext context) {
        try {
            return constructor.invoke(context);
        } catch (Throwable e) {
            throw new ReflectionException(e);
        }
    }

    public static void close(Object earlyFramebuffer) {
        try {
            close.invoke(earlyFramebuffer);
        } catch (Throwable e) {
            throw new ReflectionException(e);
        }
    }
}
