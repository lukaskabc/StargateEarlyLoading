package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import static org.lwjgl.opengl.GL11C.glGenTextures;

/**
 * Constant identifiers for textures.
 */
public class TextureIdentifierConstants {
    public static final int BACKGROUND = glGenTextures();
    public static final int CENTERED_LOGO = glGenTextures();
    public static final int STARGATE = glGenTextures();
    public static final int STARGATE_ENGAGED = glGenTextures();
    public static final int STARGATE_SYMBOLS = glGenTextures();
    public static final int STARGATE_POO_SYMBOL = glGenTextures();

    private TextureIdentifierConstants() {
        throw new AssertionError();
    }
}
