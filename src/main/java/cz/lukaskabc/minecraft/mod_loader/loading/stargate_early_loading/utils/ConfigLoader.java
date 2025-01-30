package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import net.neoforged.fml.loading.FMLPaths;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader {
    private static final String DEFAULT_CONFIG_FILE = "/default_config.json";
    private static final String CONFIG_FILE_NAME = "stargate-early-loading.json";

    private static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_NAME).toAbsolutePath();
    }

    /**
     * If the configuration file does not exist, it's created by copying the default config.
     */
    public static void copyDefaultConfig() {
        if (!Files.exists(getConfigPath())) {
            try (final InputStream defaultConfig = ConfigLoader.class.getResourceAsStream(DEFAULT_CONFIG_FILE)) {
                if (defaultConfig == null) {
                    throw new InitializationException("Could not find default config file: " + DEFAULT_CONFIG_FILE);
                }
                Files.copy(defaultConfig, getConfigPath());
            } catch (IOException e) {
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
        final Gson gson = new GsonBuilder()
                .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        try {
            return gson.fromJson(new FileReader(getConfigPath().toFile()), Config.class);
        } catch (JsonParseException | FileNotFoundException e) {
            throw new InitializationException(e);
        }
    }

    private ConfigLoader() {
        throw new AssertionError();
    }
}
