package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.ReflectionAccessor;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.ReflectionException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DelayedLoadingOverlay extends NeoForgeLoadingOverlay {
    @Nullable
    private final StargateEarlyLoadingWindow displayWindow;
    private final Field fadeOutStart;
    private boolean delayed = false;


    public DelayedLoadingOverlay(Minecraft mc, ReloadInstance reloader, Consumer<Optional<Throwable>> errorConsumer, DisplayWindow displayWindow) {
        super(mc, reloader, errorConsumer, displayWindow);
        fadeOutStart = new ReflectionAccessor(this).getField("fadeOutStart");
        if (displayWindow instanceof StargateEarlyLoadingWindow window) {
            this.displayWindow = window;
        } else {
            this.displayWindow = null;
        }
    }

    public static Supplier<LoadingOverlay> newInstance(Supplier<Minecraft> mc, Supplier<ReloadInstance> ri, Consumer<Optional<Throwable>> handler, DisplayWindow window) {
        return () -> new DelayedLoadingOverlay(mc.get(), ri.get(), handler, window);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
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
}
