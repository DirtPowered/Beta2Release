package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import org.pmw.tinylog.Logger;

public class ServerEntityHeadLookTranslator implements ModernToBetaHandler<ServerEntityHeadLookPacket> {

    @Override
    public void translate(ServerEntityHeadLookPacket packet, Session betaSession) {

        //TODO: Finish it
        int entityId = packet.getEntityId();
        byte yaw = (byte) packet.getHeadYaw();

        if (betaSession.getServer().isDebugMode()) {
            Logger.info("entityId: {}, yaw: {}", entityId, yaw);
        }

        //betaSession.sendPacket(new EntityLookPacketData(entityId, yaw, (byte) 0));
    }
}
