package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate;

import net.neoforged.fml.earlydisplay.RenderElement;

import java.util.List;

public class MilkyWayStargate extends GenericStargate {

    public MilkyWayStargate() {
        super((short) 39);
    }


    public List<RenderElement> create() {
        return super.create("milky_way_stargate", 2608);
    }
}
