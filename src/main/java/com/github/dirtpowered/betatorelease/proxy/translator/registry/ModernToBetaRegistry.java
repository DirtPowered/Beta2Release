package com.github.dirtpowered.betatorelease.proxy.translator.registry;

import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.packetlib.packet.Packet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModernToBetaRegistry {
    private final Map<Class<? extends Packet>, ModernToBetaHandler<?>> translators = new ConcurrentHashMap<>();

    public void registerTranslator(final Class<? extends Packet> packet, ModernToBetaHandler<?> translator) {
        this.translators.put(packet, translator);
    }

    public ModernToBetaHandler<?> getByPacket(final Packet packet) {
        return translators.get(packet.getClass());
    }
}