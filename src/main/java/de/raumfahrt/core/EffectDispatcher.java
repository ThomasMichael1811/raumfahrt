package de.raumfahrt.core;

import java.util.HashMap;
import java.util.Map;

public final class EffectDispatcher {

    private final Map<Integer, Runnable> effects = new HashMap<>();

    public void register(int key, Runnable effect) {
        effects.put(key, effect);
    }

    public boolean trigger(int key) {
        Runnable effect = effects.get(key);
        if (effect == null) {
            return false;
        }
        effect.run();
        return true;
    }
}
