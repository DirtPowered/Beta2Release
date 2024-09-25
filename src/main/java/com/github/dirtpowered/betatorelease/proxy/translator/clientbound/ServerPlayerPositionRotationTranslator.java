package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PlayerLookMovePacketData;
import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.configuration.Configuration;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;

public class ServerPlayerPositionRotationTranslator implements ModernToBetaHandler<ServerPlayerPositionRotationPacket> {
    private static final double STANCE = 1.6200000047683716D;

    @Override
    public void translate(ServerPlayerPositionRotationPacket packet, Session betaSession) {
        double x = packet.getX();
        double y = packet.getY() + STANCE;
        double z = packet.getZ();

        float yaw = packet.getYaw();
        float pitch = packet.getPitch();

        BetaPlayer betaPlayer = betaSession.getBetaPlayer();
        betaPlayer.setOnGround(false);
        betaPlayer.setLocation(new Location(x, y, z, pitch, yaw));

        // send client settings only in the initial login phase
        if (!betaSession.isLoggedIn()) {
            Configuration config = betaSession.getServer().getConfiguration();
            betaSession.getModernClient().sendModernPacket(new ClientSettingsPacket(config.getLocale().getCode(), config.getRenderDistance(), ChatVisibility.FULL, true, SkinPart.values(), Hand.MAIN_HAND));
        }

        betaSession.getModernClient().sendModernPacket(new ClientPlayerPositionRotationPacket(false, x, y, z, yaw, pitch));
        betaSession.getModernClient().sendModernPacket(new ClientTeleportConfirmPacket(packet.getTeleportId()));
        betaSession.sendPacket(new V1_7_3PlayerLookMovePacketData(x, y, y, z, yaw, pitch, false));

        betaSession.setLoggedIn(true);
    }
}