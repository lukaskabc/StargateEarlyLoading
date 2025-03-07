package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;

import java.util.List;

public class UniverseLoading extends UniverseLoopAllEngaged {
    final float singleStep = 1.0f / stargate.getSymbolCount();
    int nextSymbol = 1;

    public UniverseLoading(GenericStargate stargate, List<Integer> chevronOrder) {
        super(stargate, chevronOrder);
    }

    @Override
    public void updateProgress(List<ProgressMeter> progressMeters, int frameNumber) {
        final double progress = Math.ceil(getMinecraftProgress(progressMeters) / singleStep);
        if (progress >= nextSymbol) {
            stargate.encodeSymbol(nextSymbol == stargate.getSymbolCount() ? 0 : nextSymbol);
            nextSymbol++;
        }
    }

    @Override
    protected void encodeChevron(int chevron, int frameNumber) {
        // nothing to do
    }
}
