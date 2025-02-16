package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;

import java.util.List;

public class MilkyWay3Step extends DialingStrategy {
    private static final int ENCODE_DELAY_HALF = ENCODE_DELAY / 2;

    public MilkyWay3Step(GenericStargate stargate, List<Integer> chevronOrder) {
        super(stargate, chevronOrder);
    }

    @Override
    protected void encodeChevron(int chevron, int frameNumber) {
        lastChevron++;
        final int finalChevron = chevron;
        final int baseStart = Math.max(frameNumber, lastFrameExec + ENCODE_DELAY);
        lastFrameExec = baseStart + ENCODE_DELAY;
        executeAfter(baseStart, () ->
                stargate.raiseChevron(finalChevron)
        );
        executeAfter(baseStart + ENCODE_DELAY_HALF, () ->
                stargate.engageChevron(finalChevron)
        );
        executeAfter(lastFrameExec, () ->
                stargate.lowerChevron(finalChevron)
        );
    }
}
