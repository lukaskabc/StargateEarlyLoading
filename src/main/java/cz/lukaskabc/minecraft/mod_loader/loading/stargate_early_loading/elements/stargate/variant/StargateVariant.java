package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;

/**
 * Structure for JSON binding representing stargate variant
 */
@NullUnmarked
public class StargateVariant {

    public enum Type {
        MILKY_WAY,
        PEGASUS,
        UNIVERSE
    }

    private Type type;
    private String texture;
    private String engagedTexture;
    @NonNull
    private VariantSymbols symbols = new VariantSymbols();
    @NonNull
    private VariantModel stargateModel = new VariantModel();

    // region <Getters & Setters>

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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
    // endregion
}
