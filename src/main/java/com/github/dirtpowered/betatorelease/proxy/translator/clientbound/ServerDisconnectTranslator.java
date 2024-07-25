package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;

public class ServerDisconnectTranslator implements ModernToBetaHandler<ServerDisconnectPacket> {

    @Override
    public void translate(ServerDisconnectPacket packet, Session betaSession) {
        String reason = packet.getReason().getFullText();

        // the beta client is capable of displaying only up to 100 characters
        if (reason.length() > 100)
            reason = reason.substring(0, 100);

        betaSession.disconnect(reason);
    }
}