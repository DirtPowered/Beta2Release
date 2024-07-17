package com.github.dirtpowered.betatorelease.network.registry;

import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;

import java.util.HashMap;
import java.util.Map;

public class MessageHandlerRegistry {
    private final Map<Class<? extends Packet<?>>, BetaToModernHandler<?>> handlers = new HashMap<>();

    public void registerHandler(final Class<? extends Packet<?>> cls, final BetaToModernHandler<?> handler) {
        this.handlers.put(cls, handler);
    }

    public BetaToModernHandler<?> getMessageHandler(final Packet<?> msg) {
        return handlers.get(msg.getClass());
    }
}