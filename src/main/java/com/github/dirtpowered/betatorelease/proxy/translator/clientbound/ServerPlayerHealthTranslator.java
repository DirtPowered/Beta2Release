package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3UpdateHealthPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;

public class ServerPlayerHealthTranslator implements ModernToBetaHandler<ServerPlayerHealthPacket> {

    @Override
    public void translate(ServerPlayerHealthPacket packet, Session betaSession) {
        betaSession.sendPacket(new V1_7_3UpdateHealthPacketData((int) packet.getHealth()));
    }
}