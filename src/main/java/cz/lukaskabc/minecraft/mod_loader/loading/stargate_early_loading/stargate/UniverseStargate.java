package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.SGJourneyModel;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.Color;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateType;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.renderTextureCentered;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.Helper.toRadians;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.Helper.translate;

public class UniverseStargate extends GenericStargate {
    protected static final int UNIVERSE_SIDES = 54;
    protected static final float UNIVERSE_ANGLE = 360F / UNIVERSE_SIDES;

    protected static final float STARGATE_SYMBOL_RING_OUTER_HEIGHT = DEFAULT_RADIUS - 5F / 16;
    protected static final float STARGATE_SYMBOL_RING_INNER_HEIGHT = DEFAULT_RADIUS - 13F / 16;

    protected static final float STARGATE_SYMBOL_RING_OUTER_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_SYMBOL_RING_OUTER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_SYMBOL_RING_OUTER_CENTER = STARGATE_SYMBOL_RING_OUTER_LENGTH / 2;
    protected static final float STARGATE_SYMBOL_RING_INNER_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_SYMBOL_RING_INNER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_SYMBOL_RING_INNER_CENTER = STARGATE_SYMBOL_RING_INNER_LENGTH / 2;

    protected static final float STARGATE_RING_OUTER_RADIUS = DEFAULT_RADIUS - STARGATE_RING_SHRINK;
    protected static final float STARGATE_RING_OUTER_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_RING_OUTER_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_OUTER_CENTER = STARGATE_RING_OUTER_LENGTH / 2;

