package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original;

public class SGJourneyModel {

    public static float getSideWidth(int sides, float defaultDistance) {
        return 2 * defaultDistance * (float) Math.tan(Math.toRadians(180) / sides);
    }

    public static float getUsedWidth(int sides, float distanceFromCenter, float defaultDistance) {
        float sideWidth = getSideWidth(sides, defaultDistance);
        float ratio = distanceFromCenter / defaultDistance;

        return sideWidth * ratio;
    }
}
