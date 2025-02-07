package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.SGJourneyModel;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.STBHelper;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import net.neoforged.fml.earlydisplay.ElementShader;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;
import org.jline.utils.Log;
import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import java.io.FileNotFoundException;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.MEMORY_BAR_HEIGHT;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement.INDEX_TEXTURE_OFFSET;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.renderTexture;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;

public abstract class GenericStargate {
    private static final int STARGATE_TEXTURE_ID = 2;
    private static final int STARGATE_ENGAGED_TEXTURE_ID = 3;
    private static final int STARGATE_SYMBOLS_TEXTURE_ID = 4;
    private static final int STARGATE_POO_SYMBOL_TEXTURE_ID = 5;
    private static final int DEFAULT_TEXTURE_SIZE = 2608;
    public static final float SCALE = 120;
    public static final Vector2f CENTER = new Vector2f(954f, 947f / 2 + MEMORY_BAR_HEIGHT); // TODO: this is not ok
    protected static final float DEFAULT_RADIUS = 3.5F;
    protected static final int DEFAULT_SIDES = 36;
    protected static final float DEFAULT_RING_HEIGHT = 1F;
    protected static final float STARGATE_RING_SHRINK = 0.001F;
    protected static final float DEFAULT_ANGLE = 360F / DEFAULT_SIDES;
    protected static final float NUMBER_OF_CHEVRONS = 9;
    protected static final float CHEVRON_ANGLE = 360F / 9;
    // Ring
    protected static final float STARGATE_RING_THICKNESS = 7F;
    protected static final float STARGATE_RING_OFFSET = STARGATE_RING_THICKNESS / 2 / 16;
    protected static final float STARGATE_RING_OUTER_RADIUS = DEFAULT_RADIUS - STARGATE_RING_SHRINK;
    protected static final float STARGATE_RING_OUTER_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_OUTER_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_OUTER_CENTER = STARGATE_RING_OUTER_LENGTH / 2;
    protected static final float STARGATE_RING_STOP_RADIUS = DEFAULT_RADIUS - 7F / 16;
    protected static final float STARGATE_RING_STOP_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_STOP_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_STOP_CENTER = STARGATE_RING_STOP_LENGTH / 2;
    protected static final float STARGATE_EDGE_TO_CUTOUT_HEIGHT = STARGATE_RING_OUTER_RADIUS - STARGATE_RING_STOP_RADIUS;
    protected static final float STARGATE_RING_START_RADIUS = DEFAULT_RADIUS - 13F / 16;
    protected static final float STARGATE_RING_START_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_START_RADIUS, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_START_CENTER = STARGATE_RING_START_LENGTH / 2;
    protected static final float STARGATE_RING_CUTOUT_HEIGHT = STARGATE_RING_STOP_RADIUS - STARGATE_RING_START_RADIUS;
    protected static final float STARGATE_RING_INNER_HEIGHT = DEFAULT_RADIUS - (DEFAULT_RING_HEIGHT - STARGATE_RING_SHRINK);
    protected static final float STARGATE_RING_INNER_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_INNER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_INNER_CENTER = STARGATE_RING_INNER_LENGTH / 2;
    protected static final float STARGATE_RING_HEIGHT = STARGATE_RING_OUTER_RADIUS - STARGATE_RING_INNER_HEIGHT;
    protected static final float STARGATE_CUTOUT_TO_INNER_HEIGHT = STARGATE_RING_START_RADIUS - STARGATE_RING_INNER_HEIGHT;
    protected static final float STARGATE_SYMBOL_RING_OUTER_HEIGHT = DEFAULT_RADIUS - 6F / 16;
    protected static final float STARGATE_SYMBOL_RING_INNER_HEIGHT = DEFAULT_RADIUS - 14F / 16;
    protected static final float STARGATE_SYMBOL_RING_HEIGHT = STARGATE_SYMBOL_RING_OUTER_HEIGHT - STARGATE_SYMBOL_RING_INNER_HEIGHT;
    protected static final float DIVIDER_Y_CENTER = STARGATE_SYMBOL_RING_HEIGHT / 2 + STARGATE_SYMBOL_RING_INNER_HEIGHT;
    protected static final float DIVIDER_THICKNESS = 1F / 16;
    protected static final float DIVIDER_CENTER = DIVIDER_THICKNESS / 2;
    protected static final float DIVIDER_HEIGHT = 8F / 16;
    protected static final float DIVIDER_OFFSET = 0.5F / 16;
    protected final int symbolCount;
    protected final float symbolAngle;
    protected final float stargateSymbolRingOuterLength;
    protected final float stargateSymbolRingOuterCenter;
    protected final float stargateSymbolRingInnerLength;
    protected final float stargateSymbolRingInnerCenter;
    protected final StargateVariant variant;
    protected final Config.Symbols symbols;

