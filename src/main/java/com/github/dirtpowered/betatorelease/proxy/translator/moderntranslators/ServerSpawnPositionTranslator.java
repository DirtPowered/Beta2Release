package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.SpawnPositionPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;

public class ServerSpawnPositionTranslator implements ModernToBetaHandler<ServerSpawnPositionPacket> {

    @Override
    public void translate(ServerSpawnPositionPacket packet, Session betaSession) {
        Position position = packet.getPosition();
        betaSession.sendPacket(new SpawnPositionPacketData(position.getX(), position.getY(), position.getZ()));
    }
}
