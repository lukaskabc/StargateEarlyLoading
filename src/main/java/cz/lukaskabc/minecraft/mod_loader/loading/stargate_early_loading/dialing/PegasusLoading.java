package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;

import java.util.List;

public class PegasusLoading extends PegasusLoop {
    public PegasusLoading(GenericStargate stargate) {
        super(stargate);
    }

    @Override
    public void updateProgress(List<ProgressMeter> progressMeters, int frameNumber) {
        if (isEarlyProgress(progressMeters)) {
            super.updateProgress(progressMeters, frameNumber);
            return;
        }
    }
}
