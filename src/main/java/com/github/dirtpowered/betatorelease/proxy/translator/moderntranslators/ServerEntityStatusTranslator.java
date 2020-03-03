package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.EntityStatusPacketData;
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
         * 2 	Entity hurt
         * 3 	Entity dead
         * 6 	Wolf taming
         * 7 	Wolf tamed
         * 8 	Wolf shaking water off itself
         */
        int statusId;

        switch (status) {
            case LIVING_HURT:
                statusId = 2;
                break;
            case LIVING_DEATH:
                statusId = 3;
                break;
            case TAMEABLE_TAMING_FAILED:
                statusId = 6;
                break;
            case TAMEABLE_TAMING_SUCCEEDED:
                statusId = 7;
                break;
            case WOLF_SHAKE_WATER:
                statusId = 8;
                break;
            default:
                statusId = -1;
                break;
        }

        if (statusId == -1)
            return;

        betaSession.sendPacket(new EntityStatusPacketData(entityId, statusId));
    }
}
