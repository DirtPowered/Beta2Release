package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.BlockPlacePacketData;
import com.github.dirtpowered.betatorelease.data.utils.OldBlockFace;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;

public class BlockPlacePacketHandler implements BetaToModernHandler<BlockPlacePacketData> {

    @Override
    public void handlePacket(Session session, BlockPlacePacketData packetClass) {
        Position position = new Position(packetClass.getX(), packetClass.getY(), packetClass.getZ());
        BlockFace blockFace = OldBlockFace.translateBlockFace(packetClass.getDirection());

        float x = packetClass.getX();
        float y = packetClass.getX();
        float z = packetClass.getX();

        if (packetClass.getDirection() == -1) {
            session.getModernClient().sendModernPacket(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
            return;
        }

        session.getModernClient().sendModernPacket(new ClientPlayerPlaceBlockPacket(position, blockFace, Hand.MAIN_HAND, x, y, z));
    }
}
