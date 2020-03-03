package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;

public class ServerKeepAliveTranslator implements ModernToBetaHandler<ServerKeepAlivePacket> {

    @Override
    public void translate(ServerKeepAlivePacket packet, Session betaSession) {
        betaSession.sendKeepAlive();
    }
}
