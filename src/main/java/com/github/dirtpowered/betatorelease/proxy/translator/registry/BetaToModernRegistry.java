package com.github.dirtpowered.betatorelease.proxy.translator.registry;

import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;

import java.util.HashMap;
import java.util.Map;

public class BetaToModernRegistry {
    private final Map<Class<? extends Packet<?>>, BetaToModernHandler<?>> translators = new HashMap<>();

    public void registerTranslator(final Class<? extends Packet<?>> cls, final BetaToModernHandler<?> handler) {
        this.translators.put(cls, handler);
    }

    public BetaToModernHandler<?> getMessageHandler(final Packet<?> msg) {
        return translators.get(msg.getClass());
    }
}