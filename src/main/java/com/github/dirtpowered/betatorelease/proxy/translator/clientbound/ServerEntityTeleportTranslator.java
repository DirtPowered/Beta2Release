package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityTeleportPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;

public class ServerEntityTeleportTranslator implements ModernToBetaHandler<ServerEntityTeleportPacket> {

    @Override
    public void translate(ServerEntityTeleportPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        int x = Utils.toAbsolutePos(packet.getX());
        int y = Utils.toAbsolutePos(packet.getY());
        int z = Utils.toAbsolutePos(packet.getZ());

        int yaw = Utils.toAbsoluteRotation(packet.getYaw());
        int pitch = Utils.toAbsoluteRotation(packet.getPitch());

        betaSession.sendPacket(new V1_7_3EntityTeleportPacketData(entityId, x, y, z, yaw, pitch));
    }
}