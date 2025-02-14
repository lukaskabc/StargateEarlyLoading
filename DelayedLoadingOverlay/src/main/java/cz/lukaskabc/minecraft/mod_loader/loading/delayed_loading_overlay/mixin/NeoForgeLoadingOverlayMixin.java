package cz.lukaskabc.minecraft.mod_loader.loading.delayed_loading_overlay.mixin;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.StargateEarlyLoadingWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(NeoForgeLoadingOverlay.class)
public abstract class NeoForgeLoadingOverlayMixin extends LoadingOverlay {
    @Nullable
    @Unique
    private StargateEarlyLoadingWindow stargateWindow;
    @Shadow
    private long fadeOutStart;

    public NeoForgeLoadingOverlayMixin(Minecraft minecraft, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn) {
        super(minecraft, reload, onFinish, fadeIn);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void init(Minecraft mc, ReloadInstance reloader, Consumer<?> errorConsumer, DisplayWindow displayWindow, CallbackInfo ci) {
        if (displayWindow instanceof StargateEarlyLoadingWindow stargateEarlyLoadingWindow) {
            stargateWindow = stargateEarlyLoadingWindow;
            fadeOutStart = -2L;
        } else {
            stargateWindow = null;
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (stargateWindow == null) return; // how the hell this happened?

        if (stargateWindow.loadingAnimationFinished() && fadeOutStart < 0) {
            fadeOutStart = -1L;
        }
    }
}
