package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.AnimationPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;

public class AnimationPacketHandler implements BetaToModernHandler<AnimationPacketData> {

    @Override
    public void handlePacket(Session session, AnimationPacketData packetClass) {
        //TODO: Bed wakeup animation
        int type = packetClass.getAnimate();
        int entityId = packetClass.getEntityId();

        if (type == 1) {
            session.getModernClient().sendModernPacket(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND));
        } else if (type == 3) {
            session.getModernClient().sendModernPacket(new ClientPlayerStatePacket(entityId, PlayerState.LEAVE_BED));
        }
    }
}
