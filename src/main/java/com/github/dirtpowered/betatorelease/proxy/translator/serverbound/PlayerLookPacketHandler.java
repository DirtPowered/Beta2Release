package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PlayerLookPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;

public class PlayerLookPacketHandler implements BetaToModernHandler<V1_7_3PlayerLookPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3PlayerLookPacketData packetClass) {
        boolean onGround = packetClass.isOnGround();
        float yaw = packetClass.getYaw();
        float pitch = packetClass.getPitch();

        session.getModernClient().sendModernPacket(new ClientPlayerRotationPacket(onGround, yaw, pitch));
    }
}