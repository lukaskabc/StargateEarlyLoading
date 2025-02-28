package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.original;

/**
 * <a href="https://github.com/Povstalec/StargateJourney/blob/822542dfa3b3f8cd59befd177da4ddc6a6227d29/src/main/java/net/povstalec/sgjourney/common/misc/CoordinateHelper.java#L11">Original source</a>
 *
 * @author <a href="https://github.com/Povstalec/">Povstalec</a>
 */
public class SGJourneyCoordinateHelper {
    public static class CoordinateSystems {
        public static float cartesianToPolarR(float x, float y) {
            return (float) Math.sqrt(x * x + y * y);
        }

        public static float cartesianToPolarPhi(float x, float y) {
            return (float) Math.toDegrees(Math.atan2(y, x));
        }

        public static float polarToCartesianX(float r, float phi) {
            return r * (float) Math.cos(Math.toRadians(phi));
        }

        public static float polarToCartesianY(float r, float phi) {
            return r * (float) Math.sin(Math.toRadians(phi));
        }

        private CoordinateSystems() {
            throw new AssertionError();
        }
    }

    private SGJourneyCoordinateHelper() {
        throw new AssertionError();
    }
}
