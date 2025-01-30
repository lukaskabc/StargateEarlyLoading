package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@NullMarked
public class VariantSymbols {
    private Color symbolColor = new Color();
    @Nullable
    private Color encodedSymbolColor;
    @Nullable
    private Color engagedSymbolColor;

    @Nullable
    private String permanentPointOfOrigin;
    @Nullable
    private String permanentSymbols;

    private boolean symbolsGlow = false;
    private boolean encodedSymbolsGlow = false;
    private boolean engagedSymbolsGlow = false;

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

    public Color getEngagedSymbolColor() {
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

    public boolean isSymbolsGlow() {
        return symbolsGlow;
    }

    public void setSymbolsGlow(boolean symbolsGlow) {
        this.symbolsGlow = symbolsGlow;
    }

    public boolean isEncodedSymbolsGlow() {
        return encodedSymbolsGlow;
    }

    public void setEncodedSymbolsGlow(boolean encodedSymbolsGlow) {
        this.encodedSymbolsGlow = encodedSymbolsGlow;
    }

    public boolean isEngagedSymbolsGlow() {
        return engagedSymbolsGlow;
    }

    public void setEngagedSymbolsGlow(boolean engagedSymbolsGlow) {
        this.engagedSymbolsGlow = engagedSymbolsGlow;
    }
}
