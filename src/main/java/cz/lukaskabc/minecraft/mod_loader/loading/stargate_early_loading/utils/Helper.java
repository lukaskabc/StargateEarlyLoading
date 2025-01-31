package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import java.util.Random;

public class Helper {
    public static final Random random = new Random();

    public static <T> T randomElement(T[] array) {
        return array[random.nextInt(array.length)];
    }

    private Helper() {
        throw new AssertionError();
    }
}
