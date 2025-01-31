package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.variant;

public enum StargateType {
    MILKY_WAY,
    PEGASUS,
    UNIVERSE;

    public static StargateType fromName(String name) throws IllegalArgumentException {
        for (StargateType type : StargateType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown stargate type: " + name);
    }
}
