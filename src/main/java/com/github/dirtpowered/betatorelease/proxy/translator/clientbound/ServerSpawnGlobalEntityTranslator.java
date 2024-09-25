package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ThunderboltPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.type.GlobalEntityType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;

public class ServerSpawnGlobalEntityTranslator implements ModernToBetaHandler<ServerSpawnGlobalEntityPacket> {

    @Override
    public void translate(ServerSpawnGlobalEntityPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();

        int x = Utils.toAbsolutePos(packet.getX());
        int y = Utils.toAbsolutePos(packet.getY());
        int z = Utils.toAbsolutePos(packet.getZ());

        if (packet.getType() == GlobalEntityType.LIGHTNING_BOLT) {
            betaSession.sendPacket(new V1_7_3ThunderboltPacketData(entityId, x, y, z, 1));
        }
    }
}