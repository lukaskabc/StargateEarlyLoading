package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;

import java.util.List;

public abstract class DialingStrategy {
    public static final String MINECRAFT_PROGRESS = "Minecraft Progress";
    public static final String EARLY_PROGRESS = "EARLY";
    protected final GenericStargate stargate;

    protected DialingStrategy(GenericStargate stargate) {
        this.stargate = stargate;
    }

    public abstract void updateProgress(List<ProgressMeter> progressMeters);

    public static float getMinecraftProgress(final List<ProgressMeter> progressMeters) {
        return progressMeters.stream().filter(meter -> MINECRAFT_PROGRESS.equals(meter.name())).findAny()
                .orElseGet(DialingStrategy::getNullMeter)
                .progress();
    }

    public static boolean isEarlyProgress(final List<ProgressMeter> progressMeters) {
        return progressMeters.stream().anyMatch(progressMeter -> EARLY_PROGRESS.equals(progressMeter.name()));
    }

    private static ProgressMeter getNullMeter() {
        return new ProgressMeter(null, 1, 0, null);
    }
}
