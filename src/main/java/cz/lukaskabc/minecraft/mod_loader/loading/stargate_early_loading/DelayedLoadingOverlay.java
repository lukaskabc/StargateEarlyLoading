package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.ReflectionException;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DelayedLoadingOverlay implements InvocationHandler {
    @Nullable
    private final StargateEarlyLoadingWindow displayWindow;
    private final Field fadeOutStart;
    private boolean delayed = false;

    public DelayedLoadingOverlay(DisplayWindow displayWindow) {
        fadeOutStart = null;//new ReflectionAccessor(this).getField("fadeOutStart");
        if (displayWindow instanceof StargateEarlyLoadingWindow window) {
            this.displayWindow = window;
        } else {
            this.displayWindow = null;
        }
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (displayWindow == null) return; // how the hell this happened?

        if (displayWindow.loadingAnimationFinished()) {
            if (delayed) {
                setFadeOutStart(Util.getMillis());
                delayed = false;
            }
        } else {
            if (getFadeOutStart() > -1L) {
                delayed = true;
                setFadeOutStart(-1L);
            }
        }


    }

    public long getFadeOutStart() {
        try {
            return fadeOutStart.getLong(this);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public void setFadeOutStart(long value) {
        try {
            fadeOutStart.setLong(this, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
