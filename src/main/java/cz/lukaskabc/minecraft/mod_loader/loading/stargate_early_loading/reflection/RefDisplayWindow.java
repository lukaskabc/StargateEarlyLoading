package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.fml.earlydisplay.RenderElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class RefDisplayWindow extends ReflectionAccessor {
    public RefDisplayWindow(DisplayWindow displayWindow) {
        super(displayWindow, DisplayWindow.class);
    }

    public void setLoadingOverlay(Method loadingOverlay) {
        setFieldValue("loadingOverlay", loadingOverlay);
    }

    public Object initRender(String mcVersion, String forgeVersion) {
        try {
            return getMethod("initRender", String.class, String.class)
                    .invoke(target, mcVersion, forgeVersion);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setRenderScheduler(ScheduledExecutorService renderScheduler) {
        setFieldValue("renderScheduler", renderScheduler);
    }
    public void setInitializationFuture(ScheduledFuture<?> initializationFuture) {
        setFieldValue("initializationFuture", initializationFuture);
    }

    public ArrayList<RenderElement> getElements() {
        return (ArrayList<RenderElement>) getFieldValue("elements");
    }

    public void crashElegantly(String errorDetails) {
        try {
            getMethod("crashElegantly", String.class).invoke(target, errorDetails);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
