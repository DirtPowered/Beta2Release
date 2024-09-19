package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityLookPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;

public class ServerEntityRotationTranslator implements ModernToBetaHandler<ServerEntityRotationPacket> {

    @Override
    public void translate(ServerEntityRotationPacket packet, Session betaSession) {
        int yaw = Utils.toAbsoluteRotation(packet.getYaw());
        int pitch = Utils.toAbsoluteRotation(packet.getPitch());

        betaSession.sendPacket(new V1_7_3EntityLookPacketData(packet.getEntityId(), yaw, pitch));
    }
}