package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityTeleportPacketData;
import com.github.dirtpowered.betatorelease.data.entity.EntityVehicle;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;

public class ServerEntityTeleportTranslator implements ModernToBetaHandler<ServerEntityTeleportPacket> {

    @Override
    public void translate(ServerEntityTeleportPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();

        Entity entity = betaSession.getEntityCache().getEntityById(entityId);

        int x = Utils.toAbsolutePos(packet.getX());
        int z = Utils.toAbsolutePos(packet.getZ());

        double y = packet.getY();

        int yaw = Utils.toAbsoluteRotation(packet.getYaw());
        int pitch = Utils.toAbsoluteRotation(packet.getPitch());

        if (entity instanceof EntityVehicle vehicle) {
            double yOffset = vehicle.isBoat() ? 0.66D : 0.32D;
            // apply offset, so the vehicle is not sinking into the ground
            y += yOffset;
        }
        betaSession.sendPacket(new V1_7_3EntityTeleportPacketData(entityId, x, Utils.toAbsolutePos(y), z, yaw, pitch));
    }
}