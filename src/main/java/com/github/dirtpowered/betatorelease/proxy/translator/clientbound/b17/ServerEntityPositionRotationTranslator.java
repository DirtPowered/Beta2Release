package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityMoveLookPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;

public class ServerEntityPositionRotationTranslator implements ModernToBetaHandler<ServerEntityPositionRotationPacket> {

    @Override
    public void translate(ServerEntityPositionRotationPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        int x = Utils.toAbsolutePos(packet.getMovementX());
        int y = Utils.toAbsolutePos(packet.getMovementY());
        int z = Utils.toAbsolutePos(packet.getMovementZ());

        byte yaw = (byte) Utils.toAbsoluteRotation(packet.getYaw());
        byte pitch = (byte) Utils.toAbsoluteRotation(packet.getPitch());

        betaSession.sendPacket(new EntityMoveLookPacketData(entityId, x, y, z, yaw, pitch));
    }
}