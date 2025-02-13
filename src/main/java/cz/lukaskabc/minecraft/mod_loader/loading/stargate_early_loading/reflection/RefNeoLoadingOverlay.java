package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Method;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.NEOFORGE_LOADING_OVERLAY_CLASS;

public class RefNeoLoadingOverlay extends ReflectionAccessor {
    private static final MethodHandles.Lookup lookup = privateLookup(NEOFORGE_LOADING_OVERLAY_CLASS);
    private static final VarHandle fadeOutStart = findField(lookup, "fadeOutStart", long.class);
    private static final MethodHandle render = findRenderMethod();

    public RefNeoLoadingOverlay(Object target) {
        super(target, NEOFORGE_LOADING_OVERLAY_CLASS);
    }

    private static MethodHandle findRenderMethod() {
        try {
            for (final Method declaredMethod : NEOFORGE_LOADING_OVERLAY_CLASS.getDeclaredMethods()) {
                declaredMethod.setAccessible(true);
                if (declaredMethod.getName().equals("render")) {
                    return lookup.unreflect(declaredMethod);
                }
            }
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
        throw new InitializationException(NEOFORGE_LOADING_OVERLAY_CLASS.getName() + "#render method not found");
    }

    public long getFadeOutStart() {
        return (long) fadeOutStart.get(target);
    }

    public void setFadeOutStart(long value) {
        fadeOutStart.set(target, value);
    }

    public void render(Object graphics, int mouseX, int mouseY, float partialTick) {
        try {
            render.invoke(target, graphics, mouseX, mouseY, partialTick);
        } catch (Throwable throwable) {
            throw new ReflectionException(throwable);
        }
    }
}
