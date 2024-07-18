package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityPositionPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;

public class ServerEntityPositionTranslator implements ModernToBetaHandler<ServerEntityPositionPacket> {

    @Override
    public void translate(ServerEntityPositionPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        int x = Utils.toAbsolutePos(packet.getMovementX());
        int y = Utils.toAbsolutePos(packet.getMovementY());
        int z = Utils.toAbsolutePos(packet.getMovementZ());

        //Entity Relative Move (0x1F)
        betaSession.sendPacket(new EntityPositionPacketData(entityId, x, y, z));
    }
}