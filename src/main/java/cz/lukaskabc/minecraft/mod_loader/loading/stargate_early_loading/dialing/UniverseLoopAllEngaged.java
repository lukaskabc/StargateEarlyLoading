package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericStargate;

import java.util.List;

public class UniverseLoopAllEngaged extends DialingStrategy {
    public UniverseLoopAllEngaged(GenericStargate stargate, List<Integer> chevronOrder) {
        super(stargate, chevronOrder);
        // engage all chevrons
        for (int i = 0; i < 9; i++) {
            stargate.setChevronEngaged(i, true);
        }
    }

    @Override
    protected void encodeChevron(int chevron, int frameNumber) {
        // do nothing, all chevrons are active
    }
}
