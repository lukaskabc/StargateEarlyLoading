package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@NullMarked
public class VariantSymbols {
    private Color symbolColor = new Color();
    /**
     * The Symbol is encoded if it was dialed but the stargate is not connected yet
     * <p>
     * Because the loading does not display the gate when its inactive, there is no reason for symbolsGlow
     * and because there is no state for connected gate, engaged symbol is not needed either
     */
    @Nullable
    private Color encodedSymbolColor;

    /**
     * The Symbol is engaged if the Stargate is connected
     * <p>
     * Keeping this here to maintain the Color fallback order and keep the behavior of SGJ
     */
    @Nullable
    private Color engagedSymbolColor;

    @Nullable
    private String permanentPointOfOrigin;
    @Nullable
    private String permanentSymbols;

    public Color getSymbolColor() {
        return symbolColor;
    }

    public void setSymbolColor(Color symbolColor) {
        this.symbolColor = symbolColor;
    }

    public Color getEncodedSymbolColor() {
        if (encodedSymbolColor == null) {
            return getEngagedSymbolColor();
        }
        return encodedSymbolColor;
    }

    public void setEncodedSymbolColor(Color encodedSymbolColor) {
        this.encodedSymbolColor = encodedSymbolColor;
    }

    /// keeping private only for fallback purposes
    private Color getEngagedSymbolColor() {
        if (engagedSymbolColor == null) {
            return getSymbolColor();
        }
        return engagedSymbolColor;
    }

    public void setEngagedSymbolColor(Color engagedSymbolColor) {
        this.engagedSymbolColor = engagedSymbolColor;
    }

    public Optional<String> getPermanentPointOfOrigin() {
        return Optional.ofNullable(permanentPointOfOrigin);
    }

    public void setPermanentPointOfOrigin(String permanentPointOfOrigin) {
        this.permanentPointOfOrigin = permanentPointOfOrigin;
    }

    public Optional<String> getPermanentSymbols() {
        return Optional.ofNullable(permanentSymbols);
    }

    public void setPermanentSymbols(String permanentSymbols) {
        this.permanentSymbols = permanentSymbols;
    }
}
