package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;

import java.util.List;

public class UniverseLoop extends DialingStrategy {
    public UniverseLoop(GenericStargate stargate) {
        super(stargate);
    }

    @Override
    public void updateProgress(List<ProgressMeter> progressMeters) {

    }
}
