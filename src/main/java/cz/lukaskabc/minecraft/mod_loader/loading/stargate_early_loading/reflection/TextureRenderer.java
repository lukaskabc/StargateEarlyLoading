package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;

public interface TextureRenderer {
    void accept(ContextSimpleBuffer contextSimpleBuffer, int frame);
}
