package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import org.jspecify.annotations.Nullable;

import java.util.Map;

public class Config {
    private AllowedVariants variants = new AllowedVariants();
    private Map<String, Symbols> symbols = Map.of();
    @Nullable
    private String background;

    public static class AllowedVariants {
        private String[] milkyWay = new String[0];
        private String[] pegasus = new String[0];
        private String[] universe = new String[0];

        // region <Getters & Setters>
        public String[] getMilkyWay() {
            return milkyWay;
        }

        public void setMilkyWay(String[] milkyWay) {
            this.milkyWay = milkyWay;
        }

        public String[] getPegasus() {
            return pegasus;
        }

        public void setPegasus(String[] pegasus) {
            this.pegasus = pegasus;
        }

        public String[] getUniverse() {
            return universe;
        }

        public void setUniverse(String[] universe) {
            this.universe = universe;
        }
        // endregion
    }

    public record Symbols(int size, String file) {
        public float getTextureOffset(int symbol) {
            symbol -= 1;
            float symbolSize = 1F / size;
            return symbolSize * symbol + symbolSize / 2F;
        }
    }

    public AllowedVariants getVariants() {
        return variants;
    }

    public void setVariants(AllowedVariants variants) {
        this.variants = variants;
    }

    public Map<String, Symbols> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Symbols> symbols) {
        this.symbols = symbols;
    }

    @Nullable
    public String getBackground() {
        return background;
    }

    public void setBackground(@Nullable String background) {
        this.background = background;
    }
}
