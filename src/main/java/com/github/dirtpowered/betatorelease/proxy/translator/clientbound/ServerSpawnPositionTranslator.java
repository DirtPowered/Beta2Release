package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3SpawnPositionPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;

public class ServerSpawnPositionTranslator implements ModernToBetaHandler<ServerSpawnPositionPacket> {

    @Override
    public void translate(ServerSpawnPositionPacket packet, Session betaSession) {
        Position position = packet.getPosition();
        betaSession.sendPacket(new V1_7_3SpawnPositionPacketData(position.getX(), position.getY(), position.getZ()));
    }
}