package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betatorelease.data.lang.LangStorage;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;

public class ServerDisconnectTranslator implements ModernToBetaHandler<ServerDisconnectPacket> {

    @Override
    public void translate(ServerDisconnectPacket packet, Session betaSession) {
        String legacy = LangStorage.translate(packet.getReason().toJsonString(), false);
        // the beta client is capable of displaying only up to 100 characters
        if (legacy.length() > 100)
            legacy = legacy.substring(0, 100);

        betaSession.disconnect(legacy);
    }
}