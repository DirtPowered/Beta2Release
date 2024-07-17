package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UseEntityPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;

public class UseEntityPacketHandler implements BetaToModernHandler<UseEntityPacketData> {

    @Override
    public void handlePacket(Session session, UseEntityPacketData packetClass) {
        int target = packetClass.getTargetEntityId();
        boolean leftClick = packetClass.isLeftClick();

        session.getModernClient().sendModernPacket(new ClientPlayerInteractEntityPacket(target, leftClick ? InteractAction.ATTACK : InteractAction.INTERACT_AT));
    }
}