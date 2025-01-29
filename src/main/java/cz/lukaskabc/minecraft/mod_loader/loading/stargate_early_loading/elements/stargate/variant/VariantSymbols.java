package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant;

public class VariantSymbols {
    private Color symbolColor;
    private Color encodedSymbolColor;
    private Color engagedSymbolColor;

    private String permanentPointOfOrigin;
    private String permanentSymbols;

    private boolean symbolsGlow = false;
    private boolean encodedSymbolsGlow = false;
    private boolean engagedSymbolsGlow = false;
    private boolean engageEncodedSymbols = false;

    public Color getSymbolColor() {
        return symbolColor;
    }

    public void setSymbolColor(Color symbolColor) {
        this.symbolColor = symbolColor;
    }

    public Color getEncodedSymbolColor() {
        return encodedSymbolColor;
    }

    public void setEncodedSymbolColor(Color encodedSymbolColor) {
        this.encodedSymbolColor = encodedSymbolColor;
    }

    public Color getEngagedSymbolColor() {
        return engagedSymbolColor;
    }

    public void setEngagedSymbolColor(Color engagedSymbolColor) {
        this.engagedSymbolColor = engagedSymbolColor;
    }

    public String getPermanentPointOfOrigin() {
        return permanentPointOfOrigin;
    }

    public void setPermanentPointOfOrigin(String permanentPointOfOrigin) {
        this.permanentPointOfOrigin = permanentPointOfOrigin;
    }

    public String getPermanentSymbols() {
        return permanentSymbols;
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

    public boolean isEngageEncodedSymbols() {
        return engageEncodedSymbols;
    }

    public void setEngageEncodedSymbols(boolean engageEncodedSymbols) {
        this.engageEncodedSymbols = engageEncodedSymbols;
    }
}
