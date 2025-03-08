package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateType;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import org.joml.Matrix2f;

import java.util.ArrayList;
import java.util.List;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.Helper.toRadians;

public class PegasusStargate extends GenericStargate {
    protected final List<Integer> symbolList = new ArrayList<>(symbolCount);

    public PegasusStargate(StargateVariant stargateVariant, Config.Symbols symbols) {
        super((short) 36, stargateVariant, symbols);
        if (stargateVariant.getType() != null && stargateVariant.getType() != StargateType.PEGASUS) {
            throw new IllegalArgumentException("Invalid variant type: " + stargateVariant.getType());
        }
        for (int i = 0; i < symbolCount; i++) {
            symbolList.add(-1);
        }
        stargateVariant.setType(StargateType.PEGASUS);
    }

    @Override
    protected void renderSymbols(ContextSimpleBuffer contextSimpleBuffer, Matrix2f matrix2f, float rotation) {
        // the matrix object is reused for each ring segment & symbol
        Matrix2f m = new Matrix2f(matrix2f);
        for (int symbol = 0; symbol < symbolCount; symbol++) {
            renderSymbolRingSegment(contextSimpleBuffer, m);
            renderSymbol(contextSimpleBuffer, m, symbol);
            // yes the last rotation is wasted
            m.rotate(toRadians(symbolAngle));
        }
        // second loop required for depth handling
        for (int symbol = 0; symbol < symbolCount; symbol++) {
            renderSymbolDivider(contextSimpleBuffer, matrix2f, symbol, 0);
        }
    }

    @Override
    protected void renderSymbol(ContextSimpleBuffer bb, Matrix2f matrix2f, int symbol) {
        if (symbol >= this.symbolCount)
            throw new IllegalArgumentException("Symbol " + symbol + " is out of bounds - symbol count is " + this.symbolCount);

        final int symbolToRender = symbolList.get(symbol);
        if (symbolToRender < 0) {
            return;
        }

        if (symbolToRender == 0) {
            usePoOTexture(bb);
            renderSingleSymbol(bb, matrix2f, 0, 0.5F, 1);
        } else {
            useSymbolsTexture(bb);
            renderSingleSymbol(bb, matrix2f, symbolToRender, symbols.getTextureOffset(symbolToRender), symbols.size());
        }
        useStargateTexture(bb);
    }

    public List<Integer> getSymbolList() {
        return symbolList;
    }
}
