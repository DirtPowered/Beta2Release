package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityActionPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;

public class EntityActionPacketHandler implements BetaToModernHandler<V1_7_3EntityActionPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3EntityActionPacketData packetClass) {
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