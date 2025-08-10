package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.SGJourneyModel;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original.StaticSTBHelper;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.Color;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import net.minecraftforge.fml.earlydisplay.ElementShader;
import net.minecraftforge.fml.earlydisplay.RenderElement;
import net.minecraftforge.fml.earlydisplay.SimpleBufferBuilder;
import org.jline.utils.Log;
import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL32C;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.BufferHelper.renderTextureCentered;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.Helper.toRadians;
import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.Helper.translate;

public abstract class GenericStargate {
    private static final int DEFAULT_TEXTURE_SIZE = 2608;
    protected static final float DEFAULT_RADIUS = 3.5F;
    protected static final int DEFAULT_SIDES = 36;
    protected static final float DEFAULT_RING_HEIGHT = 1F;
    protected static final float STARGATE_RING_SHRINK = 0.001F;
    protected static final float DEFAULT_ANGLE = 360F / DEFAULT_SIDES;
    public static final int NUMBER_OF_CHEVRONS = 9;
    protected static final float CHEVRON_ANGLE = 360F / 9;
    // Ring
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
    protected static final float STARGATE_RING_INNER_HEIGHT = DEFAULT_RADIUS - (DEFAULT_RING_HEIGHT - STARGATE_RING_SHRINK);
    protected static final float STARGATE_RING_INNER_LENGTH = SGJourneyModel.getUsedWidth(DEFAULT_SIDES, STARGATE_RING_INNER_HEIGHT, DEFAULT_RADIUS);
    protected static final float STARGATE_RING_INNER_CENTER = STARGATE_RING_INNER_LENGTH / 2;
    protected static final float STARGATE_CUTOUT_TO_INNER_HEIGHT = STARGATE_RING_START_RADIUS - STARGATE_RING_INNER_HEIGHT;
    protected static final float STARGATE_SYMBOL_RING_OUTER_HEIGHT = DEFAULT_RADIUS - 6F / 16;
    protected static final float STARGATE_SYMBOL_RING_INNER_HEIGHT = DEFAULT_RADIUS - 14F / 16;
    protected static final float STARGATE_SYMBOL_RING_HEIGHT = STARGATE_SYMBOL_RING_OUTER_HEIGHT - STARGATE_SYMBOL_RING_INNER_HEIGHT;
    protected static final float DIVIDER_Y_CENTER = STARGATE_SYMBOL_RING_HEIGHT / 2 + STARGATE_SYMBOL_RING_INNER_HEIGHT;
    protected static final float DIVIDER_THICKNESS = 1F / 16;
    protected static final float DIVIDER_CENTER = DIVIDER_THICKNESS / 2;
    protected static final float DIVIDER_HEIGHT = 8F / 16;
    protected final int symbolCount;
    protected final float symbolAngle;
    protected final float stargateSymbolRingOuterLength;
    protected final float stargateSymbolRingOuterCenter;
    protected final float stargateSymbolRingInnerLength;
    protected final float stargateSymbolRingInnerCenter;
    protected final StargateVariant variant;
    protected final Config.Symbols symbols;
    protected int stargateTextureId;
    protected int stargateEngagedTextureId;
    protected int stargateSymbolsTextureId;
    protected int stargatePooSymbolTextureId;

    private final List<Integer> encodedAddress = new ArrayList<>(9);

    /// bitmap
    private volatile int engagedChevrons = 0;
    /// bitmap
    private volatile int raisedChevrons = 0;
    /// bitmap
    private volatile long encodedSymbols = 0;

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
        int[] width = new int[1];
        int[] height = new int[1];

