package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

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

        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        switch (direction) {
            case 0 -> {
                direction = 2;
                z -= 1;
            }
            case 1 -> x += 1;
            case 2 -> {
                direction = 0;
                z += 1;
            }
            case 3 -> x -= 1;
        }

        betaSession.sendPacket(new PaintingPacketData(entityId, x, y, z, direction, title));
    }
}