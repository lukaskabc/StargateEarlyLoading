package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

public class Config {
    private AllowedVariants allowedVariants;

    public static class AllowedVariants {
        private String[] milkyWay;
        private String[] pegasus;
        private String[] Universe;
    }

    public AllowedVariants getAllowedVariants() {
        return allowedVariants;
    }

    public void setAllowedVariants(AllowedVariants allowedVariants) {
        this.allowedVariants = allowedVariants;
    }
}
