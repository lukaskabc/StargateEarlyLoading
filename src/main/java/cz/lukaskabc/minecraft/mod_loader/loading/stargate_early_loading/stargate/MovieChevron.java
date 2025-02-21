package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericChevron.*;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.renderTextureCentered;

public class MovieChevron {
    protected static final float MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT = 4F / 16;
    protected static final float MOVIE_OUTER_CHEVRON_OUTER_CUTOFF_HEIGHT = 4.4F / 16;
    protected static final float MOVIE_OUTER_CHEVRON_X_OFFSET = (OUTER_CHEVRON_TOP_OFFSET / (OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_BOTTOM_HEIGHT)) * MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT;
    protected static final float MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS = ((OUTER_CHEVRON_TOP_OFFSET + OUTER_CHEVRON_SIDE_TOP_THICKNESS - OUTER_CHEVRON_SIDE_BOTTOM_THICKNESS) / (OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_BOTTOM_HEIGHT)) * MOVIE_OUTER_CHEVRON_OUTER_CUTOFF_HEIGHT;
    protected static final float MOVIE_OUTER_OUTER_X_OFFSET = -0.5F / 16;
    protected static final float MOVIE_OUTER_OUTER_Y_OFFSET = 2F / 16;
    protected static final float MOVIE_OUTER_OUTER_Y_LENGTH = 4F / 16;

    public static void renderMovieChevronFront(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        renderLeftMovieChevron(bb, matrix3f);
        renderLeftMovieOuterChevron(bb, matrix3f);
        renderRightMovieChevron(bb, matrix3f);
        renderRightMovieOuterChevron(bb, matrix3f);
    }

    private static void renderLeftMovieChevron(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        // Left front
        Vector3f v1 = new Vector3f(-(OUTER_CHEVRON_TOP_OFFSET + OUTER_CHEVRON_SIDE_TOP_THICKNESS),
                OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v2 = new Vector3f(-(MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS + MOVIE_OUTER_CHEVRON_X_OFFSET),
                -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v3 = new Vector3f(-MOVIE_OUTER_CHEVRON_X_OFFSET,
                -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v4 = new Vector3f(-OUTER_CHEVRON_TOP_OFFSET,
                OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);

        Vector2f u1 = new Vector2f(47F, 36F);
        Vector2f u2 = new Vector2f((52F - MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS * 16), (45F - MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT * 16));
        Vector2f u3 = new Vector2f((54F - MOVIE_OUTER_CHEVRON_X_OFFSET * 16), (45F - MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT * 16));
        Vector2f u4 = new Vector2f(50F, 36F);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    private static void renderLeftMovieOuterChevron(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        // Left front
        Vector3f v1 = new Vector3f(-(OUTER_CHEVRON_TOP_OFFSET + 2 * OUTER_CHEVRON_SIDE_TOP_THICKNESS + MOVIE_OUTER_OUTER_X_OFFSET),
                OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET - MOVIE_OUTER_OUTER_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v2 = new Vector3f(-(MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS + MOVIE_OUTER_CHEVRON_X_OFFSET + MOVIE_OUTER_OUTER_X_OFFSET),
                -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT - MOVIE_OUTER_OUTER_Y_OFFSET - MOVIE_OUTER_OUTER_Y_LENGTH,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v3 = new Vector3f(-(MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS + MOVIE_OUTER_CHEVRON_X_OFFSET + MOVIE_OUTER_OUTER_X_OFFSET),
                -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT - MOVIE_OUTER_OUTER_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v4 = new Vector3f(-(OUTER_CHEVRON_TOP_OFFSET + OUTER_CHEVRON_SIDE_TOP_THICKNESS + MOVIE_OUTER_OUTER_X_OFFSET),
                OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET - MOVIE_OUTER_OUTER_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);

        Vector2f u1 = new Vector2f(47F, 47F);
        Vector2f u2 = new Vector2f(53F, 56F);
        Vector2f u3 = new Vector2f(53F, 52F);
        Vector2f u4 = new Vector2f(50F, 47F);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    private static void renderRightMovieChevron(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        // Right front
        Vector3f v1 = new Vector3f(OUTER_CHEVRON_TOP_OFFSET,
                OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v2 = new Vector3f(MOVIE_OUTER_CHEVRON_X_OFFSET,
                -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v3 = new Vector3f(MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS + MOVIE_OUTER_CHEVRON_X_OFFSET,
                -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v4 = new Vector3f(OUTER_CHEVRON_TOP_OFFSET + OUTER_CHEVRON_SIDE_TOP_THICKNESS,
                OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);

        Vector2f u1 = new Vector2f(60F, 36F);
        Vector2f u2 = new Vector2f(56F + MOVIE_OUTER_CHEVRON_X_OFFSET * 16, 45F - MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT * 16);
        Vector2f u3 = new Vector2f(58F + MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS * 16, 45F - MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT * 16);
        Vector2f u4 = new Vector2f(63F, 36F);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    private static void renderRightMovieOuterChevron(ContextSimpleBuffer bb, Matrix3f matrix3f) {
        // Right front
        Vector3f v1 = new Vector3f(MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS + MOVIE_OUTER_CHEVRON_X_OFFSET + MOVIE_OUTER_OUTER_X_OFFSET,
                -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT - MOVIE_OUTER_OUTER_Y_OFFSET - MOVIE_OUTER_OUTER_Y_LENGTH,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v2 = new Vector3f(OUTER_CHEVRON_TOP_OFFSET + 2 * OUTER_CHEVRON_SIDE_TOP_THICKNESS + MOVIE_OUTER_OUTER_X_OFFSET,
                OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET - MOVIE_OUTER_OUTER_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v3 = new Vector3f(OUTER_CHEVRON_TOP_OFFSET + OUTER_CHEVRON_SIDE_TOP_THICKNESS + MOVIE_OUTER_OUTER_X_OFFSET,
                OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_SIDE_HEIGHT + OUTER_CHEVRON_Y_OFFSET - MOVIE_OUTER_OUTER_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);
        Vector3f v4 = new Vector3f(MOVIE_OUTER_CHEVRON_BOTTOM_THICKNESS + MOVIE_OUTER_CHEVRON_X_OFFSET + MOVIE_OUTER_OUTER_X_OFFSET,
                -OUTER_CHEVRON_BOTTOM_HEIGHT_CENTER + OUTER_CHEVRON_Y_OFFSET + MOVIE_OUTER_CHEVRON_CUTOFF_HEIGHT - MOVIE_OUTER_OUTER_Y_OFFSET,
                OUTER_CHEVRON_Z_OFFSET);

        Vector2f u1 = new Vector2f(57F, 56F);
        Vector2f u2 = new Vector2f(63F, 47F);
        Vector2f u3 = new Vector2f(60F, 47F);
        Vector2f u4 = new Vector2f(57F, 52F);

        matrix3f.transform(v1);
        matrix3f.transform(v2);
        matrix3f.transform(v3);
        matrix3f.transform(v4);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    private MovieChevron() {
        throw new AssertionError();
    }
}