    // bitmap
    private int engagedChevrons = 0;
    // bitmap
    private int raisedChevrons = 0;

    protected GenericStargate(short symbolCount, StargateVariant variant, Config.Symbols symbols) {
        this.symbolCount = symbolCount;
        this.variant = variant;
        this.symbols = symbols;

        this.symbolAngle = 360F / symbolCount;

        this.stargateSymbolRingOuterLength = SGJourneyModel.getUsedWidth(symbolCount, STARGATE_SYMBOL_RING_OUTER_HEIGHT, DEFAULT_RADIUS);
        this.stargateSymbolRingOuterCenter = stargateSymbolRingOuterLength / 2;

        this.stargateSymbolRingInnerLength = SGJourneyModel.getUsedWidth(symbolCount, STARGATE_SYMBOL_RING_INNER_HEIGHT, DEFAULT_RADIUS);
        this.stargateSymbolRingInnerCenter = stargateSymbolRingInnerLength / 2;
    }

    public RenderElement createRenderElement() {
        // texture initialization
        final String pointOfOrigin = variant.getSymbols().getPermanentPointOfOrigin().orElseThrow(() -> new InitializationException("No permanent Point Of Origin defined!"));
        final int textureOffset = GL_TEXTURE0 + INDEX_TEXTURE_OFFSET;
        try {
            // stargate texture
            STBHelper.resolveAndBindTexture(variant.getTexture(), DEFAULT_TEXTURE_SIZE, STARGATE_TEXTURE_ID + textureOffset);
            // stargate engaged texture
            STBHelper.resolveAndBindTexture(variant.getEngagedTexture(), DEFAULT_TEXTURE_SIZE, STARGATE_ENGAGED_TEXTURE_ID + textureOffset);
            // symbols texture
            STBHelper.resolveAndBindTexture(symbols.file(), DEFAULT_TEXTURE_SIZE, STARGATE_SYMBOLS_TEXTURE_ID + textureOffset);
            // Point of Origin texture
            STBHelper.resolveAndBindTexture(pointOfOrigin, DEFAULT_TEXTURE_SIZE, STARGATE_POO_SYMBOL_TEXTURE_ID + textureOffset);
        } catch (FileNotFoundException e) {
            Log.error("Failed to load texture: ", e.getMessage());
            throw new InitializationException(e);
        }
        return RefRenderElement.constructor(this::render);
    }

    public void engageChevron(int chevron) {
        engagedChevrons = engagedChevrons | (1 << chevron);
    }

    public boolean isChevronEngaged(int chevron) {
        return (engagedChevrons & (1 << chevron)) > 0;
    }

    public void raiseChevron(int chevron) {
        raisedChevrons = raisedChevrons | (1 << chevron);
    }

    public boolean isChevronRaised(int chevron) {
        return (raisedChevrons & (1 << chevron)) > 0;
    }

