package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Structure for JSON binding representing stargate variant
 */
public class StargateVariant {
    public StargateVariant(Type type) {
        this.type = type;
    }

    public enum Type {
        MILKY_WAY,
        PEGASUS,
        UNIVERSE
    }

    @JsonIgnore
    private final Type type;
    private String texture;
    private String engagedTexture;
    private VariantSymbols symbols;
    private VariantModel stargateModel;

    public Type getType() {
        return type;
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

    public VariantSymbols getSymbols() {
        return symbols;
    }

    public void setSymbols(VariantSymbols symbols) {
        this.symbols = symbols;
    }

    public VariantModel getStargateModel() {
        return stargateModel;
    }

    public void setStargateModel(VariantModel stargateModel) {
        this.stargateModel = stargateModel;
    }
}
