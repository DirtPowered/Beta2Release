package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PlayerLookMovePacketData;
import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;

public class PlayerLookMovePacketHandler implements BetaToModernHandler<V1_7_3PlayerLookMovePacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3PlayerLookMovePacketData packetClass) {
        if (!session.isLoggedIn())
            return;

        BetaPlayer player = session.getBetaPlayer();

        double x = packetClass.getX();
        double y = packetClass.getY();
        double z = packetClass.getZ();

        float yaw = packetClass.getYaw();
        float pitch = packetClass.getPitch();

        player.setLocation(new Location(x, y, z, pitch, yaw));

        if (!player.isInVehicle()) {
            session.getModernClient().sendModernPacket(new ClientPlayerPositionRotationPacket(packetClass.isOnGround(), x, y, z, yaw, pitch));
        } else {
            session.getModernClient().sendModernPacket(new ClientVehicleMovePacket(x, y, z, yaw, pitch));
        }
    }
}