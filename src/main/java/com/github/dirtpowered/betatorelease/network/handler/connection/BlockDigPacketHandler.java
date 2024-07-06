package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.BlockDigPacketData;
import com.github.dirtpowered.betatorelease.data.utils.OldBlockFace;
import com.github.dirtpowered.betatorelease.data.utils.OldPlayerAction;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;

public class BlockDigPacketHandler implements BetaToModernHandler<BlockDigPacketData> {

    @Override
    public void handlePacket(Session session, BlockDigPacketData packetClass) {
        int status = packetClass.getStatus();
        int face = packetClass.getFace();
        int x = packetClass.getX();
        int y = packetClass.getY();
        int z = packetClass.getZ();

        Position position = new Position(x, y, z);
        BlockFace blockFace = OldBlockFace.translateBlockFace(face);
        PlayerAction playerAction = OldPlayerAction.getPlayerAction(status);

        if (playerAction == null || blockFace == null)
            return;

        session.getModernClient().sendModernPacket(new ClientPlayerActionPacket(playerAction, position, blockFace));
    }
}