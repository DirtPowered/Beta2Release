package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3AnimationPacketData;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;

public class ServerEntityAnimationTranslator implements ModernToBetaHandler<ServerEntityAnimationPacket> {

    @Override
    public void translate(ServerEntityAnimationPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        Entity entity = betaSession.getEntityCache().getEntityById(entityId);

        if (entity != null)
            return; // this means that it's not human, so we don't need to send an animation packet

        int status = switch (packet.getAnimation()) {
            case SWING_ARM -> 1;
            case DAMAGE -> 2;
            case LEAVE_BED -> 3;
            default -> 0; /* no animation */
        };

        betaSession.sendPacket(new V1_7_3AnimationPacketData(entityId, status));
    }
}