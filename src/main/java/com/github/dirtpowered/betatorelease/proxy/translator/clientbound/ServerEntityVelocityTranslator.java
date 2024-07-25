package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityVelocityPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;

public class ServerEntityVelocityTranslator implements ModernToBetaHandler<ServerEntityVelocityPacket> {

    @Override
    public void translate(ServerEntityVelocityPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();

        int motionX = Utils.toBetaVelocity(packet.getMotionX());
        int motionY = Utils.toBetaVelocity(packet.getMotionY());
        int motionZ = Utils.toBetaVelocity(packet.getMotionZ());

        betaSession.sendPacket(new V1_7_3EntityVelocityPacketData(entityId, motionX, motionY, motionZ));
    }
}
