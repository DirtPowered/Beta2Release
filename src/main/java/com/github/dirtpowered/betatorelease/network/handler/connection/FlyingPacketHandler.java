package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.FlyingPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;

public class FlyingPacketHandler implements BetaToModernHandler<FlyingPacketData> {

    @Override
    public void handlePacket(Session session, FlyingPacketData packetClass) {
        BetaPlayer betaPlayer = session.getBetaPlayer();
        boolean isOnGround = packetClass.isOnGround();
        betaPlayer.setOnGround(isOnGround);

        session.getModernClient().sendModernPacket(new ClientPlayerMovementPacket(isOnGround));
    }
}
