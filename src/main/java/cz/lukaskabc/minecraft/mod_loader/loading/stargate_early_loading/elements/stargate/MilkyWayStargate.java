package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate;

import net.neoforged.fml.earlydisplay.RenderElement;

import java.util.List;

public class MilkyWayStargate extends GenericStargate {

    public MilkyWayStargate() {
        super((short) 39);
    }


    public List<RenderElement> create() {
        engageChevron(1);
        engageChevron(3);
        engageChevron(5);
        raiseChevron(0);
        engageChevron(0);
        raiseChevron(5);
        return super.create("milky_way_stargate", 2608);
        // okey this is some problem
        // we have a second texture containing only pixels for active chevron
        // so we will need a second renderer that will render only active chevrons....
    }


    @Override
    protected int getTextureId() {
        return 2;
    }

    @Override
    protected int getEngagedTextureId() {
        return 3;
    }
}
