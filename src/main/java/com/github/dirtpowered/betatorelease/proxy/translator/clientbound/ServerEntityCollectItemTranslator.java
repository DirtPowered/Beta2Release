package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3CollectPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;

public class ServerEntityCollectItemTranslator implements ModernToBetaHandler<ServerEntityCollectItemPacket> {

    @Override
    public void translate(ServerEntityCollectItemPacket packet, Session betaSession) {
        int entityId = packet.getCollectedEntityId();
        int targetEntityId = packet.getCollectorEntityId();
        betaSession.sendPacket(new V1_7_3CollectPacketData(entityId, targetEntityId));
    }
}