package com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PlayerLookMovePacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;

public class PlayerLookMovePacketHandler implements BetaToModernHandler<PlayerLookMovePacketData> {

    @Override
    public void handlePacket(Session session, PlayerLookMovePacketData packetClass) {
        if (!session.isLoggedIn())
            return;

        BetaPlayer player = session.getBetaPlayer();

        double x = packetClass.getX();
        double y = packetClass.getY();
        double z = packetClass.getZ();

        float yaw = packetClass.getYaw();
        float pitch = packetClass.getPitch();

        if (!player.isInVehicle()) {
            session.getModernClient().sendModernPacket(new ClientPlayerPositionRotationPacket(packetClass.isOnGround(), x, y, z, yaw, pitch));
        } else {
            session.getModernClient().sendModernPacket(new ClientVehicleMovePacket(x, y, z, yaw, pitch));
        }
    }
}