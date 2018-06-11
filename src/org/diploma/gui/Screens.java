package org.diploma.gui;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Screens {

    private static final Map<Class<?>, Object> screens = new ConcurrentHashMap<>();

    private Screens() {}

    @SuppressWarnings("unchecked")
    static <T> T getScreen(final Class<T> screen, final Supplier<T> factory) {
        return (T) screens.compute(screen, (k, v) -> {
            if (v != null) {
                if (v instanceof HasDependency) {
                    ((HasDependency) v).redraw();
                }
                return v;
            }
            return factory.get();
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getScreen(final Class<T> screen) {
        return Optional.ofNullable(screens.get(screen)).map(o -> (T) o);
    }

    public enum Screen {
        AlternativesAndCriterias,
        Ratios,
        Results,
    }
}
