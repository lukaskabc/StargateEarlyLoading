package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import net.neoforged.fml.earlydisplay.PerformanceInfo;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class RefPerformanceInfo extends ReflectionAccessor {
    private static final MethodHandles.Lookup lookup = privateLookup(PerformanceInfo.class);
    private static final VarHandle text = findField(lookup, "text", String.class);
    private static final VarHandle memory = findField(lookup, "memory", float.class);

    public RefPerformanceInfo() {
        // this null is safe because we are not using any method that would use the target
        // this is not great... but...
        super(null, PerformanceInfo.class);
    }

    public String text(PerformanceInfo target) {
        return (String) text.get(target);
    }

    public float memory(PerformanceInfo target) {
        return (float) memory.get(target);
    }
}
