package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;

/**
 * Structure for JSON binding representing stargate variant
 */
@NullUnmarked
public class StargateVariant {

    private StargateType type;
    private String texture;
    private String engagedTexture;
    @NonNull
    private VariantSymbols symbols = new VariantSymbols();
    @NonNull
    private VariantModel stargateModel = new VariantModel();
    private String dialingStrategy;

    // region <Getters & Setters>

    public StargateType getType() {
        return type;
    }

    public void setType(StargateType type) {
        this.type = type;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getEngagedTexture() {
        return engagedTexture;
    }

    public void setEngagedTexture(String engagedTexture) {
        this.engagedTexture = engagedTexture;
    }

    @NonNull
    public VariantSymbols getSymbols() {
        return symbols;
    }

    public void setSymbols(@NonNull VariantSymbols symbols) {
        this.symbols = symbols;
    }

    @NonNull
    public VariantModel getStargateModel() {
        return stargateModel;
    }

    public void setStargateModel(@NonNull VariantModel stargateModel) {
        this.stargateModel = stargateModel;
    }

    public void engageChevron(int chevron, @NonNull GenericStargate genericStargate) {
        genericStargate.setChevronEngaged(chevron, true);
    }

    public void raiseChevron(int chevron, @NonNull GenericStargate genericStargate) {
        if (!getStargateModel().isMovieChevronLocking() && !getStargateModel().isMoviePrimaryChevron()) {
            chevron = 0;
            genericStargate.setChevronEngaged(chevron, true);
        }

        genericStargate.doRaiseChevron(chevron);
    }

    public void lowerChevron(int chevron, @NonNull GenericStargate genericStargate) {
        if (!getStargateModel().isMovieChevronLocking() && !getStargateModel().isMoviePrimaryChevron() && chevron != 0) {
            chevron = 0;
            genericStargate.setChevronEngaged(chevron, false);
        }
        genericStargate.doLowerChevron(chevron);
    }

    public String getDialingStrategy() {
        return dialingStrategy;
    }

    public void setDialingStrategy(String dialingStrategy) {
        this.dialingStrategy = dialingStrategy;
    }

    // endregion
}
