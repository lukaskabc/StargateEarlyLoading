package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant.StargateType;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant.StargateVariant;

public class MilkyWayStargate extends GenericStargate {
    public MilkyWayStargate(StargateVariant stargateVariant, Config.Symbols symbols) {
        super((short) 39, stargateVariant, symbols);
        if (stargateVariant.getType() != null && stargateVariant.getType() != StargateType.MILKY_WAY) {
            throw new IllegalArgumentException("Invalid variant type: " + stargateVariant.getType());
        }
        stargateVariant.setType(StargateType.MILKY_WAY);

        raiseChevron(0);
        raiseChevron(3);
        engageChevron(1);
        engageChevron(5);
    }
}
