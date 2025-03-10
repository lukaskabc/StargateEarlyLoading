package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class VariantModel {
    private boolean movieChevronLocking = false;
    private boolean moviePrimaryChevron = false;

    public boolean isMovieChevronLocking() {
        return movieChevronLocking;
    }

    public void setMovieChevronLocking(boolean movieChevronLocking) {
        this.movieChevronLocking = movieChevronLocking;
    }

    public boolean isMoviePrimaryChevron() {
        return moviePrimaryChevron;
    }

    public void setMoviePrimaryChevron(boolean moviePrimaryChevron) {
        this.moviePrimaryChevron = moviePrimaryChevron;
    }
}
