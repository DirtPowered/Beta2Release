package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityLookPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;

public class ServerEntityRotationTranslator implements ModernToBetaHandler<ServerEntityRotationPacket> {

    @Override
    public void translate(ServerEntityRotationPacket packet, Session betaSession) {
        byte yaw = (byte) Utils.toAbsoluteRotation(packet.getYaw());
        byte pitch = (byte) Utils.toAbsoluteRotation(packet.getPitch());

        betaSession.sendPacket(new EntityLookPacketData(packet.getEntityId(), yaw, pitch));
    }
}