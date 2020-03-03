package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.EntityVelocityPacketData;
import com.github.dirtpowered.betatorelease.Utils.Utils;
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

        betaSession.sendPacket(new EntityVelocityPacketData(entityId, motionX, motionY, motionZ));
    }
}
