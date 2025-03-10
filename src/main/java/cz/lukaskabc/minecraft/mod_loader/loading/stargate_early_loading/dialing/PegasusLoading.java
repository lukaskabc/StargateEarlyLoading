package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.PegasusStargate;
import net.neoforged.fml.loading.progress.ProgressMeter;

import java.util.List;

public class PegasusLoading extends DialingStrategy {
    private final List<Integer> symbolList;
    private int currentSymbol = 0;
    private boolean mcNotLoading = true;

    public PegasusLoading(GenericStargate stargate, List<Integer> chevronOrder) {
        super(stargate, chevronOrder);
        if (stargate instanceof PegasusStargate pegasusStargate) {
            this.symbolList = pegasusStargate.getSymbolList();
        } else {
            throw new InitializationException("Not a pegasus stargate type: " + stargate.getClass().getSimpleName());
        }
        this.symbolList.set(currentSymbol, currentSymbol);
    }

    @Override
    public void updateProgress(List<ProgressMeter> progressMeters, int frameNumber) {
        super.updateProgress(progressMeters, frameNumber);

        final float mcProgress = getMinecraftProgress(progressMeters);
        int nextSymbol = (currentSymbol + 1) % stargate.getSymbolCount();
        if (mcProgress <= 0) {
            symbolList.set(nextSymbol, symbolList.get(currentSymbol));
            symbolList.set(currentSymbol, -1);
            currentSymbol = nextSymbol;
        } else if (1f / stargate.getSymbolCount() * nextSymbol <= mcProgress) {
            if (mcNotLoading) {
                symbolList.set(currentSymbol, -1);
                currentSymbol = 1;
                nextSymbol = 2;
                mcNotLoading = false;
            }
            symbolList.set(currentSymbol, currentSymbol);
            currentSymbol = nextSymbol;
        }
    }

    @Override
    protected void encodeChevron(int chevron, int frameNumber) {
        final int finalChevron = chevron;
        final int baseStart = Math.max(frameNumber, lastFrameExec + 1);
        lastFrameExec = baseStart + 1;
        executeAfter(baseStart, () -> {
            stargate.engageChevron(finalChevron);
        });
        lastChevron++;
    }
}
