package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import org.joml.Matrix3f;
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

    public static float toRadians(float degrees) {
        return (float) Math.toRadians(degrees);
    }

    public static void translate(Matrix3f matrix3f, float x, float y) {
        matrix3f.m20 += matrix3f.m00 * x + matrix3f.m10 * y;
        matrix3f.m21 += matrix3f.m01 * x + matrix3f.m11 * y;
        matrix3f.m22 += matrix3f.m02 * x + matrix3f.m12 * y;
    }

    private Helper() {
        throw new AssertionError();
    }
}
