package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception;

public class InitializationException extends RuntimeException {
    public InitializationException(Throwable cause) {
        super(cause);
    }

    public InitializationException(String message) {
        super(message);
    }
}
