package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing.DialingStrategy;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.Background;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.CenteredLogo;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.MojangLogo;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.StartupProgressBar;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefDialingStrategy;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefDisplayWindow;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefEarlyFrameBuffer;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ConfigLoader;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.Helper;
import net.minecraftforge.fml.earlydisplay.ColourScheme;
import net.minecraftforge.fml.earlydisplay.DisplayWindow;
import net.minecraftforge.fml.earlydisplay.RenderElement;
import net.minecraftforge.fml.earlydisplay.SimpleFont;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.ImmediateWindowProvider;
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
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.ProgressBar.BAR_HEIGHT;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glClearColor;

/**
 * Main class extending default DisplayWindow.
 * <p>
 * All the reflection stuff is here only for a single reason, to not copy the whole DisplayWindow
 * and use the existing implementation instead.
 */
public class StargateEarlyLoadingWindow extends DisplayWindow implements ImmediateWindowProvider {
    public static final String EXPECTED_WINDOW_PROVIDER = "fmlearlywindow";
    public static final int MEMORY_BAR_OFFSET = 32;
    public static final int MEMORY_BAR_HEIGHT = BAR_HEIGHT + MEMORY_BAR_OFFSET;
    private static final Logger LOG = LogManager.getLogger();
    private static Vector2f center = new Vector2f(1, 1);
    private final RefDisplayWindow accessor;
    private final Config configuration;
    private final GenericStargate stargate;
    private final DialingStrategy dialingStrategy;
    @Nullable
    private CenteredLogo centeredLogo = null;

