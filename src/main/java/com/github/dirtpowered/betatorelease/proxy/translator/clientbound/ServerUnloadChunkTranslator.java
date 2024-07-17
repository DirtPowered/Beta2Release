package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PreChunkPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import org.pmw.tinylog.Logger;

public class ServerUnloadChunkTranslator implements ModernToBetaHandler<ServerUnloadChunkPacket> {

    @Override
    public void translate(ServerUnloadChunkPacket packet, Session betaSession) {
        int xPosition = packet.getX();
        int zPosition = packet.getZ();
        if (betaSession.getServer().isDebugMode()) {
            Logger.info("Chunk (to unload) position x:{} z:{}", xPosition, zPosition);
        }
        betaSession.sendPacket(new PreChunkPacketData(xPosition, zPosition, false /* unload chunk */));
    }
}
