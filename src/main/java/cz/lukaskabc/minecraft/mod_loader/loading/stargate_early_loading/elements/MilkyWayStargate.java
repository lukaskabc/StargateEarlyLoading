package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import net.neoforged.fml.earlydisplay.RenderElement;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.ASSETS_DIRECTORY;

public class MilkyWayStargate extends GenericStargate {

    public MilkyWayStargate() {
        super((short) 39);
    }

    public RenderElement create() {
        return RefRenderElement.createQuad(ASSETS_DIRECTORY + "/gates/milky_way_stargate.png", 2608, 2, this::render);
    }

}
