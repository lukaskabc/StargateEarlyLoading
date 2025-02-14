package cz.lukaskabc.minecraft.mod_loader.loading.delayed_loading_overlay;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(StargateEarlyLoadingMod.MODID)
public class StargateEarlyLoadingMod {
    public static final String MODID = "delayed_loading_overlay";

    public StargateEarlyLoadingMod(IEventBus modEventBus, ModContainer modContainer) {
        // nothing to do
    }
}
