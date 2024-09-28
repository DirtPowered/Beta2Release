package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3RespawnPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;

public class ServerRespawnTranslator implements ModernToBetaHandler<ServerRespawnPacket> {

    @Override
    public void translate(ServerRespawnPacket packet, Session betaSession) {
        int dimension = packet.getDimension();

        // remap the end dimension to the nether
        if (dimension == 1) dimension = -1;

        betaSession.getBetaPlayer().setDimension(dimension);
        betaSession.sendPacket(new V1_7_3RespawnPacketData((byte) dimension));
    }
}