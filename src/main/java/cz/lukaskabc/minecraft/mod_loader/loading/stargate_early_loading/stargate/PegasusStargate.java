package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateType;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateVariant;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import org.joml.Matrix2f;

public class PegasusStargate extends GenericStargate {
    public PegasusStargate(StargateVariant stargateVariant, Config.Symbols symbols) {
        super((short) 36, stargateVariant, symbols);
        if (stargateVariant.getType() != null && stargateVariant.getType() != StargateType.PEGASUS) {
            throw new IllegalArgumentException("Invalid variant type: " + stargateVariant.getType());
        }
        stargateVariant.setType(StargateType.PEGASUS);
    }

    @Override
    protected void renderSymbolRingSegment(ContextSimpleBuffer bb, Matrix2f m, int symbol, float rotation) {
        super.renderSymbolRingSegment(bb, m, symbol, 0);
    }
}
