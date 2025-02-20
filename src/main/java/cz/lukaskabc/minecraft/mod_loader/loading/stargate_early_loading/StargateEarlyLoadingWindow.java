package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing.DialingStrategy;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.Background;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.MojangLogo;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.StartupProgressBar;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefDialingStrategy;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefDisplayWindow;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefEarlyFrameBuffer;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ConfigLoader;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.Helper;
import net.neoforged.fml.earlydisplay.ColourScheme;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleFont;
import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.neoforgespi.earlywindow.ImmediateWindowProvider;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.jspecify.annotations.Nullable;

import javax.swing.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.ProgressBar.BAR_HEIGHT;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

/**
 * Main class extending default DisplayWindow.
 * <p>
 * All the reflection ugly stuff is here only for a single reason, to not copy the whole DisplayWindow
 * and use the existing implementation instead.
 */
public class StargateEarlyLoadingWindow extends DisplayWindow implements ImmediateWindowProvider {
    public static final String WINDOW_PROVIDER = "StargateEarlyLoading";
    public static final int MEMORY_BAR_OFFSET = 32;
    public static final int MEMORY_BAR_HEIGHT = BAR_HEIGHT + MEMORY_BAR_OFFSET;
    private static final Logger LOG = LogManager.getLogger();
    private static int globalAlpha = 255;
    private static Vector2f center = new Vector2f(1, 1);
    private final RefDisplayWindow accessor;
    private final Config configuration;
    private final GenericStargate stargate;
    private final DialingStrategy dialingStrategy;
    private final StopWatch stopWatch = new StopWatch();

    public StargateEarlyLoadingWindow() {
        this.accessor = new RefDisplayWindow(this);
        checkFMLConfig();
        stopWatch.start();
        ConfigLoader.copyDefaultConfig();
        configuration = ConfigLoader.loadConfiguration();
        stargate = ConfigLoader.loadStargate(configuration);
        final StargateVariant variant = stargate.getVariant();
        dialingStrategy = RefDialingStrategy.instance(Optional.ofNullable(variant.getDialingStrategy())
                        .orElseGet(() -> configuration.getDefaultDialingStrategies().get(variant.getType())),
                stargate,
                configuration.getChevronOrder()
        );
    }

    private static void checkFMLConfig() {
        final String windowProvider = FMLConfig.getConfigValue(FMLConfig.ConfigValue.EARLY_WINDOW_PROVIDER);
        if (!WINDOW_PROVIDER.equals(windowProvider)) {
            JOptionPane.showMessageDialog(null, """
                    You have installed the Stargate Early Loading mod,
                    but the early window provider is not set to StargateEarlyLoading in the fml.toml config!
                    Please update the config and restart the game.
                    See mod description for instructions.
                    """);
        }
    }

    private void constructElements(@Nullable String mcVersion, String forgeVersion, final List<RenderElement> elements) {
        final SimpleFont font = accessor.getFont();
        elements.add(new Background(Helper.randomElement(configuration.getBackgrounds())).get());
        elements.add(stargate.createRenderElement());
        elements.add(new StartupProgressBar(font, dialingStrategy).get());

        // from forge early loading:
        // top middle memory info
        elements.add(RenderElement.performanceBar(font));
        // bottom left log messages
        elements.add(RenderElement.logMessageOverlay(font));
        // bottom right game version
        elements.add(RenderElement.forgeVersionOverlay(font, mcVersion + "-" + forgeVersion.split("-")[0]));
    }

    @Override
    public String name() {
        return WINDOW_PROVIDER;
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
            afterInitRender(mcVersion, forgeVersion);
        }, 1, TimeUnit.MILLISECONDS);
        accessor.setInitializationFuture(initializationFuture);
        return this::periodicTick;
    }

    /**
     * @implNote The method is called after init render is called inside the same schedule.
     * Since the scheduler is single threaded there is no possibility for race condition
     * with other scheduled tasks.
     */
    private void afterInitRender(@Nullable String mcVersion, String forgeVersion) {
        glfwMakeContextCurrent(accessor.getGlWindow());
        recreateContext();
        final List<RenderElement> elements = accessor.getElements();
        elements.clear();
        constructElements(mcVersion, forgeVersion, elements);
        glfwMakeContextCurrent(0);
    }

    public static int getGlobalAlpha() {
        return globalAlpha;
    }

    @Override
    public void updateModuleReads(final ModuleLayer layer) {
        var fm = layer.findModule("neoforge").orElseThrow();
        getClass().getModule().addReads(fm);
        var clz = Class.forName(fm, "net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay");
        var methods = Arrays.stream(clz.getMethods()).filter(m -> Modifier.isStatic(m.getModifiers())).collect(Collectors.toMap(Method::getName, Function.identity()));
        accessor.setLoadingOverlay(methods.get("newInstance"));
    }

    public static void setGlobalAlpha(int globalAlpha) {
        StargateEarlyLoadingWindow.globalAlpha = globalAlpha;
    }

    @SuppressWarnings("unused")
    public boolean loadingAnimationFinished() {
        return !stargate.isChevronRaised(0) && stargate.isChevronEngaged(0);
    }

    @Override
    public void close() {
        super.close();
        stopWatch.stop();
        LOG.info("Closing loading after: {}", stopWatch);
        throw new RuntimeException("Loading completed");
    }

    @Override
    public void addMojangTexture(int textureId) {
        accessor.getElements().addLast(new MojangLogo(textureId, accessor.getFrameCount()).get());
    }

    public static Vector2f getCenter() {
        return center;
    }

    public static void setCenter(final int x, final int y) {
        StargateEarlyLoadingWindow.center.set(x, y + (float) BAR_HEIGHT);
    }

    @Override
    public void render(int alpha) {
        globalAlpha = alpha;
        super.render(alpha);
    }

    private void recreateContext() {
        final RenderElement.DisplayContext oldContext = accessor.getContext();
        final Object oldFrameBuffer = accessor.getFramebuffer();
        final int[] width = new int[1];
        final int[] height = new int[1];
        glfwGetFramebufferSize(accessor.getGlWindow(), width, height);
        accessor.setFBSize(width[0], height[0]);
        setCenter(width[0] / 2, height[0] / 2);
        final RenderElement.DisplayContext context = new RenderElement.DisplayContext(width[0], height[0], oldContext.scale(), oldContext.elementShader(), oldContext.colourScheme(), oldContext.performance());
        accessor.setContext(context);
        accessor.setFrameBuffer(RefEarlyFrameBuffer.constructor(context));
        RefEarlyFrameBuffer.close(oldFrameBuffer);
    }
}
