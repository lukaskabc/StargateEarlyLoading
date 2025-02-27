package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.SGJourneyModel;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateType;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import org.joml.Matrix2f;
import org.joml.Vector2f;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.renderTextureCentered;

public class UniverseStargate extends GenericStargate {
    protected static final int UNIVERSE_SIDES = 54;
    protected static final float UNIVERSE_ANGLE = 360F / UNIVERSE_SIDES;

    protected static final float STARGATE_SYMBOL_RING_OUTER_HEIGHT = DEFAULT_RADIUS - 5F / 16;
    protected static final float STARGATE_SYMBOL_RING_INNER_HEIGHT = DEFAULT_RADIUS - 13F / 16;

    protected static final float STARGATE_SYMBOL_RING_OUTER_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_SYMBOL_RING_OUTER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_SYMBOL_RING_OUTER_CENTER = STARGATE_SYMBOL_RING_OUTER_LENGTH / 2;
    protected static final float STARGATE_SYMBOL_RING_INNER_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_SYMBOL_RING_INNER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_SYMBOL_RING_INNER_CENTER = STARGATE_SYMBOL_RING_INNER_LENGTH / 2;

    protected static final float STARGATE_RING_FRONT_THICKNESS = 3F;
    protected static final float STARGATE_RING_BACK_THICKNESS = 4F;
    protected static final float STARGATE_RING_OFFSET = (STARGATE_RING_FRONT_THICKNESS + STARGATE_RING_BACK_THICKNESS) / 2 / 16;
    protected static final float STARGATE_RING_DIVIDE_OFFSET = (STARGATE_RING_BACK_THICKNESS - STARGATE_RING_FRONT_THICKNESS) / 2 / 16;
    protected static final float SYMBOL_OFFSET = STARGATE_RING_OFFSET + 0.001F;

    protected static final float STARGATE_RING_OUTER_RADIUS = DEFAULT_RADIUS - STARGATE_RING_SHRINK;
    protected static final float STARGATE_RING_OUTER_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_RING_OUTER_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_OUTER_CENTER = STARGATE_RING_OUTER_LENGTH / 2;

    protected static final float STARGATE_RING_STOP_RADIUS = DEFAULT_RADIUS - 7F / 16;
    protected static final float STARGATE_RING_STOP_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_RING_STOP_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_STOP_CENTER = STARGATE_RING_STOP_LENGTH / 2;

    protected static final float STARGATE_RING_INNER_HEIGHT = DEFAULT_RADIUS - (DEFAULT_RING_HEIGHT - STARGATE_RING_SHRINK);
    protected static final float STARGATE_RING_INNER_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_RING_INNER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_INNER_CENTER = STARGATE_RING_INNER_LENGTH / 2;

    protected static final float STARGATE_RING_HEIGHT = STARGATE_RING_OUTER_RADIUS - STARGATE_RING_INNER_HEIGHT;
    protected static final float STARGATE_SYMBOL_RING_HEIGHT = STARGATE_SYMBOL_RING_OUTER_HEIGHT - STARGATE_SYMBOL_RING_INNER_HEIGHT;

    protected static final float CHEVRON_LIGHT_THICKNESS = 1F / 16;
    protected static final float CHEVRON_LIGHT_Z_OFFSET = STARGATE_RING_OFFSET + CHEVRON_LIGHT_THICKNESS;

    protected static final float CHEVRON_LIGHT_TOP_LENGTH = 4F / 16;
    protected static final float CHEVRON_LIGHT_TOP_CENTER = CHEVRON_LIGHT_TOP_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_TOP_HEIGHT = 5F / 16;

    protected static final float CHEVRON_LIGHT_MID1_LENGTH = 6F / 16;
    protected static final float CHEVRON_LIGHT_MID1_CENTER = CHEVRON_LIGHT_MID1_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_MID1_HEIGHT = 4F / 16;

    protected static final float CHEVRON_LIGHT_MID2_LENGTH = 3F / 16;
    protected static final float CHEVRON_LIGHT_MID2_CENTER = CHEVRON_LIGHT_MID2_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_MID2_HEIGHT = 2F / 16;

    protected static final float CHEVRON_LIGHT_BOTTOM_LENGTH = 1F / 16;
    protected static final float CHEVRON_LIGHT_BOTTOM_CENTER = CHEVRON_LIGHT_BOTTOM_LENGTH / 2;
    protected static final float CHEVRON_LIGHT_BOTTOM_HEIGHT = 0;

    protected static final float OUTER_CHEVRON_THICKNESS = 0.5F / 16;
    protected static final float OUTER_CHEVRON_Z_OFFSET = STARGATE_RING_OFFSET + OUTER_CHEVRON_THICKNESS;

    private float rotation = 0.0F;

    protected static final float DEFAULT_DISTANCE_FROM_CENTER = 56.0F;
    protected static final int BOXES_PER_RING = 36;

    public UniverseStargate(StargateVariant stargateVariant, Config.Symbols symbols) {
        super((short) 36, stargateVariant, symbols);
        if (stargateVariant.getType() != null && stargateVariant.getType() != StargateType.UNIVERSE) {
            throw new IllegalArgumentException("Invalid variant type: " + stargateVariant.getType());
        }
        stargateVariant.setType(StargateType.UNIVERSE);
    }

    @Override
    protected void renderRing(ContextSimpleBuffer contextSimpleBuffer, Matrix2f matrix2f, float rotation) {
        for (int j = 0; j < UNIVERSE_SIDES; j++) {
            Matrix2f m = new Matrix2f(matrix2f);
            m.rotate(j * -UNIVERSE_ANGLE * 0.017453292F);
            renderOuterRing(contextSimpleBuffer, m, j);
            renderInnerRing(contextSimpleBuffer, m, j);
        }
    }

    protected void renderOuterRing(final ContextSimpleBuffer bb, final Matrix2f matrix2f, final int j) {
        final float texBase = 8F * (j % 6) + 4;

        Vector2f v1 = new Vector2f(-STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);
        Vector2f v2 = new Vector2f(-STARGATE_RING_INNER_CENTER, STARGATE_RING_INNER_HEIGHT);
        Vector2f v3 = new Vector2f(STARGATE_RING_INNER_CENTER, STARGATE_RING_INNER_HEIGHT);
        Vector2f v4 = new Vector2f(STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(texBase - STARGATE_RING_OUTER_CENTER * 16, 15F - STARGATE_RING_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(texBase - STARGATE_RING_INNER_CENTER * 16, 15F + STARGATE_RING_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(texBase + STARGATE_RING_INNER_CENTER * 16, 15F + STARGATE_RING_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(texBase + STARGATE_RING_OUTER_CENTER * 16, 15F - STARGATE_RING_HEIGHT / 2 * 16);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }
}
