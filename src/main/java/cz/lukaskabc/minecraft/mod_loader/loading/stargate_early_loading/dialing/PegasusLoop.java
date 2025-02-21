package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericStargate;

import java.util.List;

public class PegasusLoop extends DialingStrategy {
    public PegasusLoop(GenericStargate stargate, List<Integer> chevronOrder) {
        super(stargate, chevronOrder);
    }

    @Override
    protected void encodeChevron(int chevron, int frameNumber) {

    }


}
