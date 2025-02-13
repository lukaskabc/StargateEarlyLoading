package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import net.neoforged.fml.earlydisplay.SimpleFont;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class RefSimpleFont extends ReflectionAccessor {
    private static final MethodHandles.Lookup lookup = privateLookup(SimpleFont.class);
    private static final VarHandle lineSpacing = findField(lookup, "lineSpacing", int.class);
    private static final VarHandle textureNumber = findField(lookup, "textureNumber", int.class);
    private static final VarHandle descent = findField(lookup, "descent", int.class);

    public RefSimpleFont(SimpleFont target) {
        super(target);
    }

    public int lineSpacing() {
        return (int) lineSpacing.get(target);
    }

    public int textureNumber() {
        return (int) textureNumber.get(target);
    }

    public int descent() {
        return (int) descent.get(target);
    }
}
