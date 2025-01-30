package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

public class Config {
    private AllowedVariants allowedVariants;

    public static class AllowedVariants {
        private String[] milkyWay = new String[0];
        private String[] pegasus = new String[0];
        private String[] Universe = new String[0];

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
            return Universe;
        }

        public void setUniverse(String[] universe) {
            Universe = universe;
        }
        // endregion
    }

    public AllowedVariants getAllowedVariants() {
        return allowedVariants;
    }

    public void setAllowedVariants(AllowedVariants allowedVariants) {
        this.allowedVariants = allowedVariants;
    }
}
