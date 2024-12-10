package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefDisplayWindow;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.neoforgespi.earlywindow.ImmediateWindowProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StargateEarlyLoadingWindow extends DisplayWindow implements ImmediateWindowProvider {
    private static final Logger LOG = LogManager.getLogger();
    private final RefDisplayWindow accessor;

    public StargateEarlyLoadingWindow() {
        this.accessor = new RefDisplayWindow(this);
    }

    @Override
    public String name() {
        return "StargateEarlyLoading";
    }

    @Override
    public Runnable start(@Nullable String mcVersion, String forgeVersion) {
        final var renderScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            final var thread = Executors.defaultThreadFactory().newThread(r);
            thread.setDaemon(true);
            return thread;
        });
        accessor.setRenderScheduler(renderScheduler);
        initWindow(mcVersion);
        final var initializationFuture = renderScheduler.schedule(() -> {
            final Object result = accessor.initRender(mcVersion, forgeVersion);
            afterInitRender();
            return result;
        }, 1, TimeUnit.MILLISECONDS);
        accessor.setInitializationFuture(initializationFuture);
        return this::periodicTick;
    }

    /**
     * @implNote The method is called after init render is called inside the same schedule.
     * Since the scheduler is single threaded there is no possibility for race condition
     * with other scheduled tasks.
     */
    private void afterInitRender() {
        final var elements = accessor.getElements();
        final var first = elements.getFirst();
        elements.clear();
        elements.add(first);
    }

    @Override
    public void updateModuleReads(final ModuleLayer layer) {
        var fm = layer.findModule("neoforge").orElseThrow();
        getClass().getModule().addReads(fm);
        var clz = Class.forName(fm, "net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay");
        var methods = Arrays.stream(clz.getMethods()).filter(m -> Modifier.isStatic(m.getModifiers())).collect(Collectors.toMap(Method::getName, Function.identity()));
        accessor.setLoadingOverlay(methods.get("newInstance"));
    }
}
