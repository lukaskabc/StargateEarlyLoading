package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import net.neoforged.fml.earlydisplay.ColourScheme;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleFont;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * {@link ReflectionAccessor} for {@link DisplayWindow}.
 */
public class RefDisplayWindow extends ReflectionAccessor {
    public RefDisplayWindow(DisplayWindow displayWindow) {
        super(displayWindow, DisplayWindow.class);
    }

    /**
     * Sets the {@link DisplayWindow#loadingOverlay} field.
     *
     * @param loadingOverlay the new value
     */
    public void setLoadingOverlay(Method loadingOverlay) {
        setFieldValue("loadingOverlay", loadingOverlay);
    }

    /**
     * @see DisplayWindow#initRender(String, String)
     */
    public void initRender(@Nullable String mcVersion, @NonNull String forgeVersion) {
        try {
            getMethod("initRender", String.class, String.class)
                    .invoke(target, mcVersion, forgeVersion);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Sets the {@link DisplayWindow#renderScheduler} field.
     *
     * @param renderScheduler the new value
     */
    public void setRenderScheduler(ScheduledExecutorService renderScheduler) {
        setFieldValue("renderScheduler", renderScheduler);
    }

    /**
     * Sets the {@link DisplayWindow#initializationFuture} field.
     *
     * @param initializationFuture the new value
     */
    public void setInitializationFuture(ScheduledFuture<?> initializationFuture) {
        setFieldValue("initializationFuture", initializationFuture);
    }

    /**
     * @return the value of the {@link DisplayWindow#elements} field
     */
    @SuppressWarnings("unchecked")
    public ArrayList<RenderElement> getElements() {
        return (ArrayList<RenderElement>) getFieldValue(clazz, target, "elements");
    }

    public void crashElegantly(String errorDetails) {
        try {
            getMethod("crashElegantly", String.class).invoke(target, errorDetails);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    public long getGlWindow() {
        return (long) getFieldValue(clazz, target, "window");
    }

    public void setColourScheme(ColourScheme colourScheme) {
        setFieldValue("colourScheme", colourScheme);
    }

    public boolean isMaximized() {
        return (boolean) getFieldValue(clazz, target, "maximized");
    }

    public void setFBSize(int width, int height) {
        setFieldValue("fbWidth", width);
        setFieldValue("fbHeight", height);
    }

    public RenderElement.DisplayContext getContext() {
        return (RenderElement.DisplayContext) getFieldValue(clazz, target, "context");
    }

    public void setContext(RenderElement.DisplayContext context) {
        setFieldValue("context", context);
    }

    public void setFrameBuffer(Object framebuffer) {
        setFieldValue("framebuffer", framebuffer);
    }

    public Object getFramebuffer() {
        return getFieldValue(clazz, target, "framebuffer");
    }

    public int getFrameCount() {
        return (int) getFieldValue(clazz, target, "framecount");
    }

    public SimpleFont getFont() {
        return (SimpleFont) getFieldValue(clazz, target, "font");
    }
}
