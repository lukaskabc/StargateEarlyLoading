package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;

import java.util.*;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericStargate.NUMBER_OF_CHEVRONS;

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

      /*
      // TODO: could this be more parametrized in a similar way as gate variants?
      // maybe even independently on the stargate type?
      Available strategies:
      - MilkyWay2Step: the ring rotates, chevrons take 2 steps to lock
      - MilkyWay3Step: the ring rotates, chevrons take 3 steps to lock
      - PegasusLoading: Symbols are lighting up according to the Minecraft loading progress
      - PegasusLoop: Symbols are lighting up in an endless loop one at a time, chevrons are locking according to Minecraft loading progress
      - UniverseLoading: Symbols are lighting up according to the Minecraft loading progress, the whole gate is rotating, all chevrons are active
      - UniverseLoop: Chevrons are locking according to loading progress, the whole gate is rotating, no symbol is engaged
      - UniverseLoopAllEngaged: All chevrons are active, the whole gate is rotating, no symbol is engaged
      */


    public static final String MINECRAFT_PROGRESS = "Minecraft Progress";
    public static final String EARLY_PROGRESS = "EARLY";
    public static final int ENCODE_DELAY = 20;
    public static final float EARLY_CHEVRONS = EARLY_LABELS.size();
    public static final float MINECRAFT_CHEVRONS = NUMBER_OF_CHEVRONS - EARLY_CHEVRONS;
    protected final GenericStargate stargate;
    protected final SortedMap<Integer, Runnable> toExecute = new TreeMap<>();
    protected final List<Integer> chevronOrder;
    protected int lastFrameExec = 0;
    protected int lastChevron = 1;

    protected DialingStrategy(GenericStargate stargate, List<Integer> chevronOrder) {
        this.stargate = stargate;
        ensureChevronOrderValid(chevronOrder);
        this.chevronOrder = chevronOrder;
    }

    protected static void ensureChevronOrderValid(List<Integer> chevronOrder) {
        final List<Integer> def = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        if (chevronOrder.size() == def.size() && new HashSet<>(chevronOrder).containsAll(def)) {
            return; // all ok
        }
        throw new InitializationException("Invalid configuration of chevron order: " + chevronOrder + " the order must contain exactly 8 unique numbers from range [1; 8]");
    }

    private final Set<String> earlyLabels = new HashSet<>();

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
        progressChevron(progressMeters, frameNumber);
    }

    protected void progressChevron(List<ProgressMeter> progressMeters, int frameNumber) {
        final float earlyProgress = earlyProgress(progressMeters);
        final float minecraftProgress = getMinecraftProgress(progressMeters);

        for (int chevron = lastChevron; chevron <= NUMBER_OF_CHEVRONS; chevron++) {
            final boolean shouldEncodeEarly = chevron / EARLY_CHEVRONS <= earlyProgress;
            final boolean shouldEncodeMinecraft = chevron / MINECRAFT_CHEVRONS <= minecraftProgress || minecraftProgress > 0.95f;
            if (shouldEncodeEarly || shouldEncodeMinecraft) {
                encodeChevron(chevron > 8 ? 0 : chevronOrder.get(chevron - 1), frameNumber);
            }
        }
    }

    protected void executeAfter(int frame, Runnable runnable) {
        toExecute.put(frame, runnable);
    }

    protected abstract void encodeChevron(int chevron, int frameNumber);
}
