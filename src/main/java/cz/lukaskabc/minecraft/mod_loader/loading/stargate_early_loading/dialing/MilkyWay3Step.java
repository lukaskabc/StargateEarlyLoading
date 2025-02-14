package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;

public class MilkyWay3Step extends MilkyWay2Step {
    private static final int ENCODE_DELAY_HALF = ENCODE_DELAY / 2;
    public MilkyWay3Step(GenericStargate stargate) {
        super(stargate);
    }

    @Override
    protected void encodeChevron(int chevron, int frameNumber) {
        if (chevron == 9) chevron = 0;
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
