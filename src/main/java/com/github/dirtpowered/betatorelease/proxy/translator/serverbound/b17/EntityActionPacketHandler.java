package com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityActionPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;

public class EntityActionPacketHandler implements BetaToModernHandler<EntityActionPacketData> {

    @Override
    public void handlePacket(Session session, EntityActionPacketData packetClass) {
        int state = packetClass.getState();

        PlayerState playerState = switch (state) {
            case 1 -> PlayerState.START_SNEAKING;
            case 2 -> PlayerState.STOP_SNEAKING;
            case 3 -> PlayerState.LEAVE_BED;
            default -> null;
        };

        if (playerState == null)
            return;

        int entityId = packetClass.getEntityId();
        session.getModernClient().sendModernPacket(new ClientPlayerStatePacket(entityId, playerState));
    }
}