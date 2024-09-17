package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PreChunkPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;

public class ServerUnloadChunkTranslator implements ModernToBetaHandler<ServerUnloadChunkPacket> {

    @Override
    public void translate(ServerUnloadChunkPacket packet, Session betaSession) {
        int xPosition = packet.getX();
        int zPosition = packet.getZ();

        betaSession.getBlockStorage().removeChunk(xPosition, zPosition);
        betaSession.sendPacket(new V1_7_3PreChunkPacketData(xPosition, zPosition, false /* unload chunk */));
    }
}