    protected static void renderInnerRing(final ContextSimpleBuffer bb, final Matrix2f matrix2f, final int j) {
        final float texBase = 10f * (j % 4) + 5;

        Vector2f v1 = new Vector2f(-STARGATE_RING_START_CENTER, STARGATE_RING_START_RADIUS);
        Vector2f v2 = new Vector2f(-STARGATE_RING_INNER_CENTER, STARGATE_RING_INNER_HEIGHT);
        Vector2f v3 = new Vector2f(STARGATE_RING_INNER_CENTER, STARGATE_RING_INNER_HEIGHT);
        Vector2f v4 = new Vector2f(STARGATE_RING_START_CENTER, STARGATE_RING_START_RADIUS);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(texBase - STARGATE_RING_START_CENTER * 16, 33.5F - STARGATE_CUTOUT_TO_INNER_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(texBase - STARGATE_RING_INNER_CENTER * 16, 33.5F + STARGATE_CUTOUT_TO_INNER_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(texBase + STARGATE_RING_INNER_CENTER * 16, 33.5F + STARGATE_CUTOUT_TO_INNER_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(texBase + STARGATE_RING_START_CENTER * 16, 33.5F - STARGATE_CUTOUT_TO_INNER_HEIGHT / 2 * 16);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    protected static void renderOuterRing(final ContextSimpleBuffer bb, final Matrix2f matrix2f, final int j) {
        final float texBase = 10F * (j % 4) + 5;

        Vector2f v1 = new Vector2f(-STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);
        Vector2f v2 = new Vector2f(-STARGATE_RING_STOP_CENTER, STARGATE_RING_STOP_RADIUS);
        Vector2f v3 = new Vector2f(STARGATE_RING_STOP_CENTER, STARGATE_RING_STOP_RADIUS);
        Vector2f v4 = new Vector2f(STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(texBase - 5, 10.5F - STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(texBase - 5, 10.5F + STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(texBase + 5, 10.5F + STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(texBase + 5, 10.5F - STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    private void initRender(ContextSimpleBuffer contextSimpleBuffer) {
        contextSimpleBuffer.context().elementShader().updateTextureUniform(STARGATE_TEXTURE_ID + INDEX_TEXTURE_OFFSET);
        contextSimpleBuffer.context().elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
        contextSimpleBuffer.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }


    public void render(ContextSimpleBuffer contextSimpleBuffer, int frame) {
        initRender(contextSimpleBuffer);
        final float rotation = ((frame / 5f) % 360) / 156f * 360F;

        Matrix2f matrix2f = new Matrix2f();
        matrix2f.scale(SCALE);
        matrix2f.scale(-1); // rotate 180 degrees

        for (int symbol = 0; symbol < symbolCount; symbol++) {
            renderSymbolRingSegment(contextSimpleBuffer, matrix2f, symbol, rotation);
        }

        // second loop required for depth handling
        for (int symbol = 0; symbol < symbolCount; symbol++) {
            renderSymbolDivider(contextSimpleBuffer, matrix2f, symbol, rotation);
        }

        for (int j = 0; j < DEFAULT_SIDES; j++) {
            Matrix2f m = new Matrix2f(matrix2f);
            m.rotate(j * -DEFAULT_ANGLE * 0.017453292F);
            renderOuterRing(contextSimpleBuffer, m, j);
            renderInnerRing(contextSimpleBuffer, m, j);
        }

        renderChevrons(contextSimpleBuffer, matrix2f);
        contextSimpleBuffer.simpleBufferBuilder().draw();
    }

    protected void renderSymbolDivider(ContextSimpleBuffer bb, Matrix2f m, int j, float rotation) {
        Matrix2f matrix2f = new Matrix2f(m);
        matrix2f.rotate((float) Math.toRadians(j * symbolAngle - symbolAngle / 2 + rotation));

        Vector2f v1 = new Vector2f(-DIVIDER_CENTER, DIVIDER_Y_CENTER + DIVIDER_HEIGHT / 2);
        Vector2f v2 = new Vector2f(-DIVIDER_CENTER, DIVIDER_Y_CENTER - DIVIDER_HEIGHT / 2);
        Vector2f v3 = new Vector2f(DIVIDER_CENTER, DIVIDER_Y_CENTER - DIVIDER_HEIGHT / 2);
        Vector2f v4 = new Vector2f(DIVIDER_CENTER, DIVIDER_Y_CENTER + DIVIDER_HEIGHT / 2);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(9.5F - DIVIDER_CENTER * 16, 46 - DIVIDER_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(9.5F - DIVIDER_CENTER * 16, 46 + DIVIDER_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(9.5F + DIVIDER_CENTER * 16, 46 + DIVIDER_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(9.5F + DIVIDER_CENTER * 16, 46 - DIVIDER_HEIGHT / 2 * 16);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
    }

    protected void renderSymbolRingSegment(ContextSimpleBuffer bb, Matrix2f m, int symbol, float rotation) {
        Matrix2f matrix2f = new Matrix2f(m);
        matrix2f.rotate((float) Math.toRadians(symbol * symbolAngle + rotation));

        Vector2f v1 = new Vector2f(-stargateSymbolRingOuterCenter, STARGATE_SYMBOL_RING_OUTER_HEIGHT);
        Vector2f v2 = new Vector2f(-stargateSymbolRingInnerCenter, STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v3 = new Vector2f(stargateSymbolRingInnerCenter, STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v4 = new Vector2f(stargateSymbolRingOuterCenter, STARGATE_SYMBOL_RING_OUTER_HEIGHT);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(4 - stargateSymbolRingOuterCenter * 16, 46 - STARGATE_SYMBOL_RING_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(4 - stargateSymbolRingInnerCenter * 16, 46 + STARGATE_SYMBOL_RING_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(4 + stargateSymbolRingInnerCenter * 16, 46 + STARGATE_SYMBOL_RING_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(4 + stargateSymbolRingOuterCenter * 16, 46 - STARGATE_SYMBOL_RING_HEIGHT / 2 * 16);

        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER);
        renderSymbol(bb, matrix2f, symbol);
    }

    private void renderChevrons(ContextSimpleBuffer bb, Matrix2f matrix2f) {
        renderPrimaryChevron(bb, matrix2f);
        for (int chevron = 1; chevron < NUMBER_OF_CHEVRONS; chevron++) {
            renderChevron(bb, matrix2f, chevron);
        }

        useEngagedStargateTexture(bb);
        if (isChevronEngaged(0)) {
            renderPrimaryChevron(bb, matrix2f);
        }
        for (int chevron = 1; chevron < NUMBER_OF_CHEVRONS; chevron++) {
            if (isChevronEngaged(chevron)) {
                renderChevron(bb, matrix2f, chevron);
            }
        }
        useStargateTexture(bb);
    }

    protected void useStargateTexture(ContextSimpleBuffer bb) {
        bb.simpleBufferBuilder().draw();
        bb.context().elementShader().updateTextureUniform(STARGATE_TEXTURE_ID + INDEX_TEXTURE_OFFSET);
        // not updating type uniform as it is already texture
        bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }

    protected void useEngagedStargateTexture(ContextSimpleBuffer bb) {
        bb.simpleBufferBuilder().draw();
        bb.context().elementShader().updateTextureUniform(STARGATE_ENGAGED_TEXTURE_ID + INDEX_TEXTURE_OFFSET);
        bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }

    protected void useSymbolsTexture(ContextSimpleBuffer bb) {
        bb.simpleBufferBuilder().draw();
        bb.context().elementShader().updateTextureUniform(STARGATE_SYMBOLS_TEXTURE_ID + INDEX_TEXTURE_OFFSET);
        bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }

    protected void usePoOTexture(ContextSimpleBuffer bb) {
        bb.simpleBufferBuilder().draw();
        bb.context().elementShader().updateTextureUniform(STARGATE_POO_SYMBOL_TEXTURE_ID + INDEX_TEXTURE_OFFSET);
        bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }

    protected void renderChevron(ContextSimpleBuffer bb, Matrix2f matrix2f, int chevron) {
        // 3D matrix required for translation
        Matrix3f matrix3f = new Matrix3f(matrix2f);
        matrix3f.rotate(new Quaternionf().rotationZ((float) Math.toRadians(CHEVRON_ANGLE * chevron)));

        // translation
        translate(matrix3f, 0, DEFAULT_RADIUS - (2.5f / 16));
        final boolean isRaised = isChevronRaised(chevron);

        GenericChevron.renderChevronLight(bb, matrix3f, isRaised);
        GenericChevron.renderOuterChevronFront(bb, matrix3f, isRaised);
    }

    protected void renderSingleSymbol(ContextSimpleBuffer bb, Matrix2f matrix2f, float symbolOffset, int textureXSize) {
        // TODO: symbol scale down - keep it or leave it
        /*
        final float x = 0.09f * (stargateSymbolRingOuterCenter - stargateSymbolRingInnerCenter);
        final float y = 0.09f * (STARGATE_SYMBOL_RING_OUTER_HEIGHT - STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v1 = new Vector2f(-stargateSymbolRingOuterCenter + x, STARGATE_SYMBOL_RING_OUTER_HEIGHT - y);
        Vector2f v2 = new Vector2f(-stargateSymbolRingInnerCenter + x, STARGATE_SYMBOL_RING_INNER_HEIGHT + y);
        Vector2f v3 = new Vector2f(stargateSymbolRingInnerCenter - x, STARGATE_SYMBOL_RING_INNER_HEIGHT + y);
        Vector2f v4 = new Vector2f(stargateSymbolRingOuterCenter - x, STARGATE_SYMBOL_RING_OUTER_HEIGHT - y);
         */

        Vector2f v1 = new Vector2f(-stargateSymbolRingOuterCenter, STARGATE_SYMBOL_RING_OUTER_HEIGHT);
        Vector2f v2 = new Vector2f(-stargateSymbolRingInnerCenter, STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v3 = new Vector2f(stargateSymbolRingInnerCenter, STARGATE_SYMBOL_RING_INNER_HEIGHT);
        Vector2f v4 = new Vector2f(stargateSymbolRingOuterCenter, STARGATE_SYMBOL_RING_OUTER_HEIGHT);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f((symbolOffset - (stargateSymbolRingOuterCenter * 32 / 16 / textureXSize)) * 64, (8 - STARGATE_SYMBOL_RING_HEIGHT / 2 * 32) * 4);
        Vector2f u2 = new Vector2f((symbolOffset - (stargateSymbolRingInnerCenter * 32 / 16 / textureXSize)) * 64, (8 + STARGATE_SYMBOL_RING_HEIGHT / 2 * 32) * 4);
        Vector2f u3 = new Vector2f((symbolOffset + (stargateSymbolRingInnerCenter * 32 / 16 / textureXSize)) * 64, (8 + STARGATE_SYMBOL_RING_HEIGHT / 2 * 32) * 4);
        Vector2f u4 = new Vector2f((symbolOffset + (stargateSymbolRingOuterCenter * 32 / 16 / textureXSize)) * 64, (8 - STARGATE_SYMBOL_RING_HEIGHT / 2 * 32) * 4);

        // TODO: engaged symbols
        renderTexture(bb, v1, v2, v3, v4, u1, u2, u3, u4, CENTER, variant.getSymbols().getSymbolColor().packedColor());
    }

    protected void renderSymbol(ContextSimpleBuffer bb, Matrix2f matrix2f, int symbol) {
        if (symbol >= this.symbolCount)
            throw new IllegalArgumentException("Symbol " + symbol + " is out of bounds - symbol count is " + this.symbolCount);

        if (symbol == 0) {
            usePoOTexture(bb);
            renderSingleSymbol(bb, matrix2f, 0.5F, 1);
        } else {
            useSymbolsTexture(bb);
            renderSingleSymbol(bb, matrix2f, symbols.getTextureOffset(symbol), symbols.size());
        }
        useStargateTexture(bb);
    }

    public void translate(Matrix3f matrix3f, float x, float y) {
        matrix3f.m20 += matrix3f.m00 * x + matrix3f.m10 * y;
        matrix3f.m21 += matrix3f.m01 * x + matrix3f.m11 * y;
        matrix3f.m22 += matrix3f.m02 * x + matrix3f.m12 * y;
    }

    protected void renderPrimaryChevron(ContextSimpleBuffer bb, Matrix2f matrix2f) {
        // 3D matrix required for translation
        Matrix3f matrix3f = new Matrix3f(matrix2f);
        // translation
        translate(matrix3f, 0, DEFAULT_RADIUS - (2.5f / 16));

        final boolean isRaised = isChevronRaised(0);
        final boolean isMovieChevron = variant.getStargateModel().isMoviePrimaryChevron();

        GenericChevron.renderChevronLight(bb, matrix3f, isRaised && !isMovieChevron);
        if (isMovieChevron) {
            MovieChevron.renderMovieChevronFront(bb, matrix3f);
        } else {
            GenericChevron.renderOuterChevronFront(bb, matrix3f, isRaised);
        }
    }
}
