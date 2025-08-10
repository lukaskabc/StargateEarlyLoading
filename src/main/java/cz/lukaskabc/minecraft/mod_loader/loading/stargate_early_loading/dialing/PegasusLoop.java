package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.GenericStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.PegasusStargate;
import net.minecraftforge.fml.loading.progress.ProgressMeter;

import java.util.List;

public class PegasusLoop extends DialingStrategy {
    private final List<Integer> symbolList;
    private int currentSymbol = 0;

    public PegasusLoop(GenericStargate stargate, List<Integer> chevronOrder) {
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

        int nextSymbol = (currentSymbol + 1) % stargate.getSymbolCount();
        symbolList.set(nextSymbol, symbolList.get(currentSymbol));
        symbolList.set(currentSymbol, -1);
        currentSymbol = nextSymbol;
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
