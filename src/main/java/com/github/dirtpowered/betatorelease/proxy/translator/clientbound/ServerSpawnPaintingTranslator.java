package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PaintingPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;

public class ServerSpawnPaintingTranslator implements ModernToBetaHandler<ServerSpawnPaintingPacket> {

    @Override
    public void translate(ServerSpawnPaintingPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        Position position = packet.getPosition();
        PaintingType type = packet.getPaintingType();

        // Wither painting is the only one that doesn't exist in beta 1.7
        if (type == PaintingType.WITHER) type = PaintingType.KEBAB;

        int direction = MagicValues.value(Integer.class, packet.getDirection());
        String title = MagicValues.value(String.class, type);

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

        betaSession.sendPacket(new V1_7_3PaintingPacketData(entityId, x, y, z, direction, title));
    }
}