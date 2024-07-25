package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3FlyingPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;

public class FlyingPacketHandler implements BetaToModernHandler<V1_7_3FlyingPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3FlyingPacketData packetClass) {
        BetaPlayer betaPlayer = session.getBetaPlayer();
        betaPlayer.setOnGround(packetClass.isOnGround());

        session.getModernClient().sendModernPacket(new ClientPlayerMovementPacket(betaPlayer.isOnGround()));
    }
}