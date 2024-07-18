package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;

public class ServerEntityHeadLookTranslator implements ModernToBetaHandler<ServerEntityHeadLookPacket> {

    @Override
    public void translate(ServerEntityHeadLookPacket packet, Session betaSession) {
        // skip for now, causes issues
    }
}