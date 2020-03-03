package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.AnimationPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;

public class ServerEntityAnimationTranslator implements ModernToBetaHandler<ServerEntityAnimationPacket> {

    @Override
    public void translate(ServerEntityAnimationPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        int status;
        switch (packet.getAnimation()) {
            case SWING_ARM:
                status = 1;
                break;
            case DAMAGE:
                status = 2;
                break;
            case LEAVE_BED:
                status = 3;
                break;
            default:
                status = 0; /* no animation */
                break;
        }

        betaSession.sendPacket(new AnimationPacketData(entityId, status));
    }
}
