package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate.NUMBER_OF_CHEVRONS;

public class MilkyWay2Step extends DialingStrategy {
    private static final Logger LOG = LogManager.getLogger();

    private int lastFrameExec = 0;
    private int lastChevron = 1;

    public MilkyWay2Step(GenericStargate stargate) {
        super(stargate);
    }

    @Override
    public void updateProgress(List<ProgressMeter> progressMeters, int frameNumber) {
        super.updateProgress(progressMeters, frameNumber);

        final float earlyProgress = earlyProgress(progressMeters);
        final float minecraftProgress = getMinecraftProgress(progressMeters);

        for (int chevron = lastChevron; chevron <= NUMBER_OF_CHEVRONS; chevron++) {
            final boolean shouldEncodeEarly = chevron / EARLY_CHEVRONS <= earlyProgress;
            final boolean shouldEncodeMinecraft = chevron / MINECRAFT_CHEVRONS <= minecraftProgress || minecraftProgress > 0.95f;
            if (shouldEncodeEarly || shouldEncodeMinecraft) {
                encodeChevron(chevron, frameNumber);
            }
        }
    }

    private void encodeChevron(int chevron, int frameNumber) {
        if (chevron == 9) chevron = 0;
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
