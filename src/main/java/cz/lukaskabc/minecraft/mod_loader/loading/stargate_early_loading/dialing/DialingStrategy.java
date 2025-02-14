package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate.NUMBER_OF_CHEVRONS;

public abstract class DialingStrategy {
    protected static final Set<String> EARLY_LABELS = Set.of(
            "Launching minecraft",
            "Loading mods",
            "Mod Construction",
            "Registry initialization",
            "Loading bootstrap resources",
            "Sided setup",
            "Complete loading of"
    );
    public static final String MINECRAFT_PROGRESS = "Minecraft Progress";
    public static final String EARLY_PROGRESS = "EARLY";
    public static final int ENCODE_DELAY = 20;
    public static final float EARLY_CHEVRONS = EARLY_LABELS.size();
    public static final float MINECRAFT_CHEVRONS = NUMBER_OF_CHEVRONS - EARLY_CHEVRONS;
    protected final GenericStargate stargate;
    private static final Logger LOG = LogManager.getLogger();
    protected final SortedMap<Integer, Runnable> toExecute = new TreeMap<>();

    protected DialingStrategy(GenericStargate stargate) {
        this.stargate = stargate;
    }

    private final Set<String> earlyLabels = new HashSet<>();

    public static float getMinecraftProgress(final List<ProgressMeter> progressMeters) {
        return progressMeters.stream().filter(meter -> MINECRAFT_PROGRESS.equals(meter.name())).findAny()
                .orElseGet(DialingStrategy::getNullMeter)
                .progress() * 4f;
    }

    public static boolean isEarlyProgress(final List<ProgressMeter> progressMeters) {
        return progressMeters.stream().anyMatch(progressMeter -> EARLY_PROGRESS.equals(progressMeter.name()));
    }

    private static ProgressMeter getNullMeter() {
        return new ProgressMeter(null, 1, 0, null);
    }

    private static boolean anyEarlyLabelStartsWith(String label) {
        return EARLY_LABELS.stream().anyMatch(earlyLabel -> earlyLabel.startsWith(label));
    }

    public float earlyProgress(final List<ProgressMeter> progressMeters) {
        if (progressMeters.size() == 1) {
            final String label = progressMeters.getFirst().label().getText();
            if (anyEarlyLabelStartsWith(label)) {
                earlyLabels.add(label);
            }
        } else {
            earlyLabels.addAll(progressMeters.stream().map(m -> m.label().getText()).filter(DialingStrategy::anyEarlyLabelStartsWith).toList());
        }
        return earlyLabels.size() / (float) EARLY_LABELS.size();
    }

    public void updateProgress(List<ProgressMeter> progressMeters, int frameNumber) {
        while (!toExecute.isEmpty() && toExecute.firstKey() <= frameNumber) {
            toExecute.remove(toExecute.firstKey()).run();
        }
    }

    protected void executeAfter(int frame, Runnable runnable) {
        toExecute.put(frame, runnable);
    }
}
