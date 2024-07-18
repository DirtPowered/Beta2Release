package com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PlayerPositionPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;

public class PlayerPositionPacketHandler implements BetaToModernHandler<PlayerPositionPacketData> {

    @Override
    public void handlePacket(Session session, PlayerPositionPacketData packetClass) {
        if (!session.isLoggedIn())
            return;

        double x = packetClass.getX();
        double y = packetClass.getY();
        double z = packetClass.getZ();

        boolean onGround = packetClass.isOnGround();

        session.getModernClient().sendModernPacket(new ClientPlayerPositionPacket(onGround, x, y, z));
    }
}