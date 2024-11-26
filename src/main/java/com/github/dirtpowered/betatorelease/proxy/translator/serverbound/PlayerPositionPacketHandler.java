package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PlayerPositionPacketData;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;

public class PlayerPositionPacketHandler implements BetaToModernHandler<V1_7_3PlayerPositionPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3PlayerPositionPacketData packetClass) {
        if (!session.isLoggedIn())
            return;

        BetaPlayer player = session.getBetaPlayer();

        double x = packetClass.getX();
        double y = packetClass.getY();
        double z = packetClass.getZ();

        boolean onGround = packetClass.isOnGround();

        if (y > 0) {
            player.updateLocation(x, y, z);
            player.setOnGround(packetClass.isOnGround());
        }
        session.getModernClient().sendModernPacket(new ClientPlayerPositionPacket(onGround, x, y, z));
    }
}