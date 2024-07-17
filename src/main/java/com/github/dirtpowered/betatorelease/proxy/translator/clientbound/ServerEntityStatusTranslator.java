package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityStatusPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.EntityStatus;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;

public class ServerEntityStatusTranslator implements ModernToBetaHandler<ServerEntityStatusPacket> {

    @Override
    public void translate(ServerEntityStatusPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        EntityStatus status = packet.getStatus();
        /*
         * 2 Entity hurt
         * 3 Entity dead
         * 6 Wolf taming
         * 7 Wolf tamed
         * 8 Wolf shaking water off itself
         */
        int statusId = switch (status) {
            case LIVING_HURT -> 2;
            case LIVING_DEATH -> 3;
            case TAMEABLE_TAMING_FAILED -> 6;
            case TAMEABLE_TAMING_SUCCEEDED -> 7;
            case WOLF_SHAKE_WATER -> 8;
            default -> -1;
        };

        if (statusId == -1)
            return;

        betaSession.sendPacket(new EntityStatusPacketData(entityId, statusId));
    }
}