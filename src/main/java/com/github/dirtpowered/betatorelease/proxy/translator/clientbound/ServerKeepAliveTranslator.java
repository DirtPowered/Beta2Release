package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3KeepAlivePacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;

public class ServerKeepAliveTranslator implements ModernToBetaHandler<ServerKeepAlivePacket> {

    @Override
    public void translate(ServerKeepAlivePacket packet, Session betaSession) {
        betaSession.sendPacket(new V1_7_3KeepAlivePacketData());
    }
}