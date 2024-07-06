package com.github.dirtpowered.betatorelease.proxy.translator.registry;

import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.packetlib.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public class TranslatorRegistry {
    private final Map<Class<? extends Packet>, ModernToBetaHandler> translators = new HashMap<>();

    public void registerTranslator(final Class<? extends Packet> packet, ModernToBetaHandler translator) {
        translators.put(packet, translator);
    }

    public ModernToBetaHandler getByPacket(final Packet packet) {
        return translators.get(packet.getClass());
    }
}