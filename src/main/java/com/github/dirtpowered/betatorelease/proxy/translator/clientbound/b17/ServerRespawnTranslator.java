package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.RespawnPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;

public class ServerRespawnTranslator implements ModernToBetaHandler<ServerRespawnPacket> {

    @Override
    public void translate(ServerRespawnPacket packet, Session betaSession) {
        int dimension = packet.getDimension();

        betaSession.getBetaPlayer().setDimension(dimension);
        betaSession.sendPacket(new RespawnPacketData((byte) dimension));
    }
}