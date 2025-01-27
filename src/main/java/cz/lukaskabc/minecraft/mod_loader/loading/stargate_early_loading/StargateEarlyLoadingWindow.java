package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.MilkyWayStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefDisplayWindow;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.ReflectionException;
import net.neoforged.fml.earlydisplay.ColourScheme;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.neoforgespi.earlywindow.ImmediateWindowProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class StargateEarlyLoadingWindow extends DisplayWindow implements ImmediateWindowProvider {
    public static final String ASSETS_DIRECTORY = "assets";
    private static final Logger LOG = LogManager.getLogger();
    public static int globalAlpha = 255;
    private final RefDisplayWindow accessor;

    public StargateEarlyLoadingWindow() {
        this.accessor = new RefDisplayWindow(this);
    }

    private static List<RenderElement> constructElements() {
        final List<RenderElement> elements = new ArrayList<>();
//        elements.add(DarkSkyBackground.create());
//        elements.addAll(PegasusRefreshedLoop.create());
        elements.addAll(new MilkyWayStargate().create());
        return elements;
    }

    @Override
    public String name() {
        return "StargateEarlyLoading";
    }

    @Override
    public Runnable initialize(String[] arguments) {
        final Runnable result = super.initialize(arguments);
        // force black colour scheme
        accessor.setColourScheme(ColourScheme.BLACK);
        return result;
    }

    @Override
    public Runnable start(@Nullable String mcVersion, String forgeVersion) {
        final ScheduledExecutorService renderScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            final Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setDaemon(true);
            return thread;
        });
        accessor.setRenderScheduler(renderScheduler);
        initWindow(mcVersion);
        final var initializationFuture = renderScheduler.schedule(() -> {
            accessor.initRender(mcVersion, forgeVersion);
            afterInitRender();
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
        glfwMakeContextCurrent(accessor.getGlWindow());
        recreateContext();
        final List<RenderElement> elements = accessor.getElements();
        final RenderElement loadingBar = elements.getLast();
        elements.clear();
        elements.addAll(constructElements());
        elements.add(loadingBar);
        glfwMakeContextCurrent(0);
    }

    private void recreateContext() {
        final RenderElement.DisplayContext oldContext = accessor.getContext();
        final Object oldFrameBuffer = accessor.getFramebuffer();
        final int[] width = new int[1];
        final int[] height = new int[1];
        glfwGetFramebufferSize(accessor.getGlWindow(), width, height);
        accessor.setFBSize(width[0], height[0]);
        final RenderElement.DisplayContext context = new RenderElement.DisplayContext(width[0], height[0], oldContext.scale(), oldContext.elementShader(), oldContext.colourScheme(), oldContext.performance());
        accessor.setContext(context);
        try {
            final Constructor<?> constructor = Class.forName("net.neoforged.fml.earlydisplay.EarlyFramebuffer").getDeclaredConstructor(RenderElement.DisplayContext.class);
            constructor.setAccessible(true);
            accessor.setFrameBuffer(constructor.newInstance(context));
            final Method close = Class.forName("net.neoforged.fml.earlydisplay.EarlyFramebuffer").getDeclaredMethod("close");
            close.setAccessible(true);
            close.invoke(oldFrameBuffer);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    @Override
    public void updateModuleReads(final ModuleLayer layer) {
        var fm = layer.findModule("neoforge").orElseThrow();
        getClass().getModule().addReads(fm);
        var clz = Class.forName(fm, "net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay");
        var methods = Arrays.stream(clz.getMethods()).filter(m -> Modifier.isStatic(m.getModifiers())).collect(Collectors.toMap(Method::getName, Function.identity()));
        accessor.setLoadingOverlay(methods.get("newInstance"));
    }

    @Override
    public void render(int alpha) {
        globalAlpha = alpha;
        super.render(alpha);
    }

    @Override
    public void close() {
        super.close();
        throw new RuntimeException("Loading completed");
    }
}
