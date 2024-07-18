package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityDestroyPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;

public class ServerEntityDestroyTranslator implements ModernToBetaHandler<ServerEntityDestroyPacket> {

    @Override
    public void translate(ServerEntityDestroyPacket packet, Session betaSession) {
        for (int entityId : packet.getEntityIds()) {
            betaSession.sendPacket(new EntityDestroyPacketData(entityId));
            betaSession.getEntityCache().removeEntity(entityId);
        }
    }
}