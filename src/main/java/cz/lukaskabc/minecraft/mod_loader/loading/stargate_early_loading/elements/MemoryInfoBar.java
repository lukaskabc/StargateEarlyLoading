package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefPerformanceInfo;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.RefRenderElement;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.utils.ContextSimpleBuffer;
import net.neoforged.fml.earlydisplay.ColourScheme;
import net.neoforged.fml.earlydisplay.PerformanceInfo;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.fml.earlydisplay.SimpleFont;

import java.util.function.Supplier;

import static cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow.getGlobalAlpha;
import static net.neoforged.fml.earlydisplay.RenderElement.hsvToRGB;

public class MemoryInfoBar extends ProgressBar implements Supplier<RenderElement> {
    private final RefPerformanceInfo performanceInfo = new RefPerformanceInfo();

    public MemoryInfoBar(SimpleFont font) {
        super(font);
    }

    @Override
    public RenderElement get() {
        return RefRenderElement.constructor(this::renderMemoryInfo);
    }

    private void renderMemoryInfo(final ContextSimpleBuffer bb, final int frameNumber) {
        final int y = bb.context().scaledHeight() - BAR_HEIGHT - 64;
        PerformanceInfo pi = bb.context().performance();
        final float memory = performanceInfo.memory(pi);
        final String text = performanceInfo.text(pi);
        final int colour = hsvToRGB((1.0f - (float) Math.pow(memory, 1.5f)) / 3f, 1.0f, 0.5f);
        progressBar(ctx -> new int[]{(ctx.scaledWidth() - BAR_WIDTH * ctx.scale()) / 2, y, BAR_WIDTH * ctx.scale()}, f -> colour, f -> new float[]{0f, memory})
                .accept(bb, frameNumber);
        final int width = font.stringWidth(text);
        renderText(text(bb.context().scaledWidth() / 2 - width / 2, y + BAR_HEIGHT, text, ColourScheme.BLACK.foreground().packedint(getGlobalAlpha())), bb);
    }
}
