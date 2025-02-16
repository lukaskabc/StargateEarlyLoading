package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;

import java.util.List;

public class MilkyWay2Step extends DialingStrategy {

    public MilkyWay2Step(GenericStargate stargate, List<Integer> chevronOrder) {
        super(stargate, chevronOrder);
    }

    @Override
    protected void encodeChevron(int chevron, int frameNumber) {
        lastChevron++;
        final int finalChevron = chevron;
        lastFrameExec = Math.max(frameNumber, lastFrameExec + ENCODE_DELAY) + ENCODE_DELAY;
        executeAfter(lastFrameExec - ENCODE_DELAY, () -> {
            stargate.raiseChevron(finalChevron);
        });
        executeAfter(lastFrameExec, () -> {
            stargate.engageChevron(finalChevron);
            stargate.lowerChevron(finalChevron);
        });
    }
}
