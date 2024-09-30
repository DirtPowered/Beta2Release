package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3BlockDigPacketData;
import com.github.dirtpowered.betatorelease.data.chunk.Block;
import com.github.dirtpowered.betatorelease.data.chunk.BlockDigEntry;
import com.github.dirtpowered.betatorelease.data.chunk.BlockStorage;
import com.github.dirtpowered.betatorelease.data.utils.OldBlockFace;
import com.github.dirtpowered.betatorelease.data.utils.OldPlayerAction;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;

public class BlockDigPacketHandler implements BetaToModernHandler<V1_7_3BlockDigPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3BlockDigPacketData packetClass) {
        int status = packetClass.getStatus();
        int face = packetClass.getFace();

        BlockFace blockFace = OldBlockFace.translateBlockFace(face);
        PlayerAction playerAction = OldPlayerAction.getPlayerAction(status);

        if (playerAction == null || blockFace == null)
            return;

        BetaPlayer player = session.getBetaPlayer();
        BlockStorage blockStorage = session.getBlockStorage();

        int x = packetClass.getX();
        int y = packetClass.getY();
        int z = packetClass.getZ();

        final Position pos = new Position(x, y, z);
        // check if player is about to dig a door block
        final Block block = blockStorage.getBlockAt(x, y, z);

        // emulate left-click to open the door
        if (block != null && Utils.isDoor(block.getBlockId())) {
            // send an item use packet to open the door
            if (!player.isDigging()) { // prevent from sending multiple packets
                session.getModernClient().sendModernPacket(new ClientPlayerPlaceBlockPacket(pos, blockFace, Hand.MAIN_HAND, 0, 0, 0));
            }
        }

        BlockDigEntry entry = new BlockDigEntry();

        entry.setBlockFace(blockFace);
        entry.setPlayerAction(playerAction);
        entry.setPosition(pos);

        player.setDiggingEntry(entry);

        session.getModernClient().sendModernPacket(new ClientPlayerActionPacket(playerAction, pos, blockFace));
    }
}