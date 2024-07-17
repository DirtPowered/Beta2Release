package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.SleepPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket;

public class ServerPlayerUseBedTranslator implements ModernToBetaHandler<ServerPlayerUseBedPacket> {

    @Override
    public void translate(ServerPlayerUseBedPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        Position position = packet.getPosition();

        betaSession.sendPacket(new SleepPacketData(entityId, false, position.getX(), position.getY(), position.getZ()));
    }
}