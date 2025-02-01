package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.Config;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant.StargateType;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant.StargateVariant;

public class UniverseStargate extends GenericStargate {
    public UniverseStargate(StargateVariant stargateVariant, Config.Symbols symbols) {
        super((short) 36, stargateVariant, symbols);
        if (stargateVariant.getType() != null && stargateVariant.getType() != StargateType.PEGASUS) {
            throw new IllegalArgumentException("Invalid variant type: " + stargateVariant.getType());
        }
        stargateVariant.setType(StargateType.PEGASUS);
    }
}