    protected static final float STARGATE_RING_INNER_HEIGHT = DEFAULT_RADIUS - (DEFAULT_RING_HEIGHT - STARGATE_RING_SHRINK);
    protected static final float STARGATE_RING_INNER_LENGTH = SGJourneyModel.getUsedWidth(UNIVERSE_SIDES, STARGATE_RING_INNER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_INNER_CENTER = STARGATE_RING_INNER_LENGTH / 2;

    protected static final float STARGATE_RING_HEIGHT = STARGATE_RING_OUTER_RADIUS - STARGATE_RING_INNER_HEIGHT;

    public static final int MAX_ROTATION = UNIVERSE_SIDES * 3;
    public static final int ANGLE = MAX_ROTATION / UNIVERSE_SIDES;
    public static final int ROTATION_THIRD = MAX_ROTATION / 3;
    public static final int RESET_DEGREES = ROTATION_THIRD * 2;

    public UniverseStargate(StargateVariant stargateVariant, Config.Symbols symbols) {
        super((short) 36, stargateVariant, symbols);
        if (stargateVariant.getType() != null && stargateVariant.getType() != StargateType.UNIVERSE) {
            throw new IllegalArgumentException("Invalid variant type: " + stargateVariant.getType());
        }
        stargateVariant.setType(StargateType.UNIVERSE);
    }

    @Override
    protected void renderGate(ContextSimpleBuffer contextSimpleBuffer, int frame, Matrix2f matrix2f) {
        final float rotation = ((frame / 5f) % 360) / 156f * 360F;
        matrix2f.rotate((float) Math.toRadians(rotation));
        renderRing(contextSimpleBuffer, matrix2f, rotation);
        renderSymbols(contextSimpleBuffer, matrix2f, rotation);
        renderChevrons(contextSimpleBuffer, matrix2f);
    }

    @Override
    protected void renderRing(ContextSimpleBuffer contextSimpleBuffer, Matrix2f matrix2f, float rotation) {
        Matrix2f m = new Matrix2f(matrix2f);
        m.rotate(toRadians(-UNIVERSE_ANGLE / 2));
        for (int j = 0; j < UNIVERSE_SIDES; j++) {
            renderUniverseRing(contextSimpleBuffer, m, j);
            m.rotate(toRadians(UNIVERSE_ANGLE));
        }
    }

    @Override
    protected void renderSymbols(ContextSimpleBuffer contextSimpleBuffer, Matrix2f matrix2f, float rotation) {
        // the matrix object is reused for each ring segment & symbol
        Matrix2f m = new Matrix2f(matrix2f);
        for (int symbol = 0; symbol < symbolCount; symbol++) {
            renderSymbol(contextSimpleBuffer, m, symbol);
        }
    }

    protected void renderUniverseRing(final ContextSimpleBuffer bb, final Matrix2f matrix2f, final int j) {
        final float texBase = 8F * (j % 6) + 4;

        Vector2f v1 = new Vector2f(-STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);
        Vector2f v2 = new Vector2f(-STARGATE_RING_INNER_CENTER, STARGATE_RING_INNER_HEIGHT);
        Vector2f v3 = new Vector2f(STARGATE_RING_INNER_CENTER, STARGATE_RING_INNER_HEIGHT);
        Vector2f v4 = new Vector2f(STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(texBase + STARGATE_RING_OUTER_CENTER * 16, 15F - STARGATE_RING_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(texBase + STARGATE_RING_INNER_CENTER * 16, 15F + STARGATE_RING_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(texBase - STARGATE_RING_INNER_CENTER * 16, 15F + STARGATE_RING_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(texBase - STARGATE_RING_OUTER_CENTER * 16, 15F - STARGATE_RING_HEIGHT / 2 * 16);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    @Override
    protected void renderSingleSymbol(ContextSimpleBuffer bb, Matrix2f matrix2f, int symbolNumber, float symbolOffset, int textureXSize) {
        final int symbolRow = symbolNumber / 4;
        final int symbolInRow = symbolNumber % 4;

        final Matrix2f m = new Matrix2f(matrix2f);
        m.rotate(toRadians((UNIVERSE_ANGLE * 3 / 2) + (symbolRow * CHEVRON_ANGLE) + (symbolInRow * UNIVERSE_ANGLE)));

        Vector2f v1 = new Vector2f(-STARGATE_SYMBOL_RING_OUTER_CENTER, STARGATE_SYMBOL_RING_OUTER_HEIGHT);
        Vector2f v2 = new Vector2f(-STARGATE_SYMBOL_RING_INNER_CENTER, STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v3 = new Vector2f(STARGATE_SYMBOL_RING_INNER_CENTER, STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v4 = new Vector2f(STARGATE_SYMBOL_RING_OUTER_CENTER, STARGATE_SYMBOL_RING_OUTER_HEIGHT);

        m.transform(v1);
        m.transform(v2);
        m.transform(v3);
        m.transform(v4);

        Vector2f u1 = new Vector2f((symbolOffset - (STARGATE_SYMBOL_RING_OUTER_CENTER * 32 / 16 / textureXSize)) * 64, 0);
        Vector2f u2 = new Vector2f((symbolOffset - (STARGATE_SYMBOL_RING_INNER_CENTER * 32 / 16 / textureXSize)) * 64, 64);
        Vector2f u3 = new Vector2f((symbolOffset + (STARGATE_SYMBOL_RING_INNER_CENTER * 32 / 16 / textureXSize)) * 64, 64);
        Vector2f u4 = new Vector2f((symbolOffset + (STARGATE_SYMBOL_RING_OUTER_CENTER * 32 / 16 / textureXSize)) * 64, 0);

        final Color symbolColor = isSymbolEncoded(symbolNumber) ?
                variant.getSymbols().getEncodedSymbolColor() :
                variant.getSymbols().getSymbolColor();
        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4, symbolColor.packedColor());
    }

    @Override
    protected void renderPrimaryChevron(ContextSimpleBuffer bb, Matrix2f matrix2f) {
        this.renderChevron(bb, matrix2f, 0);
    }

    @Override
    protected void renderChevron(ContextSimpleBuffer bb, Matrix2f matrix2f, int chevron) {
        Matrix3f m = new Matrix3f(matrix2f);
        m.rotate(new Quaternionf().rotationZ(toRadians(CHEVRON_ANGLE * chevron)));
        // * 3.35f I have no idea why this is needed any why chevrons are not positioned correctly otherwise
        translate(m, 0, DEFAULT_RADIUS * 3.35f - (5.5f / 16));
        UniverseChevron.renderChevron(bb, m);
    }

}
