package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PlayerLookMovePacketData;
import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;

public class ServerPlayerPositionRotationTranslator implements ModernToBetaHandler<ServerPlayerPositionRotationPacket> {

    @Override
    public void translate(ServerPlayerPositionRotationPacket packet, Session betaSession) {
        double x = packet.getX();
        double y = packet.getY() + 1.6200000047683716D;
        double z = packet.getZ();

        float yaw = packet.getYaw();
        float pitch = packet.getPitch();

        BetaPlayer betaPlayer = betaSession.getBetaPlayer();
        betaPlayer.setOnGround(false);
        betaPlayer.setLocation(new Location(x, y, z, pitch, yaw));

        betaSession.getModernClient().sendModernPacket(new ClientPlayerPositionRotationPacket(false, x, y, z, yaw, pitch));
        betaSession.getModernClient().sendModernPacket(new ClientTeleportConfirmPacket(packet.getTeleportId()));

        betaSession.sendPacket(new V1_7_3PlayerLookMovePacketData(x, y, y, z, yaw, pitch, false));
        if (!betaPlayer.getSession().isLoggedIn())
            betaSession.joinPlayer();

        betaSession.setLoggedIn(true);
    }
}