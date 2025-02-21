package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant;

import com.google.gson.annotations.SerializedName;

public enum StargateType {
    @SerializedName("milky_way")
    MILKY_WAY,
    @SerializedName("pegasus")
    PEGASUS,
    @SerializedName("universe")
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
