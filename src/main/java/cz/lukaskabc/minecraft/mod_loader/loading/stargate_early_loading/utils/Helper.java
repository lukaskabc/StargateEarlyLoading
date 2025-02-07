package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import org.jspecify.annotations.Nullable;

import java.util.Random;

public class Helper {
    public static final Random random = new Random();

    @Nullable
    public static <T> T randomElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[random.nextInt(array.length)];
    }

    private Helper() {
        throw new AssertionError();
    }
}
