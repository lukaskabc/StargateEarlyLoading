package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.MilkyWayStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.PegasusStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.UniverseStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant.StargateType;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {
    private static final String STARGATE_VARIANT_CONFIG_DIRECTORY = "stargate-early-loading";
    private static final String DEFAULT_CONFIG_FILE = "/default_config.json";
    private static final String CONFIG_FILE_NAME = "stargate-early-loading.json";
    private static final String RESOURCE_STARGATE_VARIANT_DIRECTORY = "/assets/stargate";
    private static final Logger LOG = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get().resolve(STARGATE_VARIANT_CONFIG_DIRECTORY);
    }

    private static Path getConfigFilePath() {
        return FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_NAME).toAbsolutePath();
    }

    /**
     * If the configuration file does not exist, it's created by copying the default config.
     */
    public static void copyDefaultConfig() {
        if (!Files.exists(getConfigFilePath())) {
            LOG.atDebug().log("Creating default config file {}", getConfigFilePath());
            try (final InputStream defaultConfig = ConfigLoader.class.getResourceAsStream(DEFAULT_CONFIG_FILE)) {
                if (defaultConfig == null) {
                    throw new InitializationException("Could not find default config file: " + DEFAULT_CONFIG_FILE);
                }
                Files.copy(defaultConfig, getConfigFilePath());
                LOG.atDebug().log("Config file successfully created");
            } catch (IOException e) {
                LOG.atError().log("Failed to create default config file: {}", e.getMessage());
                throw new InitializationException(e);
            }
        }
    }

    /**
     * Loads the config file
     *
     * @return loaded config
     */
    public static Config loadConfiguration() {
        LOG.atDebug().log("Loading configuration from {}", getConfigFilePath());
        try {
            return GSON.fromJson(new FileReader(getConfigFilePath().toFile()), Config.class);
        } catch (JsonParseException | FileNotFoundException e) {
            LOG.atError().log("Failed to load configuration from {}\nError:{}", getConfigFilePath(), e.getMessage());
            throw new InitializationException(e);
        }
    }

    public static StargateType[] findAvailableTypes(Config configuration) {
        List<StargateType> types = new ArrayList<>(StargateType.values().length);
        final Config.AllowedVariants allowedVariants = configuration.getVariants();
        if (allowedVariants.getMilkyWay().length > 0) {
            types.add(StargateType.MILKY_WAY);
        }
        if (allowedVariants.getPegasus().length > 0) {
            types.add(StargateType.PEGASUS);
        }
        if (allowedVariants.getUniverse().length > 0) {
            types.add(StargateType.UNIVERSE);
        }
        if (types.isEmpty()) {
            throw new InitializationException("No available stargate variants");
        }
        return types.toArray(StargateType[]::new);
    }

    /**
     * Picks random stargate variant from configuration
     */
    public static GenericStargate loadStargate(Config configuration) {
        final StargateType type = Helper.randomElement(findAvailableTypes(configuration));
        final String[] variants = switch (type) {
            case MILKY_WAY -> configuration.getVariants().getMilkyWay();
            case PEGASUS -> configuration.getVariants().getPegasus();
            case UNIVERSE -> configuration.getVariants().getUniverse();
        };
        final String variant = Helper.randomElement(variants);
        final StargateVariant stargateVariant = loadStargateVariant(type, variant);
        final Config.Symbols symbols = configuration.getSymbols().get(stargateVariant.getSymbols().getPermanentSymbols().orElseThrow(() -> new InitializationException("Stargate variant is missing permanent symbols")));

        LOG.atDebug().log("Randomly picked Stargate type: {}, variant: {}", type, variant);
        return switch (type) {
            case MILKY_WAY -> new MilkyWayStargate(stargateVariant, symbols);
            case PEGASUS -> new PegasusStargate(stargateVariant, symbols);
            case UNIVERSE -> new UniverseStargate(stargateVariant, symbols);
        };
    }

    /**
     * Tries to resolve file inside {@link #getConfigDirectory()} otherwise resolves file from resources on classpath
     */
    public static Reader resolveFile(Path path) throws FileNotFoundException {
        final Path configDir = getConfigDirectory().resolve(path);
        if (Files.exists(configDir)) {
            return new FileReader(configDir.toFile());
        }

        final InputStream result = ConfigLoader.class.getResourceAsStream("/assets/" + path.toString().replace('\\', '/'));
        if (result == null) {
            throw new FileNotFoundException(configDir.toString());
        }
        return new InputStreamReader(result);
    }

    public static StargateVariant loadStargateVariant(StargateType type, String variant) {
        final Path path = Path.of("stargate", type.name().toLowerCase(), variant);
        final Path configPath = path.resolve(variant + ".json");

        try {
            return GSON.fromJson(resolveFile(configPath), StargateVariant.class);
        } catch (JsonParseException | FileNotFoundException e) {
            LOG.atError().log("Failed to load stargate variant:{}", e.getMessage());
            throw new InitializationException(e);
        }
    }


    private ConfigLoader() {
        throw new AssertionError();
    }
}
