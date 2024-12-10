package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection.ReflectionAccessor;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.neoforgespi.earlywindow.ImmediateWindowProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StargateEarlyLoadingWindow extends DisplayWindow implements ImmediateWindowProvider {
    private static final Logger LOG = LogManager.getLogger();
    @Override
    public String name() {
        return "StargateEarlyLoading";
    }

    @Override
    public Runnable initialize(String[] arguments) {
        LOG.error("StargateEarlyLoadingWindow initialize");
        return super.initialize(arguments);
    }

    @Override
    public void updateModuleReads(final ModuleLayer layer) {
        var fm = layer.findModule("neoforge").orElseThrow();
        getClass().getModule().addReads(fm);
        var clz = Class.forName(fm, "net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay");
        var methods = Arrays.stream(clz.getMethods()).filter(m -> Modifier.isStatic(m.getModifiers())).collect(Collectors.toMap(Method::getName, Function.identity()));
        new ReflectionAccessor(this).setFieldValue("loadingOverlay", methods.get("newInstance"));
    }
}