    /**
     * Loads configuration, picks random stargate and loads its variant configuration.
     * The dialing strategy from the variant is used if present,
     * otherwise the default strategy form main config is resolved.
     */
    public StargateEarlyLoadingWindow() {
        this.accessor = new RefDisplayWindow(this);
        checkFMLConfig();
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

    /**
     * Checks whether the FML configuration has the {@link #EXPECTED_WINDOW_PROVIDER} set as the {@link FMLConfig.ConfigValue#EARLY_WINDOW_PROVIDER EARLY_WINDOW_PROVIDER}.
     * <p>
     * If the value does not match,
     * an error message dialog is displayed to instruct the user to update the config.
     */
    private static void checkFMLConfig() {
        final String windowProvider = FMLConfig.getConfigValue(FMLConfig.ConfigValue.EARLY_WINDOW_PROVIDER);
        if (!EXPECTED_WINDOW_PROVIDER.equals(windowProvider)) {
            // Create a parent frame that will appear in the taskbar
            final JFrame frame = new JFrame("Missing Forge configuration");
            frame.setAlwaysOnTop(true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);

            final int answer = JOptionPane.showConfirmDialog(frame, """
                            You have installed the Stargate Early Loading mod,
                            but the early window provider is not set to WINDOW_PROVIDER in the fml.toml config!
                            Please update the config and restart the game.
                            See mod description for instructions.
                            https://github.com/lukaskabc/StargateEarlyLoading
                            
                            Do you wish to update the config?
                            Answering yes will update the config and exit the game.
                            """.replace("WINDOW_PROVIDER", EXPECTED_WINDOW_PROVIDER),
                    "Missing Forge configuration",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            frame.dispose();

            if (answer == JOptionPane.YES_OPTION) {
                FMLConfig.updateConfig(FMLConfig.ConfigValue.EARLY_WINDOW_PROVIDER, EXPECTED_WINDOW_PROVIDER);
                System.exit(0);
            }
        }
    }


    /**
     * Constructs the elements to be rendered in the loading window.
     *
     * @param elements The list where render elements will be added.
     */
    private void constructElements(final List<RenderElement> elements) {
        final SimpleFont font = accessor.getFont();
        //        final RenderElement anvil = elements.get(0); // I mean... do we really need it? we have stargates here!
        final RenderElement logMessageOverlay = elements.get(1);
        final RenderElement forgeVersionOverlay = elements.get(2);
        elements.clear();

        elements.add(new Background(Helper.randomElement(configuration.getBackgrounds())).get());
        if (configuration.getLogoTexture() != null) {
            centeredLogo = new CenteredLogo(configuration.getLogoTexture(), configuration.getLogoTextureSize());
            elements.add(centeredLogo.get());
        }
        elements.add(stargate.createRenderElement());
        elements.add(new StartupProgressBar(font, dialingStrategy).get());
// TODO fix element positions and scaling - test with fml scale
        // from forge early loading:
        // top middle memory info

        elements.add(RenderElement.performanceBar(font));
        // bottom left log messages
        elements.add(logMessageOverlay);
        // bottom right game version
        elements.add(forgeVersionOverlay);
    }

    /**
     * @return The window provider name.
     */
    @Override
    public String name() {
        return EXPECTED_WINDOW_PROVIDER;
    }

    /**
     * In Forge context should never be called.
     *
     * @throws AssertionError always
     * @see #reinitializeAfterStateCopy()
     */
    @Override
    public Runnable initialize(String[] arguments) {
        throw new AssertionError("The Stargate Early Loading Window should not be initialized in Forge context!");
    }

    /**
     * In Forge context should never be called.
     *
     * @throws AssertionError always
     * @see #reinitializeAfterStateCopy()
     */
    @Override
    public Runnable start(String mcVersion, String forgeVersion) {
        throw new AssertionError("The Stargate Early Loading Window should not be initialized in Forge context!");
    }

    public Runnable reinitializeAfterStateCopy() {
        // from initialize method
        accessor.setColourScheme(ColourScheme.BLACK);
        // from start method
        final var future = accessor.getRenderScheduler().schedule(() -> {
            try {
                accessor.getRenderLock().acquire(); // block rendering
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            // from initWindow
            // rebind callbacks to the new instance
            glfwSetFramebufferSizeCallback(accessor.getGlWindow(), accessor::fbResize);
            glfwSetWindowPosCallback(accessor.getGlWindow(), accessor::winMove);
            glfwSetWindowSizeCallback(accessor.getGlWindow(), accessor::winResize);

            // cancel the old window tick
            if (accessor.getWindowTick() != null) {
                while (!accessor.getWindowTick().isDone()) {
                    accessor.getWindowTick().cancel(false);
                }
            } else {
                // TODO: this is potentially a problem, if the window tick was null,
                //  then the window was already handed over to the game
                LOG.error("Early window was already handed over to the game - that was fast! Aborting Stargate Early Loading initialization.");
                return;
            }
            // from initRender
            accessor.setWindowTick(accessor.getRenderScheduler().scheduleAtFixedRate(accessor::renderThreadFunc, 50, 50, TimeUnit.MILLISECONDS));
            accessor.getRenderScheduler().scheduleAtFixedRate(() -> accessor.getAnimationTimerTrigger().set(true), 1, 50, TimeUnit.MILLISECONDS);
            afterInitRender();
            accessor.getRenderLock().release();
        }, 1, TimeUnit.MILLISECONDS);
        accessor.setInitializationFuture(future);
        return this::periodicTick;
    }

    /**
     * Performs post-render initialization.
     * <p>
     * Establishes the OpenGL context, recreates the render context, and constructs render elements.
     *
     * @implNote The method is called after init render is called inside the same schedule.
     * Since the scheduler is single threaded there is no possibility for race condition
     * with other scheduled tasks.
     */
    public void afterInitRender() {
        glfwMakeContextCurrent(accessor.getGlWindow());
        // Set the clear color based on the colour scheme
        final ColourScheme colourScheme = accessor.getColourScheme();
        glClearColor(colourScheme.background().redf(), colourScheme.background().greenf(), colourScheme.background().bluef(), 1f);
        recreateContext();
        final List<RenderElement> elements = accessor.getElements();
        constructElements(elements);
        glfwMakeContextCurrent(0);
    }

    /**
     * Reimplementation of the superclass method using the module of this class.
     * <p>
     * Updates the module reads, adding a read edge to the 'neoforge' module.
     * <p>
     * Uses reflection to obtain the loading overlay instance method.
     *
     * @param layer The ModuleLayer from which modules are read.
     * @see DisplayWindow#updateModuleReads(ModuleLayer)
     */
    @Override
    public void updateModuleReads(final ModuleLayer layer) {
        var fm = layer.findModule("forge").orElseThrow();
        getClass().getModule().addReads(fm);
        var clz = FMLLoader.getGameLayer().findModule("forge").map(l -> Class.forName(l, "net.minecraftforge.client.loading.ForgeLoadingOverlay")).orElseThrow();
        var methods = Arrays.stream(clz.getMethods()).filter(m -> Modifier.isStatic(m.getModifiers())).collect(Collectors.toMap(Method::getName, Function.identity()));
        accessor.setLoadingOverlay(methods.get("newInstance"));
    }

    /**
     * Checks if the loading animation is finished.
     *
     * @return {@code true} if the animation is finished, {@code false} otherwise.
     * @implNote Unused warning is suppressed as this is an api for DelayedLoadingOverlay
     */
    @SuppressWarnings("unused")
    public boolean loadingAnimationFinished() {
        return !stargate.isChevronRaised(0) && stargate.isChevronEngaged(0);
    }

    /**
     * Closes the window.
     * <p>
     * Invokes the superclass close method
     * and optionally throws a RuntimeException if the configuration specifies so.
     *
     * @see DisplayWindow#close()
     */
    @Override
    public void close() {
        super.close();
        if (configuration.doExitAfterLoad()) {
            System.exit(13);
        }
    }

    /**
     * Adds the Mojang logo to the loading screen.
     *
     * @param textureId The texture id of the Mojang logo.
     */
    @Override
    public void addMojangTexture(int textureId) {
        int delayMojang = 0;
        if (centeredLogo != null) {
            centeredLogo.setFadeOutStart(accessor.getFrameCount());
            delayMojang = 5;
        }
        accessor.getElements().add(new MojangLogo(textureId, accessor.getFrameCount() + delayMojang).get());
    }

    /**
     * Returns the center position of the frame buffer.
     *
     * @return A {@link Vector2f} representing the center position.
     */
    public static Vector2f getCenter() {
        return center;
    }

    /**
     * Sets the center position of the frame buffer based on provided coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public static void setCenter(final int x, final int y) {
        StargateEarlyLoadingWindow.center.set(x, y + (float) BAR_HEIGHT);
    }

    /**
     * Recreates the rendering context overriding the frame buffer resolution set by the original DisplayWindow implementation.
     * <p>
     * Updates the frame buffer size according to OpenGL window frame buffer size,
     * creates a new display context, sets up a new frame buffer,
     * and updates the center position based on the scaled context dimensions.
     * <p>
     * The old frame buffer is closed to release the resources.
     */
    private void recreateContext() {
        final RenderElement.DisplayContext oldContext = accessor.getContext();
        final Object oldFrameBuffer = accessor.getFramebuffer();
        final int[] width = new int[1];
        final int[] height = new int[1];
        glfwGetFramebufferSize(accessor.getGlWindow(), width, height);
        accessor.setFBSize(width[0], height[0]);

        LOG.debug("The available size of the framebuffer in the window is {}x{}", width[0], height[0]);

        final RenderElement.DisplayContext context = new RenderElement.DisplayContext(
                width[0],
                height[0],
                oldContext.scale(),
                oldContext.elementShader(),
                accessor.getColourScheme(),
                oldContext.performance()
        );
        accessor.setContext(context);
        accessor.setFrameBuffer(RefEarlyFrameBuffer.constructor(context));
        setCenter(context.scaledWidth() / 2, context.scaledHeight() / 2);
        RefEarlyFrameBuffer.close(oldFrameBuffer);
    }
}
