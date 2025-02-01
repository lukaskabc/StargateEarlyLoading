package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleBufferBuilder;

public record ContextSimpleBuffer(SimpleBufferBuilder simpleBufferBuilder, RenderElement.DisplayContext context) {
}
