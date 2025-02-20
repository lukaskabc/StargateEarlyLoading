package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.reflection;

import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.dialing.*;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.elements.stargate.GenericStargate;
import cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.exception.InitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.stream.Collectors;

public class RefDialingStrategy extends ReflectionAccessor {
    private static final List<Class<? extends DialingStrategy>> DIALING_STRATEGIES = List.of(
            MilkyWay2Step.class,
            MilkyWay3Step.class,
            PegasusLoading.class,
            PegasusLoop.class,
            UniverseLoop.class
    );
    private static final Logger LOG = LogManager.getLogger();

    private RefDialingStrategy(Object target) {
        super(target);
        throw new AssertionError();
    }

    public static Class<? extends DialingStrategy> resolveDialingStrategy(String dialingStrategyName) {
        final String lowerName = dialingStrategyName.toLowerCase();
        return DIALING_STRATEGIES.stream()
                .filter(clazz -> clazz.getSimpleName().toLowerCase().equals(lowerName))
                .findAny().orElseThrow(() -> {
                    LOG.error("Invalid configuration! Dialing strategy '{}' not found! Options are: {}", () -> dialingStrategyName, () -> DIALING_STRATEGIES.stream().map(Class::getSimpleName).collect(Collectors.joining(", ")));
                    return new InitializationException("Invalid dialing strategy");
                });
    }

    public static DialingStrategy instance(String strategyName, GenericStargate stargate, List<Integer> chevronOrder) {
        return instance(resolveDialingStrategy(strategyName), stargate, chevronOrder);
    }

    public static <T extends DialingStrategy> T instance(Class<T> clazz, GenericStargate stargate, List<Integer> chevronOrder) {
        try {
            return (T) MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, GenericStargate.class, List.class))
                    .invoke(stargate, chevronOrder);
        } catch (Throwable e) {
            throw new ReflectionException(e);
        }
    }
}
