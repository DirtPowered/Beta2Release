package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.BlockPlacePacketData;
import com.github.dirtpowered.betatorelease.data.utils.OldBlockFace;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;

public class BlockPlacePacketHandler implements BetaToModernHandler<BlockPlacePacketData> {

    @Override
    public void handlePacket(Session session, BlockPlacePacketData packetClass) {
        if (packetClass.getDirection() == -1) {
            session.getModernClient().sendModernPacket(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
            return;
        }

        Position position = new Position(packetClass.getX(), packetClass.getY(), packetClass.getZ());
        BlockFace blockFace = OldBlockFace.translateBlockFace(packetClass.getDirection());

        float x = packetClass.getX();
        float y = packetClass.getX();
        float z = packetClass.getX();

        session.getModernClient().sendModernPacket(new ClientPlayerPlaceBlockPacket(position, blockFace, Hand.MAIN_HAND, x, y, z));
    }
}