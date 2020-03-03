package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.PlayerLookMovePacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;

public class PlayerLookMovePacketHandler implements BetaToModernHandler<PlayerLookMovePacketData> {

    @Override
    public void handlePacket(Session session, PlayerLookMovePacketData packetClass) {

        if (!session.isLoggedIn())
            return;

        float yaw = packetClass.getYaw();
        float pitch = packetClass.getPitch();

        boolean isOnGround = packetClass.isOnGround();

        session.getModernClient().sendModernPacket(new ClientPlayerRotationPacket(isOnGround, yaw, pitch));
    }
}
