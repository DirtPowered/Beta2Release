package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.EntityActionPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import org.pmw.tinylog.Logger;

public class EntityActionPacketHandler implements BetaToModernHandler<EntityActionPacketData> {

    @Override
    public void handlePacket(Session session, EntityActionPacketData packetClass) {
        int entityId = packetClass.getEntityId();
        int state = packetClass.getState();
        PlayerState playerState;

        switch (state) {
            case 1:
                playerState = PlayerState.START_SNEAKING;
                break;
            case 2:
                playerState = PlayerState.STOP_SNEAKING;
                break;
            case 3:
                playerState = PlayerState.LEAVE_BED;
                break;
            default:
                playerState = null;
                break;
        }

        if (playerState == null)
            return;

        if (session.getServer().isDebugMode()) {
            Logger.info("entityId: {}, state: {}", entityId, state);
        }
        session.getModernClient().sendModernPacket(new ClientPlayerStatePacket(entityId, playerState));
    }
}
