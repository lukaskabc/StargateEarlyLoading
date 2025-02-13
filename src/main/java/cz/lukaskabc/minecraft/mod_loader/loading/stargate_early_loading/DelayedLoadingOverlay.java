package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefNeoLoadingOverlay;
import net.minecraft.Util;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.NEOFORGE_LOADING_OVERLAY_CLASS;

public class DelayedLoadingOverlay implements InvocationHandler {
    private final RefNeoLoadingOverlay loading;
    @Nullable
    private final StargateEarlyLoadingWindow displayWindow;
    private boolean delayed = false;

    private DelayedLoadingOverlay(DisplayWindow displayWindow, Object neoforgeLoadingOverlay) {
        loading = new RefNeoLoadingOverlay(neoforgeLoadingOverlay);
        if (displayWindow instanceof StargateEarlyLoadingWindow window) {
            this.displayWindow = window;
        } else {
            this.displayWindow = null;
        }
    }

    public static Object constructProxiedOverlay(final DisplayWindow displayWindow, final Object neoforgeLoadingOverlay) {
        // what else should I do... bloody hell
        Class<?> overlayClass = NEOFORGE_LOADING_OVERLAY_CLASS.getSuperclass().getSuperclass();

        return Proxy.newProxyInstance(NEOFORGE_LOADING_OVERLAY_CLASS.getClassLoader(),
                new Class[]{overlayClass},
                new DelayedLoadingOverlay(displayWindow, neoforgeLoadingOverlay));
    }

    public void render(Object graphics, int mouseX, int mouseY, float partialTick) {
        loading.render(graphics, mouseX, mouseY, partialTick);
        if (displayWindow == null) return; // how the hell this happened?

        if (displayWindow.loadingAnimationFinished()) {
            if (delayed) {
                loading.setFadeOutStart(Util.getMillis());
                delayed = false;
            }
        } else {
            if (loading.getFadeOutStart() > -1L) {
                delayed = true;
                loading.setFadeOutStart(-1L);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("render")) {
            render(args[0], (int) args[1], (int) args[2], (float) args[3]);
            return null; // void method
        }
        if (!method.canAccess(proxy)) {
            method.setAccessible(true);
        }
        return method.invoke(proxy, args);
    }
}
