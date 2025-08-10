package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils;

import net.minecraftforge.fml.earlydisplay.RenderElement;
import net.minecraftforge.fml.earlydisplay.SimpleBufferBuilder;

public record ContextSimpleBuffer(SimpleBufferBuilder simpleBufferBuilder, RenderElement.DisplayContext context) {
}
