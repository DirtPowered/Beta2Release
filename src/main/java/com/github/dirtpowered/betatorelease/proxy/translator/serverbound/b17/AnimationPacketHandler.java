package com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.AnimationPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;

public class AnimationPacketHandler implements BetaToModernHandler<AnimationPacketData> {

    @Override
    public void handlePacket(Session session, AnimationPacketData packetClass) {
        int type = packetClass.getAnimate();

        if (type == 1) {
            session.getModernClient().sendModernPacket(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND));
        } else if (type == 3) {
            session.getModernClient().sendModernPacket(new ClientPlayerStatePacket(packetClass.getEntityId(), PlayerState.LEAVE_BED));
        }
    }
}