        try {
            // stargate texture
            stargateTextureId = StaticSTBHelper.resolveAndBindTexture(variant.getTexture(), width, height);
            // stargate engaged texture
            stargateEngagedTextureId = StaticSTBHelper.resolveAndBindTexture(variant.getEngagedTexture(), width, height);
            // symbols texture
            stargateSymbolsTextureId = StaticSTBHelper.resolveAndBindTexture(symbols.file(), width, height);
            // Point of Origin texture
            stargatePooSymbolTextureId = StaticSTBHelper.resolveAndBindTexture(pointOfOrigin, width, height);
        } catch (FileNotFoundException e) {
            Log.error("Failed to load texture: ", e.getMessage());
            throw new InitializationException(e);
        }
        return RefRenderElement.constructor(this::render);
    }

    public synchronized void encodeSymbol(int symbol) {
        if (symbol < 0 || symbol >= symbolCount) {
            throw new IllegalArgumentException("Symbol " + symbol + " is out of bounds - symbol count is " + symbolCount);
        }
        encodedSymbols |= (1L << symbol);
    }

    public synchronized void engageChevron(int chevron) {
        variant.engageChevron(chevron, this);
    }

    public synchronized void raiseChevron(int chevron) {
        variant.raiseChevron(chevron, this);
    }

    public synchronized void lowerChevron(int chevron) {
        variant.lowerChevron(chevron, this);
    }

    public synchronized void setChevronEngaged(int chevron, boolean engaged) {
        if (engaged) {
            engagedChevrons |= (1 << chevron);
        } else {
            engagedChevrons &= ~(1 << chevron);
        }
    }

    public boolean isChevronEngaged(int chevron) {
        return (engagedChevrons & (1 << chevron)) > 0;
    }

    public synchronized void doRaiseChevron(int chevron) {
        raisedChevrons = raisedChevrons | (1 << chevron);
    }

    public synchronized void doLowerChevron(int chevron) {
        raisedChevrons = raisedChevrons & ~(1 << chevron);
    }

    public boolean isChevronRaised(int chevron) {
        return (raisedChevrons & (1 << chevron)) > 0;
    }

    public boolean isSymbolEncoded(int symbol) {
        return (encodedSymbols & (1L << symbol)) > 0;
    }

    protected void renderInnerRing(final ContextSimpleBuffer bb, final Matrix2f matrix2f, final int j) {
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

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    protected void renderOuterRing(final ContextSimpleBuffer bb, final Matrix2f matrix2f, final int j) {
        final float texBase = 10F * (j % 4) + 5;

        Vector2f v1 = new Vector2f(-STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);
        Vector2f v2 = new Vector2f(-STARGATE_RING_STOP_CENTER, STARGATE_RING_STOP_RADIUS);
        Vector2f v3 = new Vector2f(STARGATE_RING_STOP_CENTER, STARGATE_RING_STOP_RADIUS);
        Vector2f v4 = new Vector2f(STARGATE_RING_OUTER_CENTER, STARGATE_RING_OUTER_RADIUS);

        matrix2f.transform(v1);
        matrix2f.transform(v2);
        matrix2f.transform(v3);
        matrix2f.transform(v4);

        Vector2f u1 = new Vector2f(texBase - STARGATE_RING_OUTER_CENTER * 16, 10.5F - STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u2 = new Vector2f(texBase - STARGATE_RING_STOP_CENTER * 16, 10.5F + STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u3 = new Vector2f(texBase + STARGATE_RING_STOP_CENTER * 16, 10.5F + STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);
        Vector2f u4 = new Vector2f(texBase + STARGATE_RING_OUTER_CENTER * 16, 10.5F - STARGATE_EDGE_TO_CUTOUT_HEIGHT / 2 * 16);

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    protected void initRender(ContextSimpleBuffer contextSimpleBuffer) {
        GL32C.glBindTexture(GL11C.GL_TEXTURE_2D, stargateTextureId);
        contextSimpleBuffer.context().elementShader().updateTextureUniform(0);
        contextSimpleBuffer.context().elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
        contextSimpleBuffer.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }


    public void render(ContextSimpleBuffer contextSimpleBuffer, int frame) {
        initRender(contextSimpleBuffer);

        Matrix2f matrix2f = new Matrix2f();
        final int base = Math.min(contextSimpleBuffer.context().scaledHeight(), contextSimpleBuffer.context().scaledWidth());
        matrix2f.scale(base * 0.4f / DEFAULT_RADIUS); // TODO: auto calc scale
        matrix2f.scale(-1); // rotate 180 degrees

        renderGate(contextSimpleBuffer, frame, matrix2f);

        contextSimpleBuffer.simpleBufferBuilder().draw();
    }

    protected void renderSymbols(ContextSimpleBuffer contextSimpleBuffer, Matrix2f matrix2f, float rotation) {
        // the matrix object is reused for each ring segment & symbol
        Matrix2f m = new Matrix2f(matrix2f);
        m.rotate(toRadians(rotation));
        for (int symbol = 0; symbol < symbolCount; symbol++) {
            renderSymbolRingSegment(contextSimpleBuffer, m);
            renderSymbol(contextSimpleBuffer, m, symbol);
            // yes the last rotation is wasted
            m.rotate(toRadians(symbolAngle));
        }
        // second loop required for depth handling
        for (int symbol = 0; symbol < symbolCount; symbol++) {
            renderSymbolDivider(contextSimpleBuffer, matrix2f, symbol, rotation);
        }
    }

    protected void renderRing(ContextSimpleBuffer contextSimpleBuffer, Matrix2f matrix2f, float rotation) {
        for (int j = 0; j < DEFAULT_SIDES; j++) {
            Matrix2f m = new Matrix2f(matrix2f);
            m.rotate(toRadians(j * -DEFAULT_ANGLE));
            renderOuterRing(contextSimpleBuffer, m, j);
            renderInnerRing(contextSimpleBuffer, m, j);
        }
    }

    protected void renderGate(ContextSimpleBuffer contextSimpleBuffer, int frame, Matrix2f matrix2f) {
        final float rotation = ((frame / 5f) % 360) / 156f * 360F;
        renderSymbols(contextSimpleBuffer, matrix2f, rotation);
        renderRing(contextSimpleBuffer, matrix2f, rotation);
        renderChevrons(contextSimpleBuffer, matrix2f);
    }

    protected void renderSymbolDivider(ContextSimpleBuffer bb, Matrix2f m, int j, float rotation) {
        Matrix2f matrix2f = new Matrix2f(m);
        matrix2f.rotate(toRadians(j * symbolAngle - symbolAngle / 2 + rotation));

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

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    protected void renderSymbolRingSegment(ContextSimpleBuffer bb, Matrix2f matrix2f) {
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

        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4);
    }

    protected void renderChevrons(ContextSimpleBuffer bb, Matrix2f matrix2f) {
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
        GL32C.glBindTexture(GL11C.GL_TEXTURE_2D, stargateTextureId);
        bb.context().elementShader().updateTextureUniform(0);
        // not updating type uniform as it is already texture
        bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }

    protected void useEngagedStargateTexture(ContextSimpleBuffer bb) {
        bb.simpleBufferBuilder().draw();
        GL32C.glBindTexture(GL11C.GL_TEXTURE_2D, stargateEngagedTextureId);
        bb.context().elementShader().updateTextureUniform(0);
        bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }

    protected void useSymbolsTexture(ContextSimpleBuffer bb) {
        bb.simpleBufferBuilder().draw();
        GL32C.glBindTexture(GL11C.GL_TEXTURE_2D, stargateSymbolsTextureId);
        bb.context().elementShader().updateTextureUniform(0);
        bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }

    protected void usePoOTexture(ContextSimpleBuffer bb) {
        bb.simpleBufferBuilder().draw();
        GL32C.glBindTexture(GL11C.GL_TEXTURE_2D, stargatePooSymbolTextureId);
        bb.context().elementShader().updateTextureUniform(0);
        bb.simpleBufferBuilder().begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
    }

    protected void renderChevron(ContextSimpleBuffer bb, Matrix2f matrix2f, int chevron) {
        // 3D matrix required for translation
        Matrix3f matrix3f = new Matrix3f(matrix2f);
        matrix3f.rotate(new Quaternionf().rotationZ(toRadians(CHEVRON_ANGLE * chevron)));

        // translation
        translate(matrix3f, 0, DEFAULT_RADIUS - (2.5f / 16));
        final boolean isRaised = isChevronRaised(chevron);

        GenericChevron.renderChevronLight(bb, matrix3f, isRaised);
        GenericChevron.renderOuterChevronFront(bb, matrix3f, isRaised);
    }

    protected void renderSingleSymbol(ContextSimpleBuffer bb, Matrix2f matrix2f, int symbolNumber, float symbolOffset, int textureXSize) {
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

        final Color symbolColor = isSymbolEncoded(symbolNumber) ?
                variant.getSymbols().getEncodedSymbolColor() :
                variant.getSymbols().getSymbolColor();
        renderTextureCentered(bb, v1, v2, v3, v4, u1, u2, u3, u4, symbolColor.packedColor());
    }

    protected void renderSymbol(ContextSimpleBuffer bb, Matrix2f matrix2f, int symbol) {
        if (symbol >= this.symbolCount)
            throw new IllegalArgumentException("Symbol " + symbol + " is out of bounds - symbol count is " + this.symbolCount);

        if (symbol == 0) {
            usePoOTexture(bb);
            renderSingleSymbol(bb, matrix2f, symbol, 0.5F, 1);
        } else {
            useSymbolsTexture(bb);
            renderSingleSymbol(bb, matrix2f, symbol, symbols.getTextureOffset(symbol), symbols.size());
        }
        useStargateTexture(bb);
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

    public StargateVariant getVariant() {
        return variant;
    }

    public int getSymbolCount() {
        return symbolCount;
    }
}
