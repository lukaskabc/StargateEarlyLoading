package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;

import java.util.List;

public abstract class DialingStrategy {
    protected final GenericStargate stargate;

    protected DialingStrategy(GenericStargate stargate) {
        this.stargate = stargate;
    }

    public abstract void updateProgress(List<ProgressMeter> progressMeters);

}
