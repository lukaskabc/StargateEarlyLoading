package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MilkyWay2Step extends DialingStrategy {
    private static final Logger LOG = LogManager.getLogger();

    public MilkyWay2Step(GenericStargate stargate) {
        super(stargate);
    }

    @Override
    public void updateProgress(List<ProgressMeter> progressMeters) {
        progressMeters.forEach(m -> {
            LOG.info("{} {}/{} {}", m.name(), m.progress(), m.steps(), m.label().getText());
        });
    }
}
