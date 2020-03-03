package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.PreChunkPacketData;
import com.github.dirtpowered.betatorelease.Utils.Utils;
import com.github.dirtpowered.betatorelease.data.chunk.BetaChunk;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.pmw.tinylog.Logger;

public class ServerChunkDataTranslator implements ModernToBetaHandler<ServerChunkDataPacket> {

    @Override
    public void translate(ServerChunkDataPacket packet, Session betaSession) {
        if (!betaSession.isLoggedIn()) {
            return;
        }

        Column chunkColumn = packet.getColumn();
        int xPosition = chunkColumn.getX();
        int zPosition = chunkColumn.getZ();

        BetaChunk betaChunk = new BetaChunk(xPosition, zPosition);

        betaSession.sendPacket(new PreChunkPacketData(xPosition, zPosition, true /* allocate space */));
        //https://wiki.vg/index.php?title=Protocol&oldid=689#Pre-Chunk_.280x32.29

        Chunk[] chunks = packet.getColumn().getChunks();

        int index = 0;
        while (index < chunks.length) {
            Chunk chunk = chunks[index];

            if (chunk == null) {
                index++;
                continue;
            }

            final int columnCurrentHeight = index * 16; //(0-127)
            if (betaSession.getServer().isDebugMode())
                Logger.info("currentHeight: {}", columnCurrentHeight);

            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockState blockState = chunk.getBlocks().get(x, y, z);
                        betaChunk.setBlock(x, y + columnCurrentHeight, z, Utils.isBlockAllowed(blockState.getId()) ? blockState.getId() : 1);
                        betaChunk.setMetaData(x, y + columnCurrentHeight, z, blockState.getData());
                        betaChunk.setBlockLight(x, z, y, chunk.getBlockLight().get(x, y, z));
                        betaChunk.setSkyLight(x, z, y, chunk.getSkyLight().get(x, y, z));
                    }
                }
            }
            index++;
        }

        betaSession.queueChunk(betaChunk);
    }
}
