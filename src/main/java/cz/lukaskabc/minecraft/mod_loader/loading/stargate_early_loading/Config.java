package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing.MilkyWay2Step;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing.PegasusLoop;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing.UniverseLoop;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant.StargateType;

import java.util.List;
import java.util.Map;

public class Config {
    private boolean crashAfterLoad = false;
    private AllowedVariants variants = new AllowedVariants();
    private Map<String, Symbols> symbols = Map.of();
    private String[] backgrounds = new String[0];
    private List<Integer> chevronOrder = List.of(1, 2, 3, 4, 5, 6, 7, 8);

    private Map<StargateType, String> defaultDialingStrategies = Map.of(
            StargateType.MILKY_WAY, MilkyWay2Step.class.getSimpleName(),
            StargateType.PEGASUS, PegasusLoop.class.getSimpleName(),
            StargateType.UNIVERSE, UniverseLoop.class.getSimpleName()
    );

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

    // region <Getters & Setters>
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

    public String[] getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(String[] backgrounds) {
        this.backgrounds = backgrounds;
    }

    public List<Integer> getChevronOrder() {
        return chevronOrder;
    }

    public void setChevronOrder(List<Integer> chevronOrder) {
        this.chevronOrder = chevronOrder;
    }

    public Map<StargateType, String> getDefaultDialingStrategies() {
        return defaultDialingStrategies;
    }

    public void setDefaultDialingStrategies(Map<StargateType, String> defaultDialingStrategies) {
        this.defaultDialingStrategies = defaultDialingStrategies;
    }

    public boolean doCrashAfterLoad() {
        return crashAfterLoad;
    }

    public void setCrashAfterLoad(boolean crashAfterLoad) {
        this.crashAfterLoad = crashAfterLoad;
    }
    // endregion
}
