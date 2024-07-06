package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PaintingPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;

public class ServerSpawnPaintingTranslator implements ModernToBetaHandler<ServerSpawnPaintingPacket> {

    @Override
    public void translate(ServerSpawnPaintingPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        Position position = packet.getPosition();
        int direction = MagicValues.value(Integer.class, packet.getDirection());
        String title = MagicValues.value(String.class, packet.getPaintingType());

        betaSession.sendPacket(new PaintingPacketData(entityId, position.getX(), position.getY(), position.getZ(), direction, title));
    }
}